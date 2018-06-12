package gala.gala_api.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import gala.gala_api.service.email.EmailService;
import gala.gala_api.service.email.SendTicketEmail;
import org.springframework.beans.factory.annotation.Autowired;

import gala.gala_api.dao.EventCrudDao;
import gala.gala_api.dao.TicketCrudDao;
import gala.gala_api.entity.Event;
import gala.gala_api.entity.Ticket;
import gala.gala_api.entity.TicketStatus;
import gala.gala_api.responses.CreateTicketResponse;
import gala.gala_api.responses.ValidateTicketResponse;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class TicketService {

  @Autowired
  private TicketCrudDao ticketCrudDao;

  @Autowired
  private EventCrudDao eventCrudDao;

  @Autowired
  private AwsS3Service awsS3Service;

  @Autowired
  private EmailService emailService;

	public CreateTicketResponse createTicket(Long eventId, String email) {
    CreateTicketResponse response = new CreateTicketResponse();
    Ticket newTicket = new Ticket();
    newTicket.setEmail(email);
    newTicket.setStatus(TicketStatus.ACTIVE); //Might not be always the case, but for now makes sense.
    Optional<Event> maybeEvent = this.eventCrudDao.findById(eventId);
    Event event;

    if (maybeEvent.isPresent()) {
      event = maybeEvent.get();
    } else {
      response.setSuccess(false);
      response.setMessage("The event with Id: " + eventId.toString() + " could not be found");
      return response;
    }

    if (event.getCapacity() > ticketCrudDao.findByEvent(event).size()) {
      newTicket.setEvent(event);
      response.setTicket(ticketCrudDao.save(newTicket));

      this.generateAndUploadQRCode(newTicket.getId());
      emailService.sendEmail(email, new SendTicketEmail(event.getName(), newTicket.getId()));

      response.setSuccess(true);
      response.setMessage("Ticket successfully added.");
      return response;
    } else {
      response.setSuccess(false);
      response.setMessage("Event capacity has already been reached.");
      return response;
    }
  }

  public CreateTicketResponse createTicketv2(long eventId, String email) {
	  Optional<Event> maybeEvent = eventCrudDao.findById(eventId);

	  if (maybeEvent.isPresent()) {
	    Event event = maybeEvent.get();
	    if (areTicketsRemaining(event)) {
        Ticket ticket = createNewTicket(event, email);
        ticketCrudDao.save(ticket);
        generateAndSendQrCode(ticket);

        return createResponse(true, "Ticket successfully added.");
      } else {
	      return createResponse(false, "Event capacity has already been reached.");
      }
    } else {
	    return createResponse(false, "No event exists with id: " + eventId);
    }
  }

  private boolean areTicketsRemaining(Event event) {
    List<Ticket> existingTicketsForEvent = ticketCrudDao.findByEvent(event);
    return event.getCapacity() > existingTicketsForEvent.size();
  }

  public ValidateTicketResponse validateTicket(Long ticketId) {
    ValidateTicketResponse response = new ValidateTicketResponse();
    Ticket ticket;
    Optional<Ticket> maybeTicket = ticketCrudDao.findById(ticketId);

    if (maybeTicket.isPresent()) {
      ticket = maybeTicket.get();
    } else {
      response.setSuccess(false);
      response.setMessage("Ticket with Id " + ticketId.toString() + " could not be found.");
      return response;
    }

    if (ticket.getStatus() == TicketStatus.ACTIVE) {
      ticket.setStatus(TicketStatus.VALIDATED);
      ticketCrudDao.save(ticket);
      response.setSuccess(true);
      response.setMessage("Ticket with Id " + ticketId.toString() + " was successfully validated.");
      return response;
    } else if (ticket.getStatus() == TicketStatus.VOIDED) {
      response.setSuccess(false);
      response.setMessage("Ticket with Id " + ticketId.toString() + " was voided. Could not validate.");
      return response;
    } else if (ticket.getStatus() == TicketStatus.VALIDATED){
      response.setSuccess(false);
      response.setMessage("Ticket with Id " + ticketId.toString() + " has already been validated. Could not validate.");
      return response;
    } else {
      response.setSuccess(false);
      response.setMessage("Could not ascertain whether ticket with Id "
              + ticketId.toString() + " was active or not. Could not validate");
      return response;
    }
  }

  //TODO Talk about error handling
  private void generateAndUploadQRCode(String qrValue) {
    QRCodeWriter qrCodeWriter = new QRCodeWriter();
    BitMatrix bitMatrix;
    try {
      bitMatrix = qrCodeWriter.encode(qrValue, BarcodeFormat.QR_CODE, 200, 200);
    } catch (WriterException e) {
      e.printStackTrace();
      bitMatrix = null;
      //TODO Error handling here
    }

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    try {
      MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream);
    } catch (IOException e) {
      e.printStackTrace();
    }
    byte[] s3Obj = byteArrayOutputStream.toByteArray();

    awsS3Service.putS3ObjectFromByteArray(s3Obj, "gala-cdn", "prod/autogenerated/qr-codes/qr-" + qrValue + ".png");
  }
}