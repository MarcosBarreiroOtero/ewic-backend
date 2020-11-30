package es.ewic.backend.controller;

import java.util.ArrayList;
import java.util.List;

import es.ewic.backend.model.controlParameter.ControlParameter;
import es.ewic.backend.model.entry.Entry;
import es.ewic.backend.model.reservation.Reservation;
import es.ewic.backend.model.shop.Shop;
import es.ewic.backend.service.configurationService.ControlParameterDetails;
import es.ewic.backend.service.reservationService.ReservationDetails;
import es.ewic.backend.service.shopService.EntryDetails;
import es.ewic.backend.service.shopService.ShopDetails;

public class TransformationUtils {

	private TransformationUtils() {

	}

	// Reservation
	public static List<ReservationDetails> reservationsToReservationsDetails(List<Reservation> reservations) {
		List<ReservationDetails> reservationsDetails = new ArrayList<>();
		for (Reservation rsv : reservations) {
			reservationsDetails.add(new ReservationDetails(rsv));
		}
		return reservationsDetails;
	}

	// Shop
	public static List<ShopDetails> shopsToShopDetails(List<Shop> shops) {
		List<ShopDetails> shopDetails = new ArrayList<>();
		for (Shop shop : shops) {
			shopDetails.add(new ShopDetails(shop));
		}
		return shopDetails;
	}

	// Control parameters
	public static List<ControlParameterDetails> controlParametersToControlParameterDetails(
			List<ControlParameter> controlParameters) {
		List<ControlParameterDetails> controlParameterDetails = new ArrayList<>();
		for (ControlParameter controlParameter : controlParameters) {
			controlParameterDetails
					.add(new ControlParameterDetails(controlParameter.getName(), controlParameter.getValue()));
		}
		return controlParameterDetails;
	}

	// Entry
	public static List<EntryDetails> entriesToEntryDetails(List<Entry> entries) {
		List<EntryDetails> entryDetails = new ArrayList<>();
		for (Entry entry : entries) {
			entryDetails.add(new EntryDetails(entry));
		}
		return entryDetails;
	}

}
