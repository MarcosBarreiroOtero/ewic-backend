package es.ewic.backend.model.shop;

import es.ewic.backend.model.shop.Shop.ShopType;
import es.ewic.backend.modelutil.dao.GenericDao;

public interface ShopDao extends GenericDao<Shop, Integer> {

	boolean checkShopDuplicate(int idShop, String name, String location, ShopType type);

}
