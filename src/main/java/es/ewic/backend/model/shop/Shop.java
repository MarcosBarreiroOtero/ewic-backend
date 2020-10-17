package es.ewic.backend.model.shop;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import es.ewic.backend.model.seller.Seller;
import es.ewic.backend.service.shopService.ShopDetails;

@Entity
public class Shop implements Serializable {

	private static final long serialVersionUID = -6896714588922492560L;

	public enum ShopType {
		OTHER(1), FRUIT_STORE(2);

		private int numVal;

		ShopType(int numVal) {
			this.numVal = numVal;
		}

		public int getNumVal() {
			return numVal;
		}
	}

	private int idShop;
	private String name;
	private float latitude;
	private float longitude;
	private String location;
	private int maxCapacity;
	private ShopType type;

	private Seller seller;

	public Shop() {
		// empty constructor
	}

	public Shop(String name, float latitude, float longitude, String location, int maxCapacity, ShopType type,
			Seller seller) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.location = location;
		this.maxCapacity = maxCapacity;
		this.type = type;
		this.seller = seller;
	}

	public Shop(ShopDetails details, Seller seller) {
		this.name = details.getName();
		this.longitude = details.getLongitude();
		this.latitude = details.getLatitude();
		this.location = details.getLocation();
		this.maxCapacity = details.getMaxCapacity();
		this.type = details.getType();
		this.seller = seller;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdShop() {
		return idShop;
	}

	public void setIdShop(int idShop) {
		this.idShop = idShop;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getMaxCapacity() {
		return maxCapacity;
	}

	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

	public ShopType getType() {
		return type;
	}

	public void setType(ShopType type) {
		this.type = type;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "seller")
	public Seller getSeller() {
		return seller;
	}

	public void setSeller(Seller seller) {
		this.seller = seller;
	}

}
