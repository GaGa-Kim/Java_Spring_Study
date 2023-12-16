package com.gaga.springtoby.user.service;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * 아무런 기능이 없는 MailSender 구현 클래스
 */
public class DummyMailSender implements MailSender {
	public void send(SimpleMailMessage mailMessage) throws MailException {
	}

	public void send(SimpleMailMessage[] mailMessage) throws MailException {
	}
}
