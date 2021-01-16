package es.ewic.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import es.ewic.backend.model.seller.Seller;
import es.ewic.backend.modelutil.PasswordEncrypter.PasswordEncrypter;
import es.ewic.backend.modelutil.exceptions.DuplicateInstanceException;
import es.ewic.backend.modelutil.exceptions.IncorrectPasswordException;
import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;
import es.ewic.backend.service.sellerService.SellerDetails;
import es.ewic.backend.service.sellerService.SellerService;

@RestController
@RequestMapping("/seller")
public class SellerController {

	@Autowired
	SellerService sellerService;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public SellerDetails registerSeller(@RequestBody Seller seller) {
		try {
			sellerService.saveOrUpdateSeller(seller);
			return new SellerDetails(seller);
		} catch (DuplicateInstanceException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}

	}

	@GetMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
	public SellerDetails loginSeller(@RequestParam String loginName, @RequestParam String password) {
		try {
			return new SellerDetails(sellerService.login(loginName, password));
		} catch (InstanceNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (IncorrectPasswordException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}

	@PutMapping(path = "/password/{id}")
	public void changePasswordSeller(@PathVariable("id") int idSeller, @RequestParam(name = "newPwd") String newPwd,
			@RequestParam(name = "oldPwd") String oldPwd) {
		try {
			sellerService.changePassword(idSeller, newPwd, oldPwd);
		} catch (InstanceNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (IncorrectPasswordException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}

	@PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public SellerDetails updateSeller(@PathVariable("id") int idSeller, @RequestBody SellerDetails sellerDetails) {
		try {
			Seller seller = sellerService.getSellerById(idSeller);
			seller.setLoginName(sellerDetails.getLoginName());
			seller.setFirstName(sellerDetails.getFirstName());
			seller.setLastName(sellerDetails.getLastName());
			seller.setEmail(sellerDetails.getEmail());

			seller = sellerService.saveOrUpdateSeller(seller);
			return new SellerDetails(seller);

		} catch (InstanceNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (DuplicateInstanceException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@DeleteMapping(path = "/{id}")
	public void deleteSeller(@PathVariable("id") int idSeller, @RequestParam(name = "pwd") String password) {

		try {
			Seller seller = sellerService.getSellerById(idSeller);

			if (PasswordEncrypter.isClearPasswordCorrect(password, seller.getPassword())) {
				sellerService.deleteSeller(idSeller);
			} else {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect password");
			}
		} catch (InstanceNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}

	}

}
