package es.ewic.backend.modelutil;

public class ConfigurationGlobalNames {

	private ConfigurationGlobalNames() {
	}

	// Mail params
	public static final String MAIL_HOST = "mailHost";
	public static final String MAIL_PORT = "mailPort";
	public static final String MAIL_USERNAME = "mailUsername";
	public static final String MAIL_PASSWORD = "mailPassword";

	// Reservation params
	public static final String RESERVATION_WAIT_MINUTES = "reservationWaitMinutes";
	public static final String MINUTES_BETWEEN_RESERVATIONS = "minutesBetweenReservations";
	public static final String MINUTES_AFTER_OPENING_MORNING = "minutesAfterOpeningMorning";
	public static final String MINUTES_BEFORE_CLOSING_MORNING = "minutesBeforeClosingMorning";
	public static final String MINUTES_AFTER_OPENING_AFTERNOON = "minutesAfterOpeningAfternoon";
	public static final String MINUTES_BEFORE_CLOSING_AFTERNOON = "minutesBeforeClosingAfternoon";

}
