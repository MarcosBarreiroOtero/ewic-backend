package es.ewic.backend.model.reservation;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import es.ewic.backend.model.client.Client;
import es.ewic.backend.model.shop.Shop;

@Entity
public class Reservation implements Serializable {

	private static final long serialVersionUID = -2479774326179655209L;

	public enum ReservationState {
		ACTIVE(1), COMPLETED(2), CANCELLED(3);

		private int numVal;

		ReservationState(int numVal) {
			this.numVal = numVal;
		}

		public int getNumVal() {
			return numVal;
		}
	}

	private int idReservation;
	private Calendar date;
	private ReservationState state;
	private String remarks;

	private Client client;
	private Shop shop;

	public Reservation() {
		// empty constructor
	}

	public Reservation(Calendar date, ReservationState state, String remarks, Client client, Shop shop) {
		this.date = date;
		this.state = state;
		this.remarks = remarks;
		this.client = client;
		this.shop = shop;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdReservation() {
		return idReservation;
	}

	public void setIdReservation(int idReservation) {
		this.idReservation = idReservation;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public ReservationState getState() {
		return state;
	}

	public void setState(ReservationState state) {
		this.state = state;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client")
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shop")
	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

}
