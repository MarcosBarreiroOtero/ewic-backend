package es.ewic.backend.model.seller;

import org.springframework.stereotype.Repository;

import es.ewic.backend.modelutil.dao.GenericDaoHibernate;
import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;

@Repository("sellerDao")
public class SellerDaoHibernate extends GenericDaoHibernate<Seller, Integer> implements SellerDao {

	@Override
	public Seller findByLoginName(String loginName) throws InstanceNotFoundException {

		if (loginName == null) {
			throw new InstanceNotFoundException("LoginName null", Seller.class.getName());
		}

		Seller seller = getSession()
				.createQuery("SELECT s FROM Seller s WHERE LOWER(s.loginName) = :loginName", Seller.class)
				.setParameter("loginName", loginName.toLowerCase()).setMaxResults(1).uniqueResult();

		if (seller == null) {
			throw new InstanceNotFoundException(loginName, Seller.class.getSimpleName());
		} else {
			return seller;
		}
	}

}
