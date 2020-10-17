package es.ewic.backend.model.client;

import org.springframework.stereotype.Repository;

import es.ewic.backend.modelutil.dao.GenericDaoHibernate;
import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;

@Repository("clientDao")
public class ClientDaoHibernate extends GenericDaoHibernate<Client, Integer> implements ClientDao {

	@Override
	public Client findClientByIdGoogleLogin(String idGoogleLogin) throws InstanceNotFoundException {
		if (idGoogleLogin == null) {
			throw new InstanceNotFoundException("Null idGoogleLogin", Client.class.getName());
		}

		Client client = getSession()
				.createQuery("SELECT c FROM Client c WHERE c.idGoogleLogin = : idGoogleLogin", Client.class)
				.setParameter("idGoogleLogin", idGoogleLogin).setMaxResults(1).uniqueResult();

		if (client == null) {
			throw new InstanceNotFoundException(idGoogleLogin, Client.class.getSimpleName());
		} else {
			return client;
		}
	}

}
