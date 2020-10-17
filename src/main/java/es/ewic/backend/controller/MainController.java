package es.ewic.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import es.ewic.backend.service.sellerService.SellerService;

@RestController
public class MainController {

	@Autowired
	private SellerService sellerService;

	@GetMapping("/")
	@ResponseBody
	public String index() {
		return "Bienvenidos " + sellerService.getSellers().size();
	}

}
