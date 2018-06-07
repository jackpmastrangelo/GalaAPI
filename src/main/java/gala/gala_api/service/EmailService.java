package gala.gala_api.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

  final String FROM = "jmastrangelo111@gmail.com";

  final String SUBJECT = "An important message from the dipshit at Gala";

  final String TEST_EMAIL_CONTENT_HTML = "Dear sir/madam, <br />Hello! :)";

  final String TEST_EMAIL_CONTENT_TEXT = "Dear sir/madam,\nHello! :)";

  AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder
          .standard().withRegion(Regions.US_EAST_1).build();

  public void sampleEmail() {
    try {

      SendEmailRequest request = new SendEmailRequest()
              .withDestination(
                      new Destination().withToAddresses( FROM ) )
              .withMessage(new Message()
                      .withBody(new Body()
                              .withHtml(new Content()
                                      .withCharset("UTF-8").withData(TEST_EMAIL_CONTENT_HTML))
                              .withText(new Content()
                                      .withCharset("UTF-8").withData(TEST_EMAIL_CONTENT_TEXT)))
                      .withSubject(new Content()
                              .withCharset("UTF-8").withData(SUBJECT)))
              .withSource(FROM);

      client.sendEmail(request);

      System.out.println("Email sent!");


    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
