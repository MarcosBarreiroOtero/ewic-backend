package es.ewic.backend.service.clientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.ewic.backend.model.client.Client;
import es.ewic.backend.model.client.ClientDao;
import es.ewic.backend.modelutil.exceptions.DuplicateInstanceException;
import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;

@Service("clientService")
@Transactional
public class ClientServiceImp implements ClientService {

	@Autowired
	private ClientDao clientDao;

	@Override
	@Transactional(readOnly = true)
	public Client getClientByIdGoogleLogin(String idGoogleLogin) throws InstanceNotFoundException {
		return clientDao.findClientByIdGoogleLogin(idGoogleLogin);
	}

	@Override
	public Client saveOrUpdateClient(Client client) throws DuplicateInstanceException {

		try {
			Client c = clientDao.find(client.getIdClient());
			if (!c.getIdGoogleLogin().equals(client.getIdGoogleLogin())) {
				checkClientDuplicate(client);
			}

			c.setFirstName(client.getFirstName());
			c.setLastName(client.getLastName());
			c.setEmail(client.getEmail());
			return c;
		} catch (InstanceNotFoundException e) {
			checkClientDuplicate(client);
			clientDao.save(client);
			return client;
		}
	}

	private void checkClientDuplicate(Client client) throws DuplicateInstanceException {

		try {
			clientDao.findClientByIdGoogleLogin(client.getIdGoogleLogin());
			throw new DuplicateInstanceException(client.getIdGoogleLogin(), Client.class.getSimpleName());
		} catch (InstanceNotFoundException e) {
			// no duplicate
		}
	}

	@Override
	public void deleteClient(int idClient) throws InstanceNotFoundException {
		clientDao.remove(idClient);
	}

}
