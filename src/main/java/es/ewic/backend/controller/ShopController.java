package es.ewic.backend.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import es.ewic.backend.model.client.Client;
import es.ewic.backend.model.entry.Entry;
import es.ewic.backend.model.seller.Seller;
import es.ewic.backend.model.shop.Shop;
import es.ewic.backend.model.shop.Shop.ShopType;
import es.ewic.backend.modelutil.DateUtils;
import es.ewic.backend.modelutil.exceptions.DuplicateInstanceException;
import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;
import es.ewic.backend.modelutil.exceptions.NoAuthorizedException;
import es.ewic.backend.service.clientService.ClientService;
import es.ewic.backend.service.sellerService.SellerService;
import es.ewic.backend.service.shopService.EntryDetails;
import es.ewic.backend.service.shopService.ShopDetails;
import es.ewic.backend.service.shopService.ShopService;

@RestController
@RequestMapping("/shop")
public class ShopController {

	@Autowired
	private ShopService shopService;
	@Autowired
	private SellerService sellerService;
	@Autowired
	private ClientService clientService;

	private List<ShopDetails> shopsToShopDetails(List<Shop> shops) {
		List<ShopDetails> shopDetails = new ArrayList<ShopDetails>();
		for (Shop shop : shops) {
			shopDetails.add(new ShopDetails(shop));
		}
		return shopDetails;
	}

	private List<EntryDetails> entriesToEntryDetails(List<Entry> entries) {
		List<EntryDetails> entryDetails = new ArrayList<EntryDetails>();
		for (Entry entry : entries) {
			entryDetails.add(new EntryDetails(entry));
		}
		return entryDetails;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ShopDetails registerShop(@RequestBody ShopDetails shopDetails) {

		try {
			Seller seller = sellerService.getSellerById(shopDetails.getIdSeller());
			Shop shop = new Shop(shopDetails, seller);
			shopService.saveOrUpdateShop(shop);
			return new ShopDetails(shop);
		} catch (InstanceNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (DuplicateInstanceException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (NoAuthorizedException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}

	@PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ShopDetails updateShop(@PathVariable("id") int idShop, @RequestBody ShopDetails shopDetails) {

		try {
			Seller seller = sellerService.getSellerById(shopDetails.getIdSeller());
			Shop shop = shopService.getShopById(idShop);

			if (shop.getSeller().getIdSeller() == seller.getIdSeller()) {
				Shop updateShop = new Shop(shopDetails, seller);
				updateShop.setIdShop(shop.getIdShop());
				shopService.saveOrUpdateShop(updateShop);
				return new ShopDetails(updateShop);
			} else {
				throw new InstanceNotFoundException(idShop, ShopController.class.getSimpleName());
			}
		} catch (InstanceNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (DuplicateInstanceException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (NoAuthorizedException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}

	}

	@GetMapping(path = "/types", produces = MediaType.APPLICATION_JSON_VALUE)
	public ShopType[] getShopTypes() {
		return ShopType.values();
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ShopDetails> getShopsByFilters(@RequestParam(required = false) Float latitude,
			@RequestParam(required = false) Float longitude, @RequestParam(required = false) String name,
			@RequestParam(required = false) ShopType shopType) {

		List<Shop> shops = shopService.getShopsByFilters(name, shopType, latitude, longitude);
		return shopsToShopDetails(shops);

	}

	// ENTRIES
	@PostMapping(path = "/{id}/entry")
	public int registerEntry(@PathVariable("id") int idShop, @RequestParam(required = false) String idGoogleLogin) {

		try {
			Shop shop = shopService.getShopById(idShop);
			Client client = null;
			if (idGoogleLogin != null) {
				client = clientService.getClientByIdGoogleLogin(idGoogleLogin);
			}

			Calendar now = Calendar.getInstance();
			Entry e = new Entry(now, shop, client);
			shopService.registerEntry(e);

			return e.getIdEntry();
		} catch (InstanceNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (DuplicateInstanceException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (NoAuthorizedException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}

	@PutMapping(path = "/{id}/exit")
	public void registerExit(@PathVariable("id") int idShop,
			@RequestParam(required = true, name = "entryNumber") int idEntry) {

		try {
			shopService.registerExit(idEntry);
		} catch (InstanceNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (NoAuthorizedException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}

	@GetMapping(path = "/{id}/dailyEntries")
	public List<EntryDetails> getDailyEntries(@PathVariable("id") int idShop,
			@RequestParam(required = true, name = "date") String date) {

		Calendar day = DateUtils.parseDateDate(date);
		if (day == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date");
		}

		List<Entry> entries = shopService.getDailyEntriesShop(idShop, day);
		return entriesToEntryDetails(entries);

	}
}
