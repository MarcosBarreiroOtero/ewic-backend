package es.ewic.backend.modelutil;

public class NoAuthorizedOperationsNames {

	private NoAuthorizedOperationsNames() {
	}

	// SHOP
	public static final String MAX_CAPACITY_INVALID = "Max capacity less than 0";

	// RESERVATION
	public static final String CHANGE_CLIENT_OR_SHOP = "Change client or shop";
	public static final String MOVE_RESERVATION_TO_PAST = "Move reservation to past";
	public static final String RESERVATION_NOT_MUTABLE = "Reservation not mutable";
	public static final String RESERVATION_WHEN_SHOP_FULL = "Reservation when shop full";

	// ENTRY
	public static final String SHOP_NOT_OPENED = "Shop not opened";
	public static final String CLIENT_ALREADY_ENTERED = "Client already entered";
	public static final String EXIT_ALREADY_REGISTERED = "Exit already registered";

}
