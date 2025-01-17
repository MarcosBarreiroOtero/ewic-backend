package es.ewic.backend.model.entry;

import java.util.Calendar;
import java.util.List;

import es.ewic.backend.modelutil.dao.GenericDao;
import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;

public interface EntryDao extends GenericDao<Entry, Integer> {

	Entry findUncompletedEntry(int idClient) throws InstanceNotFoundException;

	List<Entry> findUncompletedEntriesByShop(int idShop) throws InstanceNotFoundException;

	List<Entry> findDailyEntriesShop(int idShop, Calendar date);

	List<Entry> findEntriesClientBetweenDates(int idClient, Calendar dateFrom, Calendar dateTo);

	List<Entry> findDailyManualEntriesByShop(int idShop, Calendar date);

}
