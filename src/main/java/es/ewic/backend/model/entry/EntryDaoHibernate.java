package es.ewic.backend.model.entry;

import java.util.Calendar;
import java.util.List;

import org.hibernate.query.Query;
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
		return getSession()
				.createQuery("SELECT e FROM Entry e where e.shop.idShop = :idShop AND DATE(e.start) = DATE(:date)",
						Entry.class)
				.setParameter("idShop", idShop).setParameter("date", date).list();
	}

	@Override
	public List<Entry> findEntriesClientBetweenDates(int idClient, Calendar dateFrom, Calendar dateTo) {
		String where = "where e.client.idClient = :idClient";
		if (dateFrom != null) {
			where += " AND DATE(e.start) >= DATE(:dateFrom) ";
		}
		if (dateTo != null) {
			where += " AND DATE(e.start) <= DATE(:dateTo) ";
		}
		Query<Entry> query = getSession().createQuery("SELECT e FROM Entry e " + where, Entry.class)
				.setParameter("idClient", idClient);

		if (dateFrom != null) {
			query.setParameter("dateFrom", dateFrom);
		}
		if (dateTo != null) {
			query.setParameter("dateTo", dateTo);
		}
		return query.list();
	}

}
