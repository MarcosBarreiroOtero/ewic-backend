package es.ewic.backend.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
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
import es.ewic.backend.service.configurationService.ShopImage;
import es.ewic.backend.service.shopService.ShopService;

@RestController
@RequestMapping("/configuration/{id}")
public class ConfigurationController {

	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private ShopService shopService;

	private final String SHOP_IMAGES_FOLDER = "shopImages/";

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

	@PostMapping(path = "image", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void uploadShopImage(@PathVariable("id") int idShop, @RequestBody ShopImage shopImage) {
		try {
			Shop shop = shopService.getShopById(idShop);

			String base64 = shopImage.getImageBase64();

			File image = new File(SHOP_IMAGES_FOLDER + shop.getIdShop() + ".png");
			image.getParentFile().mkdirs();
			if (image.exists()) {
				image.delete();
			}
			image.createNewFile();
			byte[] decodedBytes = Base64.getDecoder().decode(base64);
			FileOutputStream fos = new FileOutputStream(image);
			fos.write(decodedBytes);
			fos.close();
			throw new ResponseStatusException(HttpStatus.OK);
		} catch (InstanceNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping(path = "image", produces = MediaType.IMAGE_PNG_VALUE)
	public String sendShopImage(@PathVariable("id") int idShop) {
		try {
			Shop shop = shopService.getShopById(idShop);
			String base64 = "";
			File image = new File(SHOP_IMAGES_FOLDER + shop.getIdShop() + ".png");
			image.getParentFile().mkdirs();
			if (image.exists()) {
				byte[] fileContent = Files.readAllBytes(image.toPath());
				base64 = Base64.getEncoder().encodeToString(fileContent);
			}
			return base64;
		} catch (InstanceNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

}
