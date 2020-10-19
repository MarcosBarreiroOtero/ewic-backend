package es.ewic.backend.model.entry;

import org.springframework.stereotype.Repository;

import es.ewic.backend.modelutil.dao.GenericDaoHibernate;

@Repository("entryDao")
public class EntryDaoHibernate extends GenericDaoHibernate<Entry, Integer> implements EntryDao {

}
