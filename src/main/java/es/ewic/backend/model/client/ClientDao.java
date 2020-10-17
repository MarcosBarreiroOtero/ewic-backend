package es.ewic.backend.model.client;

import es.ewic.backend.modelutil.dao.GenericDao;
import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;

public interface ClientDao extends GenericDao<Client, Integer> {

	Client findClientByIdGoogleLogin(String idGoogleLogin) throws InstanceNotFoundException;

}
