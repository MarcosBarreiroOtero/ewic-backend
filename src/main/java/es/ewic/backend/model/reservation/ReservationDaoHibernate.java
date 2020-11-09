package es.ewic.backend.model.reservation;

import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Repository;

import es.ewic.backend.model.reservation.Reservation.ReservationState;
import es.ewic.backend.modelutil.DateUtils;
import es.ewic.backend.modelutil.dao.GenericDaoHibernate;
import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;

@Repository("reservationDao")
public class ReservationDaoHibernate extends GenericDaoHibernate<Reservation, Integer> implements ReservationDao {

	@Override
	public List<Reservation> findAllByClientId(int idClient) {
		return getSession()
				.createQuery("SELECT r FROM Reservation r WHERE r.client.idClient = :idClient", Reservation.class)
				.setParameter("idClient", idClient).list();
	}

	@Override
	public List<Reservation> findAllByShopId(int idShop) {
		return getSession().createQuery("SELECT r FROM Reservation r WHERE r.shop.idShop = :idShop", Reservation.class)
				.setParameter("idShop", idShop).list();
	}

	@Override
	public Reservation findByDateShopAndClient(Calendar date, int idShop, int idClient)
			throws InstanceNotFoundException {
		if (date == null) {
			throw new InstanceNotFoundException("Date null", Reservation.class.getSimpleName());
		}

		Reservation rsv = getSession().createQuery(
				"SELECT r FROM Reservation r WHERE r.shop.idShop = :idShop AND r.client.idClient = :idClient AND TIMESTAMP(r.date) = TIMESTAMP(:date)",
				Reservation.class).setParameter("idShop", idShop).setParameter("idClient", idClient)
				.setParameter("date", date).setMaxResults(1).uniqueResult();

		if (rsv == null) {
			throw new InstanceNotFoundException(DateUtils.formatDateLong(date), Reservation.class.getSimpleName());
		} else {
			return rsv;
		}
	}

	@Override
	public List<Reservation> getFutureActiveReservations(Calendar date) {
		return getSession().createQuery(
				"SELECT r FROM Reservation r WHERE TIMESTAMP(r.date) >= TIMESTAMP(:date) AND r.state = :activeStates",
				Reservation.class).setParameter("date", date).setParameter("activeStates", ReservationState.ACTIVE)
				.list();
	}

	@Override
	public List<Reservation> getWaitingReservations() {
		return getSession().createQuery("SELECT r FROM Reservation r WHERE r.state = :waiting", Reservation.class)
				.setParameter("waiting", ReservationState.WAITING).list();
	}

	@Override
	public List<Reservation> getActiveAndWaitingReservationsByClientAndDay(Calendar date, int idClient) {
		return getSession().createQuery(
				"SELECT r FROM Reservation r WHERE r.client.idClient = :idClient AND DATE(r.date) = DATE(:date) AND (r.state = :active OR r.state = :waiting)",
				Reservation.class).setParameter("idClient", idClient).setParameter("date", date)
				.setParameter("active", ReservationState.ACTIVE).setParameter("waiting", ReservationState.WAITING)
				.list();
	}

}
