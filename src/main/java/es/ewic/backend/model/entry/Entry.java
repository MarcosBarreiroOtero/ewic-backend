package es.ewic.backend.model.entry;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
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
public class Entry implements Serializable {

	private static final long serialVersionUID = 1372374395700192637L;

	private int idEntry;
	private Calendar start;
	private Calendar end;
	private long duration;

	private Shop shop;
	private Client client;

	public Entry() {
	}

	public Entry(Calendar start, Shop shop, Client client) {
		this.start = start;
		this.shop = shop;
		this.client = client;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdEntry() {
		return idEntry;
	}

	public void setIdEntry(int idEntry) {
		this.idEntry = idEntry;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	public Calendar getStart() {
		return start;
	}

	public void setStart(Calendar start) {
		this.start = start;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getEnd() {
		return end;
	}

	public void setEnd(Calendar end) {
		this.end = end;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shop")
	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client")
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

}
