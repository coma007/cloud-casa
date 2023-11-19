package com.casa.app.util.email;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import com.casa.app.estate.RealEstate;
import com.casa.app.estate.RealEstateDTO;
import com.casa.app.request.RealEstateRequest;
import com.casa.app.request.RealEstateRequestDTO;
import com.casa.app.user.regular_user.RegularUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;


import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private Integer port;

    @Value("${spring.mail.username}")
    private String user;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean auth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean starttls;

    private Properties getMailProperties(){
        Properties props = new Properties();
        props.put("mail.smtp.host", host); //SMTP Host
        props.put("mail.smtp.port", port); //TLS Port
        props.put("mail.smtp.auth", auth); //enable authentication
        props.put("mail.smtp.starttls.enable", starttls); //enable STARTTLS
        props.setProperty("mail.user", user);
        props.setProperty("mail.password", password);
        return props;
    }

    private Session getSession() {
        Properties props = getMailProperties();
        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(props.getProperty("mail.user"), props.getProperty("mail.password"));
            }
        };
        return Session.getInstance(props, auth);
    }


    public void sendVerificationEmail(RegularUser user, String url) throws UnsupportedEncodingException, jakarta.mail.MessagingException {
        String toAddress = user.getUsername();
        String fromAddress = getMailProperties().getProperty("mail.user");
        String senderName = "Casa";


        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Casa";

        content = content.replace("[[name]]", user.getFirstName());
        content = content.replace("[[URL]]", url);

        Session session = getSession();
        sendEmail(session, toAddress, subject, content);
    }

    public void sendNotificationEmail(RealEstateRequest request) throws UnsupportedEncodingException, jakarta.mail.MessagingException {
        String toAddress = request.getRealEstate().getOwner().getUser().getUsername();
        String fromAddress = getMailProperties().getProperty("mail.user");
        String senderName = "Casa";

        String subject = "Real Estate Notification";
        String content = "Dear [[name]],<br>"
                + "Your request for real estate registration <b>[[estate]]</b> has been processed.<br>"
                + "<b>Current status</b>: [[status]] <br>"
                + "<b>Comment</b>: [[comment]] <br>"
                + "Thank you,<br>"
                + "Casa";

        content = content.replace("[[name]]", request.getRealEstate().getOwner().getUser().getFirstName());
        content = content.replace("[[estate]]", request.getRealEstate().getName());
        content = content.replace("[[status]]", request.isApproved() ? "approved" : "declined");
        content = content.replace("[[comment]]", request.getComment());

        Session session = getSession();
        sendEmail(session, toAddress, subject, content);
    }

    private void sendEmail(Session session, String toEmail, String subject, String body) throws jakarta.mail.MessagingException, UnsupportedEncodingException{
        session.setDebug(true);
        MimeMessage msg = new MimeMessage(session);
        Properties props = getMailProperties();

        msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
        msg.addHeader("format", "flowed");
        msg.addHeader("Content-Transfer-Encoding", "8bit");

        msg.setFrom(new InternetAddress(props.getProperty("mail.user"), "Casa"));

        msg.setReplyTo(InternetAddress.parse("no_reply@example.com", false));

        msg.setSubject(subject, "UTF-8");

        msg.setContent(body, "text/html");

        msg.setSentDate(new Date());

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
        System.out.println("Message is ready");
        Transport.send(msg);

        System.out.println("Email Sent Successfully!!");
    }

}