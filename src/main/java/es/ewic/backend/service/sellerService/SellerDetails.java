package es.ewic.backend.service.sellerService;

import es.ewic.backend.model.seller.Seller;

public class SellerDetails {

	private int idSeller;
	private String loginName;
	private String firstName;
	private String lastName;
	private String email;

	public SellerDetails(int idSeller, String loginName, String firstName, String lastName, String email) {
		this.idSeller = idSeller;
		this.loginName = loginName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public SellerDetails(Seller seller) {
		this.idSeller = seller.getIdSeller();
		this.loginName = seller.getLoginName();
		this.firstName = seller.getFirstName();
		this.lastName = seller.getLastName();
		this.email = seller.getEmail();
	}

	public int getIdSeller() {
		return idSeller;
	}

	public String getLoginName() {
		return loginName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

}
