package es.ewic.backend.service.shopService;

import es.ewic.backend.model.shop.Shop;

public class ShopName {

	private int idShop;
	private String name;
	private String timetable;

	public ShopName(int idShop, String name, String timetable) {
		this.idShop = idShop;
		this.name = name;
		this.timetable = timetable;
	}

	public ShopName(Shop shop) {
		this.idShop = shop.getIdShop();
		this.name = shop.getName();
		this.timetable = shop.getTimetable();
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

}
