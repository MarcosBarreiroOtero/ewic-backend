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
	private int actualCapacity;
	private ShopType type;
	private boolean allowEntries;
	private int idSeller;
	private String timetable;

	public ShopDetails(String name, float latitude, float longitude, String location, int maxCapacity, ShopType type,
			int idSeller, String timetable) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.location = location;
		this.maxCapacity = maxCapacity;
		this.type = type;
		this.idSeller = idSeller;
		this.timetable = timetable;
	}

	public ShopDetails(Shop shop) {
		this.idShop = shop.getIdShop();
		this.name = shop.getName();
		this.latitude = shop.getLatitude();
		this.longitude = shop.getLongitude();
		this.location = shop.getLocation();
		this.maxCapacity = shop.getMaxCapacity();
		this.actualCapacity = shop.getActualCapacity();
		this.allowEntries = shop.isAllowEntries();
		this.type = shop.getType();
		this.idSeller = shop.getSeller().getIdSeller();
		this.timetable = shop.getTimetable();
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

	public int getActualCapacity() {
		return actualCapacity;
	}

	public boolean isAllowEntries() {
		return allowEntries;
	}

	public void setAllowEntries(boolean allowEntries) {
		this.allowEntries = allowEntries;
	}

	public ShopType getType() {
		return type;
	}

	public int getIdSeller() {
		return idSeller;
	}

	public String getTimetable() {
		return timetable;
	}

}
