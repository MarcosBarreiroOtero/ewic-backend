package es.ewic.backend.model.controlParameter;

import java.util.List;

import es.ewic.backend.modelutil.dao.GenericDao;
import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;

public interface ControlParameterDao extends GenericDao<ControlParameter, Integer> {

	ControlParameter findByNameAndShop(String name, int idShop) throws InstanceNotFoundException;

	List<ControlParameter> findAllByShopId(int idShop);

}
