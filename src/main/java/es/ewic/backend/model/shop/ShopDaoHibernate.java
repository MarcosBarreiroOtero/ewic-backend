package es.ewic.backend.model.shop;

import org.springframework.stereotype.Repository;

import es.ewic.backend.model.shop.Shop.ShopType;
import es.ewic.backend.modelutil.dao.GenericDaoHibernate;

@Repository("shopDao")
public class ShopDaoHibernate extends GenericDaoHibernate<Shop, Integer> implements ShopDao {

	@Override
	public boolean checkShopDuplicate(int idShop, String name, String location, ShopType type) {

		Shop shop = getSession().createQuery(
				"SELECT s FROM Shop s WHERE s.name = :name AND s.location = :location AND s.type = :type AND s.idShop != :idShop",
				Shop.class).setParameter("name", name).setParameter("location", location).setParameter("type", type)
				.setParameter("idShop", idShop).setMaxResults(1).uniqueResult();

		return shop != null;
	}

}
