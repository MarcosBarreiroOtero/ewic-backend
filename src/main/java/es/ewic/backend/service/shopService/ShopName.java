package es.ewic.backend.service.shopService;

import es.ewic.backend.model.shop.Shop;

public class ShopName {

	private int idShop;
	private String name;

	public ShopName(int idShop, String name) {
		this.idShop = idShop;
		this.name = name;
	}

	public ShopName(Shop shop) {
		this.idShop = shop.getIdShop();
		this.name = shop.getName();
	}

	public int getIdShop() {
		return idShop;
	}

	public String getName() {
		return name;
	}

}
