package es.ewic.backend.service.mailService;

import java.util.concurrent.Future;

import es.ewic.backend.model.reservation.Reservation;

public interface MailService {

	public Future<String> sendMail(int idShop, String message, String to, String subject);

	public Future<String> sendClientNewReservation(Reservation reservation);

	public Future<String> sendSellerNewReservation(Reservation reservation);

}
