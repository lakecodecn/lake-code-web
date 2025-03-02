package cn.lakecode.web.manager;

import cn.lakecode.web.bo.MailConf;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class MailManager {


    public static void sendMail(MailConf mailConf, String to, String subject, String content) {
        JavaMailSenderImpl sender = sender(mailConf);
        MimeMessage message = sender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setFrom(mailConf.getFromName() + " <" + mailConf.getFrom() + ">");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            sender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("send email fail");
        }
    }

    private static JavaMailSenderImpl sender(MailConf mailConf) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailConf.getHost());
        mailSender.setPort(mailConf.getPort());

        mailSender.setUsername(mailConf.getUsername());
        mailSender.setPassword(mailConf.getPassword());
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false");
        return mailSender;
    }


}
