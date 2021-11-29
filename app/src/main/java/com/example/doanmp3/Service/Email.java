package com.example.doanmp3.Service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email  {
    String username = "musicplayersieucapvippro@gmail.com";
    String password = "simond352001";
    Properties prop;
    Session session;

    public Email() {
        prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.post", "587");
        session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public boolean SendTo(String Email, String Subject, String Body){
        try {
            Message mess = new MimeMessage(session);
            mess.setFrom(new InternetAddress(username));
            mess.setRecipients(Message.RecipientType.TO, InternetAddress.parse(Email));
            mess.setSubject(Subject);
            mess.setText(Body);
            Transport.send(mess);
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }


}
