package es.ewic.backend.service.mailService;

import java.util.Properties;
import java.util.concurrent.Future;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.ewic.backend.modelutil.ConfigurationGlobalNames;
import es.ewic.backend.service.configurationService.ConfigurationService;

@Service("mailService")
@Transactional
public class MailServiceImp implements MailService {

	@Autowired
	private ConfigurationService configurationService;

	private JavaMailSender getJavaMailSender(int idShop) {

		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		mailSender.setHost(
				configurationService.readControlParameterByNameAndShop(ConfigurationGlobalNames.MAIL_HOST, idShop));
		mailSender.setPort(Integer.parseInt(
				configurationService.readControlParameterByNameAndShop(ConfigurationGlobalNames.MAIL_PORT, idShop)));
		mailSender.setUsername(
				configurationService.readControlParameterByNameAndShop(ConfigurationGlobalNames.MAIL_USERNAME, idShop));
		mailSender.setPassword(
				configurationService.readControlParameterByNameAndShop(ConfigurationGlobalNames.MAIL_PASSWORD, idShop));

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");

		return mailSender;

	}

	@Override
	@Async
	public Future<String> sendMail(int idShop, String message, String to, String subject) {
		JavaMailSender mailSender = getJavaMailSender(idShop);

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
		try {
			helper.setText(message, true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setFrom(configurationService
					.readControlParameterByNameAndShop(ConfigurationGlobalNames.MAIL_USERNAME, idShop));
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return new AsyncResult<>("Okey");
	}

}
