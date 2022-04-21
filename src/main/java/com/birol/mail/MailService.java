package com.birol.mail;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import com.birol.ems.dto.EMPLOYEE_BASIC;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class MailService {

	Logger logger = LoggerFactory.getLogger(MailService.class);
	
	@Autowired
    private JavaMailSender emailSender;

    @Autowired
    private org.thymeleaf.spring5.SpringTemplateEngine templateEngine;

    public void sendEmail(Mail mail) throws MessagingException {
    	MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        Context context = new Context();
        context.setVariables(mail.getModel());
        String html = templateEngine.process("email/verify-code", context);

        helper.setTo(mail.getTo());
        helper.setText(html, true);
        helper.setSubject(mail.getSubject());
        helper.setFrom(mail.getFrom());

        emailSender.send(message);
        logger.info("Success send mail");
    }

    public void sendEmailForNewUser(EMPLOYEE_BASIC emp) throws MessagingException {
    	
    	Map<String, Object> maps = new HashMap<>();
    	String full_name= emp.getFirst_name()+" "+emp.getLast_name();
		maps.put("username", full_name);		

		Mail mail = new Mail();
		mail.setFrom("netlit.ems.se@gmail.com");
		mail.setSubject("Registration");
		mail.setTo(emp.getEmail());
		mail.setModel(maps);
    	
    	MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        Context context = new Context();
        context.setVariables(mail.getModel());
        String html = templateEngine.process("email/new-user", context);

        helper.setTo(mail.getTo());
        helper.setText(html, true);
        helper.setSubject(mail.getSubject());
        helper.setFrom(mail.getFrom());

        emailSender.send(message);
        logger.info("Success send mail");
    }

}
