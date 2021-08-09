package com.example.candy.service.email;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.candy.domain.user.User;
import com.example.candy.service.user.UserService;

@Service
public class MailService {
	
	@Autowired
	private JavaMailSender MailSender;
	
    @Autowired 
    private UserService userService;
	
	@Value("${spring.mail.username}")
	private String emailSender;
	
	private String authCode;
	
	@Transactional
	public void mailSend(User user) {
		SimpleMailMessage simpleMessage = new SimpleMailMessage();
	
		setAuthCode(user, getAuthCode());
		
		simpleMessage.setFrom(emailSender);
		simpleMessage.setTo(user.getEmail());
		simpleMessage.setSubject("Test");
		simpleMessage.setText(authCode);
		
		MailSender.send(simpleMessage);
	}
	
	@Transactional
	public String getAuthCode() {
		Random random = new Random();
		StringBuffer buffer = new StringBuffer();
		int num;
		
		while(buffer.length() < 6) {
			num = random.nextInt(10);
			buffer.append(num);
		}
		
		return buffer.toString();
	}
	
	@Transactional
	public void setAuthCode(User user, String auth) {
		user.setAuthCode(auth);
		this.authCode = auth;
	}
	
	

}
