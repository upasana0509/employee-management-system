package com.ems.util;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailUtility {
    public static void sendEmail(String toAddress, String subject, String messageContent) throws MessagingException {
        // SMTP settings (Using Mailtrap or Gmail SMTP)
        String host = "smtp.mailtrap.io"; 
        String port = "2525";
        final String userName = "your_username"; 
        final String password = "your_password"; 

        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };

        Session session = Session.getInstance(properties, auth);
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress("admin@ems.com"));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
        msg.setSubject(subject);
        msg.setText(messageContent);

        Transport.send(msg);
    }
}