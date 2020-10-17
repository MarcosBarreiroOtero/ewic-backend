package es.ewic.backend.model.controlParameter;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import es.ewic.backend.model.shop.Shop;

@Entity
public class ControlParameter implements Serializable {

	private static final long serialVersionUID = 7622284617878698183L;

	private int idControlParameter;
	private String name;
	private String value;

	private Shop shop;

	public ControlParameter() {
		// empty constructor
	}

	public ControlParameter(String name, String value, Shop shop) {
		this.name = name;
		this.value = value;
		this.shop = shop;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdControlParameter() {
		return idControlParameter;
	}

	public void setIdControlParameter(int idControlParameter) {
		this.idControlParameter = idControlParameter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shop")
	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

}
