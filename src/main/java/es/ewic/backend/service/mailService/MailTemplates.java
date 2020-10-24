package es.ewic.backend.service.mailService;

import java.text.SimpleDateFormat;

import es.ewic.backend.model.reservation.Reservation;

public class MailTemplates {

	public static SimpleDateFormat sdfReservation = new SimpleDateFormat("dd 'de' MMMM 'a las' HH:mm");

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

}
