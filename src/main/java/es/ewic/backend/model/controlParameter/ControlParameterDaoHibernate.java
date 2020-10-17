package es.ewic.backend.model.controlParameter;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.ewic.backend.modelutil.dao.GenericDaoHibernate;
import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;

@Repository("controlParameterDao")
public class ControlParameterDaoHibernate extends GenericDaoHibernate<ControlParameter, Integer>
		implements ControlParameterDao {

	@Override
	public ControlParameter findByNameAndShop(String name, int idShop) throws InstanceNotFoundException {
		if (name == null) {
			throw new InstanceNotFoundException("Name null", ControlParameter.class.getSimpleName());
		}

		ControlParameter controlParameter = getSession()
				.createQuery("SELECT c FROM ControlParameter c WHERE c.name = :name AND c.shop.idShop = :idShop",
						ControlParameter.class)
				.setParameter("name", name).setParameter("idShop", idShop).setMaxResults(1).uniqueResult();

		if (controlParameter == null) {
			throw new InstanceNotFoundException(name, ControlParameter.class.getSimpleName());
		} else {
			return controlParameter;
		}
	}

	@Override
	public List<ControlParameter> findAllByShopId(int idShop) {
		return getSession()
				.createQuery("SELECT c FROM ControlParameter c WHERE c.shop.idShop = :idShop", ControlParameter.class)
				.setParameter("idShop", idShop).list();
	}

}
