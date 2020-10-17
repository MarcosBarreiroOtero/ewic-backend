package es.ewic.backend.modelutil.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.ReturningWork;
import org.springframework.beans.factory.annotation.Autowired;

import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;

public class GenericDaoHibernate<E, PK extends Serializable> implements GenericDao<E, PK> {

	private SessionFactory sessionFactory;

	private Class<E> entityClass;

	@SuppressWarnings("unchecked")
	public GenericDaoHibernate() {
		this.entityClass = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public void save(E entity) {
		getSession().saveOrUpdate(entity);
	}

	public String getURLDatabase() {
		return sessionFactory.getCurrentSession().doReturningWork(new ReturningWork<String>() {
			@Override
			public String execute(Connection connection) throws SQLException {
				return connection.getMetaData().getURL();
			}
		});

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> getAll() {
		return getSession().createQuery("SELECT s FROM " + entityClass.getName() + " s").list();
	}

	@Override
	public E find(PK id) throws InstanceNotFoundException {
		E entity = (E) getSession().get(entityClass, id);
		if (entity == null) {
			throw new InstanceNotFoundException(id, entityClass.getName());
		}
		return entity;
	}

	@Override
	public void remove(PK id) throws InstanceNotFoundException {
		getSession().delete(find(id));
	}

}
