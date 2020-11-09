package es.ewic.backend.service.reservationService;

import java.util.Calendar;
import java.util.List;

import es.ewic.backend.model.reservation.Reservation;
import es.ewic.backend.modelutil.exceptions.DuplicateInstanceException;
import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;
import es.ewic.backend.modelutil.exceptions.NoAuthorizedException;

public interface ReservationService {

	Reservation getReservationById(int idReservation) throws InstanceNotFoundException;

	Reservation saveOrUpdateReservation(Reservation reservation)
			throws DuplicateInstanceException, NoAuthorizedException;

	List<Reservation> getReservationsByIdClient(int idClient);

	void cancelReservation(int idReservation) throws InstanceNotFoundException, NoAuthorizedException;

	void reservationScheduledTask();

	Reservation getCloseReservationByClient(Calendar now, int idClient);

}
