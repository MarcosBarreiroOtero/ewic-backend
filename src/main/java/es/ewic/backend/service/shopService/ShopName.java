package es.ewic.backend.service.shopService;

import es.ewic.backend.model.shop.Shop;

public class ShopName {

	private int idShop;
	private String name;
	private String timetable;
	private int maxCapacity;
	private String minutesBetweenReservations;
	private String minutesAfterOpeningMorning;
	private String minutesBeforeClosingMorning;
	private String minutesAfterOpeningAfternoon;
	private String minutesBeforeClosingAfternoon;

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

	public String getMinutesBetweenReservations() {
		return minutesBetweenReservations;
	}

	public void setMinutesBetweenReservations(String minutesBetweenReservations) {
		this.minutesBetweenReservations = minutesBetweenReservations;
	}

	public String getMinutesAfterOpeningMorning() {
		return minutesAfterOpeningMorning;
	}

	public void setMinutesAfterOpeningMorning(String minutesAfterOpeningMorning) {
		this.minutesAfterOpeningMorning = minutesAfterOpeningMorning;
	}

	public String getMinutesBeforeClosingMorning() {
		return minutesBeforeClosingMorning;
	}

	public void setMinutesBeforeClosingMorning(String minutesBeforeClosingMorning) {
		this.minutesBeforeClosingMorning = minutesBeforeClosingMorning;
	}

	public String getMinutesAfterOpeningAfternoon() {
		return minutesAfterOpeningAfternoon;
	}

	public void setMinutesAfterOpeningAfternoon(String minutesAfterOpeningAfternoon) {
		this.minutesAfterOpeningAfternoon = minutesAfterOpeningAfternoon;
	}

	public String getMinutesBeforeClosingAfternoon() {
		return minutesBeforeClosingAfternoon;
	}

	public void setMinutesBeforeClosingAfternoon(String minutesBeforeClosingAfternoon) {
		this.minutesBeforeClosingAfternoon = minutesBeforeClosingAfternoon;
	}

}
