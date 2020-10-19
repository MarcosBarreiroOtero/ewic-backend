package es.ewic.backend.service.shopService;

import es.ewic.backend.model.shop.Shop;
import es.ewic.backend.model.shop.Shop.ShopType;

public class ShopDetails {

	private int idShop;
	private String name;
	private float latitude;
	private float longitude;
	private String location;
	private int maxCapacity;
	private ShopType type;
	private int idSeller;

	public ShopDetails(String name, float latitude, float longitude, String location, int maxCapacity, ShopType type,
			int idSeller) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.location = location;
		this.maxCapacity = maxCapacity;
		this.type = type;
		this.idSeller = idSeller;
	}

	public ShopDetails(Shop shop) {
		this.idShop = shop.getIdShop();
		this.name = shop.getName();
		this.latitude = shop.getLatitude();
		this.longitude = shop.getLongitude();
		this.location = shop.getLocation();
		this.maxCapacity = shop.getMaxCapacity();
		this.type = shop.getType();
		this.idSeller = shop.getSeller().getIdSeller();
	}

	public int getIdShop() {
		return idShop;
	}

	public String getName() {
		return name;
	}

	public float getLatitude() {
		return latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public String getLocation() {
		return location;
	}

	public int getMaxCapacity() {
		return maxCapacity;
	}

	public ShopType getType() {
		return type;
	}

	public int getIdSeller() {
		return idSeller;
	}

}
