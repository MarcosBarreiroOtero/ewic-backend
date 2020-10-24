package es.ewic.backend.model.client;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Client implements Serializable {

	private static final long serialVersionUID = -1348076269598315669L;

	private int idClient;
	private String idGoogleLogin;
	private String firstName;
	private String lastName;
	private String email;

	public Client() {
		// empty constructor
	}

	public Client(String idGoogleLogin, String firstName, String lastName, String email) {
		this.idGoogleLogin = idGoogleLogin;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdClient() {
		return idClient;
	}

	public void setIdClient(int idClient) {
		this.idClient = idClient;
	}

	public String getIdGoogleLogin() {
		return idGoogleLogin;
	}

	public void setIdGoogleLogin(String idGoogleLogin) {
		this.idGoogleLogin = idGoogleLogin;
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

	public String completeName() {
		return (firstName != null && !firstName.isEmpty() ? firstName + " " : "") + (lastName == null ? "" : lastName);
	}

	public String completeNameFormal() {
		return (lastName != null && !lastName.isEmpty() ? lastName + ", " : "") + (firstName == null ? "" : firstName);
	}

}
