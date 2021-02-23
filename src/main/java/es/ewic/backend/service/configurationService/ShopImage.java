package es.ewic.backend.service.configurationService;

import java.io.Serializable;

public class ShopImage implements Serializable {

	private static final long serialVersionUID = 3421750457160971608L;

	private String imageBase64;

	public ShopImage() {
	}

	public ShopImage(String imageBase64) {
		this.imageBase64 = imageBase64;
	}

	public String getImageBase64() {
		return imageBase64;
	}

}
