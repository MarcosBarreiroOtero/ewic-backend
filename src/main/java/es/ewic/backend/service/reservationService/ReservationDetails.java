package es.ewic.backend.service.reservationService;

import es.ewic.backend.model.reservation.Reservation;
import es.ewic.backend.model.reservation.Reservation.ReservationState;
import es.ewic.backend.modelutil.DateUtils;

public class ReservationDetails {

	public int idReservation;
	public String date;
	public ReservationState state;
	public String remarks;
	public String idGoogleLoginClient;
	public int idShop;

	public ReservationDetails(String date, ReservationState state, String remarks, String idGoogleLoginClient,
			int idShop) {
		this.date = date;
		this.state = state;
		this.remarks = remarks;
		this.idGoogleLoginClient = idGoogleLoginClient;
		this.idShop = idShop;
	}

	public ReservationDetails(Reservation reservation) {
		this.idReservation = reservation.getIdReservation();
		this.date = DateUtils.formatDateLong(reservation.getDate());
		this.state = reservation.getState();
		this.remarks = reservation.getRemarks();
		this.idGoogleLoginClient = reservation.getClient().getIdGoogleLogin();
		this.idShop = reservation.getShop().getIdShop();
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

	public String getIdGoogleLoginClient() {
		return idGoogleLoginClient;
	}

	public void setIdGoogleLoginClient(String idGoogleLoginClient) {
		this.idGoogleLoginClient = idGoogleLoginClient;
	}

	public int getIdShop() {
		return idShop;
	}
}
