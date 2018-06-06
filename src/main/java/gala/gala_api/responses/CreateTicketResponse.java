package gala.gala_api.responses;

import gala.gala_api.entity.Ticket;

public class CreateTicketResponse {

    /**
     * The status of the createTicket call, true if it was succesful false if it was unsuccessful.
     */
    private boolean success;

    /**
     * A message specifying more information, to be shown to the user if the ticket creation was a failure.
     */
    private String message;

    /**
     * The ticket that was created, null if there was a failure.
     */
    private Ticket ticket;

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the ticket
	 */
	public Ticket getTicket() {
		return ticket;
	}

	/**
	 * @param ticket the ticket to set
	 */
	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}
    

    

}