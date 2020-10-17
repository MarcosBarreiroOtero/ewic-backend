package es.ewic.backend.service.configurationService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.ewic.backend.model.controlParameter.ControlParameter;
import es.ewic.backend.model.controlParameter.ControlParameterDao;
import es.ewic.backend.model.shop.Shop;
import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;

@Service("controlService")
@Transactional
public class ConfigurationServiceImp implements ConfigurationService {

	@Autowired
	private ControlParameterDao controlParameterDao;

	@Override
	public List<ControlParameter> getAllControlParametersOfShop(int idShop) {
		return controlParameterDao.findAllByShopId(idShop);
	}

	@Override
	public void setControlParameter(Shop shop, String name, String value) {
		ControlParameter controlParameter;
		try {
			controlParameter = controlParameterDao.findByNameAndShop(name, shop.getIdShop());
			controlParameter.setValue(value);

		} catch (InstanceNotFoundException e) {
			controlParameter = new ControlParameter(name, value, shop);
		}
		controlParameterDao.save(controlParameter);

	}

	@Override
	@Transactional(readOnly = true)
	public String readControlParameterByNameAndShop(String name, int idShop) {
		try {
			return controlParameterDao.findByNameAndShop(name, idShop).getValue();
		} catch (InstanceNotFoundException e) {
			return "";
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ControlParameter getControlParameterByNameAndShop(String name, int idShop) throws InstanceNotFoundException {
		return controlParameterDao.findByNameAndShop(name, idShop);
	}

}
