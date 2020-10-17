package es.ewic.backend.model.seller;

import es.ewic.backend.modelutil.dao.GenericDao;
import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;

public interface SellerDao extends GenericDao<Seller, Integer> {

	Seller findByLoginName(String loginName) throws InstanceNotFoundException;

}
