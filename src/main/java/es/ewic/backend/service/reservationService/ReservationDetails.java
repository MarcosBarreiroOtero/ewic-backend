package es.ewic.backend.service.reservationService;

import es.ewic.backend.model.reservation.Reservation;
import es.ewic.backend.model.reservation.Reservation.ReservationState;
import es.ewic.backend.modelutil.DateUtils;

public class ReservationDetails {

	private int idReservation;
	private String date;
	private ReservationState state;
	private String remarks;
	private int nClients;
	private String idGoogleLoginClient;
	private int idShop;
	private String shopName;

	public ReservationDetails(String date, String remarks, int nClients, String idGoogleLoginClient, int idShop) {
		this.date = date;
		this.state = ReservationState.ACTIVE;
		this.remarks = remarks;
		this.nClients = nClients == 0 ? 1 : nClients;
		this.idGoogleLoginClient = idGoogleLoginClient;
		this.idShop = idShop;
	}

	public ReservationDetails(Reservation reservation) {
		this.idReservation = reservation.getIdReservation();
		this.date = DateUtils.formatDateLong(reservation.getDate());
		this.state = reservation.getState();
		this.remarks = reservation.getRemarks();
		this.nClients = reservation.getnClients();
		this.idGoogleLoginClient = reservation.getClient().getIdGoogleLogin();
		this.idShop = reservation.getShop().getIdShop();
		this.shopName = reservation.getShop().getName();
	}

	public int getIdReservation() {
		return idReservation;
	}

	public String getDate() {
		return date;
	}

	public ReservationState getState() {
		return state;
	}

	public String getRemarks() {
		return remarks;
	}

	public int getnClients() {
		return nClients;
	}

	public String getIdGoogleLoginClient() {
		return idGoogleLoginClient;
	}

	public void setIdGoogleLoginClient(String idGoogleLoginClient) {
		this.idGoogleLoginClient = idGoogleLoginClient;
	}

	public int getIdShop() {
		return idShop;
	}

	public String getShopName() {
		return shopName;
	}

}
