package es.ewic.backend.service.mailService;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import es.ewic.backend.model.reservation.Reservation;

public class MailTemplates {

	public static SimpleDateFormat sdfReservation = new SimpleDateFormat("dd 'de' MMMM 'a las' HH:mm");

	// Email new reservation to client

	public static String newReservationClientSubject(Reservation rsv) {
		return "Nueva reserva en " + rsv.getShop().getName() + ": " + sdfReservation.format(rsv.getDate().getTime());
	}

	public static String newReservationClientMessage(Reservation rsv) {
		String content = "<html><p>Estimado " + rsv.getClient().completeNameFormal() + ",</p>";

		content += "<p>Le comunicamos que se acaba de crear una reserva en <strong>" + rsv.getShop().getName()
				+ "</strong> el <strong>" + sdfReservation.format(rsv.getDate().getTime()) + "</strong>.</p>";

		content += "<p>Saludos.</p></html>";

		return content;

	}

	// Email new reservation to seller

	public static String newReservationSellerSubject(Reservation rsv) {
		return "Nueva reserva cliente " + rsv.getClient().completeNameFormal() + ": "
				+ sdfReservation.format(rsv.getDate().getTime());
	}

	public static String newReservationSellerMessage(Reservation rsv) {
		String content = "<html><p>Estimado " + rsv.getShop().getSeller().completeNameFormal() + ",</p>";

		content += "<p>Le comunicamos que se acaba de crear una nueva reserva en <strong>" + rsv.getShop().getName()
				+ "</strong> por el cliente <strong>" + rsv.getClient().completeNameFormal() + "</strong> el <strong>"
				+ sdfReservation.format(rsv.getDate().getTime()) + "</strong>.</p>";

		content += "<p>Saludos.</p></html>";

		return content;

	}

	// Update reservation to client

	public static String updateReservationClientSubject(Reservation rsv) {
		return "Actualización reserva en " + rsv.getShop().getName();
	}

	public static String updateReservationClientMessage(Reservation rsv, Calendar oldDate) {
		String content = "<html><p>Estimado " + rsv.getClient().completeNameFormal() + ",</p>";

		content += "<p>Le comunicamos que su en reserva <strong>" + rsv.getShop().getName() + "</strong> el "
				+ sdfReservation.format(oldDate.getTime()) + " se ha cambiado para el <strong>"
				+ sdfReservation.format(rsv.getDate().getTime()) + "</strong>.</p>";

		content += "<p>Saludos.</p></html>";

		return content;
	}

	// Update reservation to seller

	public static String updateReservationSellerSubject(Reservation rsv) {
		return "Actualización reserva cliente " + rsv.getClient().completeNameFormal();
	}

	public static String updateReservationSellerMessage(Reservation rsv, Calendar oldDate) {
		String content = "<html><p>Estimado " + rsv.getShop().getSeller().completeNameFormal() + ",</p>";

		content += "<p>Le comunicamos que la reserva del cliente <strong>" + rsv.getClient().completeNameFormal()
				+ "</strong> el " + sdfReservation.format(oldDate.getTime()) + " ha cambiado su fecha para el <strong>"
				+ sdfReservation.format(rsv.getDate().getTime()) + "</strong>.</p>";

		content += "<p>Saludos.</p></html>";

		return content;
	}

	// Delete reservation to client
	public static String deleteReservationClientSubject(Reservation rsv) {
		return "Cancelación reserva en " + rsv.getShop().getName() + ": "
				+ sdfReservation.format(rsv.getDate().getTime());
	}

	public static String deleteReservationClientMessage(Reservation rsv) {
		String content = "<html><p>Estimado " + rsv.getClient().completeNameFormal() + ",</p>";

		content += "<p>Le comunicamos que su reserva en <strong>" + rsv.getShop().getName() + "</strong> el <strong>"
				+ sdfReservation.format(rsv.getDate().getTime()) + "</strong> acaba cancelarse.</p>";

		content += "<p>Saludos.</p></html>";

		return content;
	}

	// Delete reservation to seller
	public static String deleteReservationSellerSubject(Reservation rsv) {
		return "Cancelación reserva cliente " + rsv.getClient().completeNameFormal() + ": "
				+ sdfReservation.format(rsv.getDate().getTime());
	}

	public static String deleteReservationSellerMessage(Reservation rsv) {
		String content = "<html><p>Estimado " + rsv.getShop().getSeller().completeNameFormal() + ",</p>";

		content += "<p>Le comunicamos que la  reserva en <strong>" + rsv.getShop().getName()
				+ "</strong> del cliente <strong>" + rsv.getClient().completeNameFormal() + "</strong> el <strong>"
				+ sdfReservation.format(rsv.getDate().getTime()) + "</strong> acaba de cancelarse.</p>";

		content += "<p>Saludos.</p></html>";

		return content;

	}

}
