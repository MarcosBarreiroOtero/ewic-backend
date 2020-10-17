package es.ewic.backend.service.shopService;

import es.ewic.backend.model.shop.Shop;
import es.ewic.backend.modelutil.exceptions.DuplicateInstanceException;
import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;
import es.ewic.backend.modelutil.exceptions.NoAuthorizedException;

public interface ShopService {

	Shop getShopById(int idShop) throws InstanceNotFoundException;

	Shop saveOrUpdateShop(Shop shop) throws DuplicateInstanceException, NoAuthorizedException;

}
