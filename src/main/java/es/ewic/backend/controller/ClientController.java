package es.ewic.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import es.ewic.backend.model.client.Client;
import es.ewic.backend.modelutil.exceptions.DuplicateInstanceException;
import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;
import es.ewic.backend.service.clientService.ClientService;

@RestController
@RequestMapping("/client")
public class ClientController {

	@Autowired
	private ClientService clientService;

	@GetMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Client loginClient(@RequestBody Client client) {
		try {
			Client c = clientService.getClientByIdGoogleLogin(client.getIdGoogleLogin());
			return c;
		} catch (InstanceNotFoundException e) {
			try {
				return clientService.saveOrUpdateClient(client);
			} catch (DuplicateInstanceException e1) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
			}
		}
	}

	@PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Client updateClient(@PathVariable("id") String idGoogleLogin, @RequestBody Client client) {

		try {
			Client c = clientService.getClientByIdGoogleLogin(idGoogleLogin);
			client.setIdClient(c.getIdClient());
			clientService.saveOrUpdateClient(client);
			return client;
		} catch (InstanceNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (DuplicateInstanceException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@DeleteMapping(path = "/{id}/{idGoogle}")
	public void deleteClient(@PathVariable("id") int idClient, @PathVariable("idGoogle") String idGoogleLogin) {
		try {
			Client c = clientService.getClientByIdGoogleLogin(idGoogleLogin);
			if (c.getIdClient() == idClient) {
				clientService.deleteClient(idClient);
			} else {
				throw new InstanceNotFoundException(idGoogleLogin, Client.class.getSimpleName());
			}
		} catch (InstanceNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

}
