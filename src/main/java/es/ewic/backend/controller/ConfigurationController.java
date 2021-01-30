package es.ewic.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import es.ewic.backend.model.shop.Shop;
import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;
import es.ewic.backend.service.configurationService.ConfigurationService;
import es.ewic.backend.service.configurationService.ControlParameterDetails;
import es.ewic.backend.service.shopService.ShopService;

@RestController
@RequestMapping("/configuration/{id}")
public class ConfigurationController {

	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private ShopService shopService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ControlParameterDetails> getShopConfiguration(@PathVariable("id") int idShop) {

		try {
			Shop shop = shopService.getShopById(idShop);
			return TransformationUtils.controlParametersToControlParameterDetails(
					configurationService.getAllControlParametersOfShop(shop.getIdShop()));
		} catch (InstanceNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@GetMapping(path = "reservation", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ControlParameterDetails> getReservationParameters(@PathVariable("id") int idShop) {
		try {
			Shop shop = shopService.getShopById(idShop);
			return TransformationUtils.controlParametersToControlParameterDetails(
					configurationService.getReservationParametersOfShop(shop.getIdShop()));
		} catch (InstanceNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ControlParameterDetails updateShopParameter(@PathVariable("id") int idShop,
			@RequestBody ControlParameterDetails controlParameterDetails) {

		try {
			Shop shop = shopService.getShopById(idShop);
			configurationService.setControlParameter(shop, controlParameterDetails.getName(),
					controlParameterDetails.getValue());
			return controlParameterDetails;
		} catch (InstanceNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@PostMapping(path = "batch", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ControlParameterDetails> updateShopParameter(@PathVariable("id") int idShop,
			@RequestBody List<ControlParameterDetails> controlParameterDetailsList) {

		try {
			Shop shop = shopService.getShopById(idShop);
			for (ControlParameterDetails controlParameterDetails : controlParameterDetailsList) {
				configurationService.setControlParameter(shop, controlParameterDetails.getName(),
						controlParameterDetails.getValue());
			}
			return controlParameterDetailsList;
		} catch (InstanceNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

}
