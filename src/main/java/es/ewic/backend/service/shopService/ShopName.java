package es.ewic.backend.service.shopService;

import es.ewic.backend.model.shop.Shop;

public class ShopName {

	private int idShop;
	private String name;
	private String timetable;
	private int maxCapacity;

	public ShopName(int idShop, String name, String timetable, int maxCapacity) {
		this.idShop = idShop;
		this.name = name;
		this.timetable = timetable;
		this.maxCapacity = maxCapacity;
	}

	public ShopName(Shop shop) {
		this.idShop = shop.getIdShop();
		this.name = shop.getName();
		this.timetable = shop.getTimetable();
		this.maxCapacity = shop.getMaxCapacity();
	}

	public int getIdShop() {
		return idShop;
	}

	public String getName() {
		return name;
	}

	public String getTimetable() {
		return timetable;
	}

	public int getMaxCapacity() {
		return maxCapacity;
	}

}
