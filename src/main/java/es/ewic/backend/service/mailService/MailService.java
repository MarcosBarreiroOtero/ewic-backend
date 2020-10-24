package es.ewic.backend.service.mailService;

import java.util.concurrent.Future;

public interface MailService {

	public Future<String> sendMail(int idShop, String message, String to, String subject);

}
