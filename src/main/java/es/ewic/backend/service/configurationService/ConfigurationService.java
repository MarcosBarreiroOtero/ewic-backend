package es.ewic.backend.service.configurationService;

import java.util.List;

import es.ewic.backend.model.controlParameter.ControlParameter;
import es.ewic.backend.model.shop.Shop;
import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;

public interface ConfigurationService {

	List<ControlParameter> getAllControlParametersOfShop(int idShop);

	void setControlParameter(Shop shop, String name, String value);

	String readControlParameterByNameAndShop(String name, int idShop);

	ControlParameter getControlParameterByNameAndShop(String name, int idShop) throws InstanceNotFoundException;

}
