package es.ewic.backend.service.clientService;

import java.util.List;

import es.ewic.backend.model.client.Client;
import es.ewic.backend.modelutil.exceptions.DuplicateInstanceException;
import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;

public interface ClientService {

	Client getClientByIdGoogleLogin(String idGoogleLogin) throws InstanceNotFoundException;

	List<Client> getClients();

	Client saveOrUpdateClient(Client client) throws DuplicateInstanceException;

	void deleteClient(int idClient) throws InstanceNotFoundException;

}
