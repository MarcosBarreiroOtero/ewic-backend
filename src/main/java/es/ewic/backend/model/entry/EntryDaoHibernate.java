package es.ewic.backend.model.entry;

import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Repository;

import es.ewic.backend.modelutil.dao.GenericDaoHibernate;
import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;

@Repository("entryDao")
public class EntryDaoHibernate extends GenericDaoHibernate<Entry, Integer> implements EntryDao {

	@Override
	public Entry findUncompletedEntry(int idClient) throws InstanceNotFoundException {

		Entry entry = getSession()
				.createQuery("SELECT e FROM Entry e where e.client.idClient = :idClient AND e.end is NULL", Entry.class)
				.setParameter("idClient", idClient).setMaxResults(1).uniqueResult();

		if (entry == null) {
			throw new InstanceNotFoundException(idClient, Entry.class.getSimpleName());
		} else {
			return entry;
		}
	}

	@Override
	public List<Entry> findDailyEntriesShop(int idShop, Calendar date) {
		return getSession().createQuery(
				"SELECT e FROM Entry e where e.shop.idShop = :idShop AND DATE(e.start) = DATE(:date) AND e.end IS NOT NULL",
				Entry.class).setParameter("idShop", idShop).setParameter("date", date).list();
	}

}
