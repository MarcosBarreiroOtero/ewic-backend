package es.ewic.backend.service.shopService;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.ewic.backend.model.entry.Entry;
import es.ewic.backend.model.entry.EntryDao;
import es.ewic.backend.model.shop.Shop;
import es.ewic.backend.model.shop.Shop.ShopType;
import es.ewic.backend.model.shop.ShopDao;
import es.ewic.backend.modelutil.DateUtils;
import es.ewic.backend.modelutil.NoAuthorizedOperationsNames;
import es.ewic.backend.modelutil.exceptions.DuplicateInstanceException;
import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;
import es.ewic.backend.modelutil.exceptions.NoAuthorizedException;

@Service("shopService")
@Transactional
public class ShopServiceImp implements ShopService {

	@Autowired
	private ShopDao shopDao;
	@Autowired
	private EntryDao entryDao;

	@Override
	@Transactional(readOnly = true)
	public Shop getShopById(int idShop) throws InstanceNotFoundException {
		return shopDao.find(idShop);
	}

	@Override
	public Shop saveOrUpdateShop(Shop shop) throws DuplicateInstanceException, NoAuthorizedException {

		if (shop.getMaxCapacity() <= 0) {
			throw new NoAuthorizedException(NoAuthorizedOperationsNames.MAX_CAPACITY_INVALID,
					Shop.class.getSimpleName());
		}

		try {
			Shop s = shopDao.find(shop.getIdShop());
			checkShopDuplicate(shop);

			s.setName(shop.getName());
			s.setLatitude(shop.getLatitude());
			s.setLongitude(shop.getLongitude());
			s.setMaxCapacity(shop.getMaxCapacity());
			s.setLocation(shop.getLocation());
			s.setType(shop.getType());

			return s;

		} catch (InstanceNotFoundException e) {
			checkShopDuplicate(shop);
			shopDao.save(shop);
			return null;
		}
	}

	private void checkShopDuplicate(Shop shop) throws DuplicateInstanceException {

		if (shopDao.checkShopDuplicate(shop.getIdShop(), shop.getName(), shop.getLocation(), shop.getType())) {
			throw new DuplicateInstanceException(shop.getName(), Shop.class.getSimpleName());
		}
	}

	@Override
	public List<Shop> getShopsByFilters(String name, ShopType type, Float latitude, Float longitude) {

		List<Shop> shops = shopDao.getShopsByFilters(name, type);

		// Order shops by distance in meters.
		shops.sort(new Comparator<Shop>() {
			@Override
			public int compare(Shop o1, Shop o2) {
				if (distFrom(latitude, longitude, o1.getLatitude(), o1.getLongitude()) > distFrom(latitude, longitude,
						o2.getLatitude(), o2.getLongitude())) {
					return 1;
				} else {
					return -1;
				}
			}
		});

		return shops;
	}

	private static float distFrom(float lat1, float lng1, float lat2, float lng2) {
		double earthRadius = 6371000; // meters
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		float dist = (float) (earthRadius * c);

		return dist;
	}

	@Override
	public Entry registerEntry(Entry entry) throws DuplicateInstanceException, NoAuthorizedException {
		// check if client already entered
		if (entry.getClient() != null) {
			try {
				entryDao.findUncompletedEntry(entry.getClient().getIdClient());
				throw new NoAuthorizedException(NoAuthorizedOperationsNames.CLIENT_ALREADY_ENTERED,
						Entry.class.getSimpleName());
			} catch (InstanceNotFoundException e) {
				// Entry ok
			}
		}
		try {
			entryDao.find(entry.getIdEntry());
			throw new DuplicateInstanceException(entry.getIdEntry(), Entry.class.getSimpleName());
		} catch (InstanceNotFoundException e) {
			entryDao.save(entry);
			return entry;
		}
	}

	@Override
	public Entry registerExit(int idEntry) throws InstanceNotFoundException, NoAuthorizedException {
		Entry e = entryDao.find(idEntry);

		if (e.getEnd() != null) {
			throw new NoAuthorizedException(NoAuthorizedOperationsNames.EXIT_ALREADY_REGISTERED,
					Entry.class.getSimpleName());
		}

		Calendar now = Calendar.getInstance();
		e.setEnd(now);
		Calendar start = (Calendar) e.getStart().clone();

		long duration = DateUtils.getMinutesDifference(start, now);
		e.setDuration(duration);
		return e;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Entry> getDailyEntriesShop(int idShop, Calendar date) {
		return entryDao.findDailyEntriesShop(idShop, date);
	}

}
