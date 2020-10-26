package es.ewic.backend.service.mailService;

import java.util.Calendar;
import java.util.concurrent.Future;

import es.ewic.backend.model.reservation.Reservation;

public interface MailService {

	public Future<String> sendMail(int idShop, String message, String to, String subject);

	public Future<String> sendClientNewReservation(Reservation reservation);

	public Future<String> sendSellerNewReservation(Reservation reservation);

	public Future<String> sendClientUpdateReservation(Reservation reservation, Calendar oldDate);

	public Future<String> sendSellerUpdateReservation(Reservation reservation, Calendar oldDate);

	public Future<String> sendClientDeleteReservation(Reservation reservation);

	public Future<String> sendSellerDeleteReservation(Reservation reservation);

}
