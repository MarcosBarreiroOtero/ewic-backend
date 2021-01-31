package es.ewic.backend.service.shopService;

import java.util.Calendar;
import java.util.List;

import es.ewic.backend.model.entry.Entry;
import es.ewic.backend.model.reservation.Reservation;
import es.ewic.backend.model.shop.Shop;
import es.ewic.backend.model.shop.Shop.ShopType;
import es.ewic.backend.modelutil.exceptions.DuplicateInstanceException;
import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;
import es.ewic.backend.modelutil.exceptions.MaxCapacityException;
import es.ewic.backend.modelutil.exceptions.NoAuthorizedException;

public interface ShopService {

	Shop getShopById(int idShop) throws InstanceNotFoundException;

	Shop saveOrUpdateShop(Shop shop) throws DuplicateInstanceException, NoAuthorizedException;

	List<Shop> getShopsByFilters(String name, ShopType type, Float latitude, Float longitude);

	List<Shop> getShopsByIdSeller(int idSeller);

	void startCapacityControl(int idShop) throws InstanceNotFoundException;

	void endCapacityControl(int idShop) throws InstanceNotFoundException;

	// Entries
	Entry getEntryById(int idEntry) throws InstanceNotFoundException;

	Entry registerEntry(Entry entry) throws DuplicateInstanceException, NoAuthorizedException, MaxCapacityException;

	Entry registerEntryWithReservation(Entry entry, Reservation reservation)
			throws DuplicateInstanceException, NoAuthorizedException;

	Entry registerExit(int idEntry) throws InstanceNotFoundException, NoAuthorizedException;

	List<Entry> getDailyEntriesShop(int idShop, Calendar date);

	List<Entry> getEntriesClientBetweenDates(int idClient, Calendar dateFrom, Calendar dateTo);

	List<Entry> getDailyManualEntriesShop(int idShop, Calendar date);

}
