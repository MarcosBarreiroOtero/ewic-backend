package es.ewic.backend.modelutil.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;

import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;

public interface GenericDao<E, PK extends Serializable> {

	void save(E entity);

	E find(PK id) throws InstanceNotFoundException;

	void remove(PK id) throws InstanceNotFoundException;

	List<E> getAll();

	public Session getSession();

}
