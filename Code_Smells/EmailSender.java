import java.util.logging.Logger;

public class EmailSender {

    private static final Logger logger = Logger.getLogger(EmailSender.class.getName());

    private EmailSender(){}

    public static void sendEmail(String customerEmail, String subject, String message){
        logger.log("Email to: " + customerEmail);
        logger.log("Subject: " + subject);
        logger.log("Body: " + message);
    }
}