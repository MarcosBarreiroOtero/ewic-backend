package es.ewic.backend.model.reservation;

import java.util.Calendar;
import java.util.List;

import es.ewic.backend.modelutil.dao.GenericDao;
import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;

public interface ReservationDao extends GenericDao<Reservation, Integer> {

	List<Reservation> findAllByClientId(int idClient);

	List<Reservation> findAllByShopId(int idShop);

	Reservation findByDateClient(Calendar date, int idClient) throws InstanceNotFoundException;

	List<Reservation> getFutureActiveReservations(Calendar date);

	List<Reservation> getWaitingReservations();

	List<Reservation> getActiveAndWaitingReservationsByClientAndDay(Calendar date, int idClient);

	List<Reservation> getReservationsByShopAndDate(Calendar date, int idShop);

}
