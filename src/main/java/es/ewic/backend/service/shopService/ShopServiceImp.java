package es.ewic.backend.service.shopService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.ewic.backend.model.shop.Shop;
import es.ewic.backend.model.shop.ShopDao;
import es.ewic.backend.modelutil.exceptions.DuplicateInstanceException;
import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;

@Service("shopService")
@Transactional
public class ShopServiceImp implements ShopService {

	@Autowired
	private ShopDao shopDao;

	@Override
	@Transactional(readOnly = true)
	public Shop getShopById(int idShop) throws InstanceNotFoundException {
		return shopDao.find(idShop);
	}

	@Override
	public Shop saveOrUpdateShop(Shop shop) throws DuplicateInstanceException {

		try {
			Shop s = shopDao.find(shop.getIdShop());
			checkShopDuplicate(shop);

			s.setName(shop.getName());
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

}
