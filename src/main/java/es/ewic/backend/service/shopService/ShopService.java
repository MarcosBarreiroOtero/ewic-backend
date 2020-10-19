package es.ewic.backend.service.shopService;

import java.util.List;

import es.ewic.backend.model.entry.Entry;
import es.ewic.backend.model.shop.Shop;
import es.ewic.backend.model.shop.Shop.ShopType;
import es.ewic.backend.modelutil.exceptions.DuplicateInstanceException;
import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;
import es.ewic.backend.modelutil.exceptions.NoAuthorizedException;

public interface ShopService {

	Shop getShopById(int idShop) throws InstanceNotFoundException;

	Shop saveOrUpdateShop(Shop shop) throws DuplicateInstanceException, NoAuthorizedException;

	List<Shop> getShopsByFilters(String name, ShopType type, Float latitude, Float longitude);

	// Entries
	Entry registerEntry(Entry entry) throws DuplicateInstanceException;

	Entry registerExit(int idEntry) throws InstanceNotFoundException, NoAuthorizedException;

}
