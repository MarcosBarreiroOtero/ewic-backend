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

import es.ewic.backend.model.reservation.Reservation;
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

		if (mailSender.getHost().equals("") || mailSender.getUsername().equals("")
				|| mailSender.getPassword().equals("")) {
			return null;
		}

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.timeout", "10000");
		props.put("mail.smtp.connectiontimeout", "10000");
		props.put("mail.smtp.writetimeout", "10000");

		props.put("mail.debug", "true");

		return mailSender;

	}

	@Override
	@Async
	public Future<String> sendMail(int idShop, String message, String to, String subject) {
		JavaMailSender mailSender = getJavaMailSender(idShop);

		if (mailSender == null) {
			System.out.println("Email not configured");
			return new AsyncResult<>("Error");
		}

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
		try {
			helper.setText(message, true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setFrom(configurationService
					.readControlParameterByNameAndShop(ConfigurationGlobalNames.MAIL_USERNAME, idShop));
			mailSender.send(mimeMessage);
			return new AsyncResult<>("Email Send Successfully");
		} catch (MessagingException e) {
			e.printStackTrace();
			return new AsyncResult<>("Error");
		}

	}

	@Override
	@Async
	public Future<String> sendClientNewReservation(Reservation reservation) {
		String content = MailTemplates.newReservationClientMessage(reservation);
		String to = reservation.getClient().getEmail();
		String subject = MailTemplates.newReservationClientSubject(reservation);

		return sendMail(reservation.getShop().getIdShop(), content, to, subject);
	}

	@Override
	@Async
	public Future<String> sendSellerNewReservation(Reservation reservation) {
		String content = MailTemplates.newReservationSellerMessage(reservation);
		String to = reservation.getShop().getSeller().getEmail();
		String subject = MailTemplates.newReservationSellerSubject(reservation);

		return sendMail(reservation.getShop().getIdShop(), content, to, subject);
	}

}
