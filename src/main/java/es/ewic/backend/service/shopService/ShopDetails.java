package es.ewic.backend.service.shopService;

import es.ewic.backend.model.shop.Shop;
import es.ewic.backend.model.shop.Shop.ShopType;

public class ShopDetails {

	private Integer idShop;
	private String name;
	private String location;
	private ShopType type;
	private int idSeller;

	public ShopDetails(String name, String location, ShopType type, int idSeller) {
		this.idShop = 0;
		this.name = name;
		this.location = location;
		this.type = type;
		this.idSeller = idSeller;
	}

	public ShopDetails(Shop shop) {
		this.idShop = shop.getIdShop();
		this.name = shop.getName();
		this.location = shop.getLocation();
		this.type = shop.getType();
		this.idSeller = shop.getSeller().getIdSeller();
	}

	public int getIdShop() {
		return idShop;
	}

	public String getName() {
		return name;
	}

	public String getLocation() {
		return location;
	}

	public ShopType getType() {
		return type;
	}

	public int getIdSeller() {
		return idSeller;
	}

}
