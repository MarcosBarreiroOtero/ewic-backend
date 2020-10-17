package es.ewic.backend.model.seller;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Seller implements Serializable {

	private static final long serialVersionUID = 1197410832802735188L;

	private int idSeller;
	private String loginName;
	private String password;
	private String firstName;
	private String lastName;
	private String email;

	public Seller() {
	}

	public Seller(String loginName, String password, String firstName, String lastName, String email) {
		this.loginName = loginName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdSeller() {
		return idSeller;
	}

	public void setIdSeller(int idSeller) {
		this.idSeller = idSeller;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
