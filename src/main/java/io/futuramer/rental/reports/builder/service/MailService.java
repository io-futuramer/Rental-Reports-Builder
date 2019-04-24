package io.futuramer.rental.reports.builder.service;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Class handler of E-Mail sending
 */
class MailService {

    private static final String SMTP_USERNAME = "io.futuramer.devops@gmail.com";
    private static final String SMTP_PASSWORD = "removed_due_to_security_reasons";
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";

    private Properties properties;
    private Session session;

    /**
     * Constructor, initializing properties
     */
    MailService() {
        properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);

        session = Session.getInstance(properties,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
                }
            }
        );
    }

    /**
     * Method to send report to specified recipients
     * @param message_recipients_map Map of recipients where
     *                               <K> is E-Mail address
     *                               <V> is a name of recipient
     * @param email_report_body body of E-Mail
     * @throws ReportException in case of SMTP or messaging issues
     */
    void sendReports(Map<String, String> message_recipients_map, String email_report_body) throws ReportException {
        try {
            for (Map.Entry<String, String> message_recipient_entry : message_recipients_map.entrySet()) {

                Message message = new MimeMessage(session);

                message.setFrom(new InternetAddress(SMTP_USERNAME));
                InternetAddress recipient = new InternetAddress(message_recipient_entry.getKey(), message_recipient_entry.getValue());
                message.setRecipient(Message.RecipientType.TO, recipient);

                message.setSubject("Testing Subject");
                message.setContent(email_report_body, "text/html");

                Transport.send(message);
            }
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
