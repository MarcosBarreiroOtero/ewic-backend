package es.ewic.backend.service.reservationService;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.ewic.backend.model.reservation.Reservation;
import es.ewic.backend.model.reservation.Reservation.ReservationState;
import es.ewic.backend.model.reservation.ReservationDao;
import es.ewic.backend.modelutil.DateUtils;
import es.ewic.backend.modelutil.NoAuthorizedOperationsNames;
import es.ewic.backend.modelutil.exceptions.DuplicateInstanceException;
import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;
import es.ewic.backend.modelutil.exceptions.NoAuthorizedException;

@Service("reservationService")
@Transactional
public class ReservationServiceImp implements ReservationService {

	@Autowired
	private ReservationDao reservationDao;

	@Override
	@Transactional(readOnly = true)
	public Reservation getReservationById(int idReservation) throws InstanceNotFoundException {
		return reservationDao.find(idReservation);
	}

	@Override
	public Reservation saveOrUpdateReservation(Reservation reservation)
			throws DuplicateInstanceException, NoAuthorizedException {

		Calendar now = Calendar.getInstance();
		try {
			Reservation rsv = reservationDao.find(reservation.getIdReservation());

			if (reservation.getState() != ReservationState.ACTIVE) {
				throw new NoAuthorizedException(NoAuthorizedOperationsNames.RESERVATION_NOT_MUTABLE,
						Reservation.class.getSimpleName());
			}

			if (reservation.getShop().getIdShop() != rsv.getShop().getIdShop()
					|| reservation.getClient().getIdClient() != rsv.getClient().getIdClient()) {
				throw new NoAuthorizedException(NoAuthorizedOperationsNames.CHANGE_CLIENT_OR_SHOP,
						Reservation.class.getSimpleName());
			}

			if (!DateUtils.compareDaysByGet(rsv.getDate(), reservation.getDate())) {
				checkReservationDuplicate(reservation);
			}
			if (now.after(reservation.getDate())) {
				throw new NoAuthorizedException(NoAuthorizedOperationsNames.MOVE_RESERVATION_TO_PAST,
						Reservation.class.getSimpleName());
			}

			rsv.setDate(reservation.getDate());
			rsv.setRemarks(reservation.getRemarks());
			return rsv;

		} catch (InstanceNotFoundException e) {
			checkReservationDuplicate(reservation);

			if (now.after(reservation.getDate())) {
				throw new NoAuthorizedException(NoAuthorizedOperationsNames.MOVE_RESERVATION_TO_PAST,
						Reservation.class.getSimpleName());
			}
			reservationDao.save(reservation);
			return reservation;
		}
	}

	private void checkReservationDuplicate(Reservation rsv) throws DuplicateInstanceException {
		try {
			reservationDao.findByDateShopAndClient(rsv.getDate(), rsv.getShop().getIdShop(),
					rsv.getClient().getIdClient());
			throw new DuplicateInstanceException(DateUtils.formatDateLong(rsv.getDate()),
					Reservation.class.getSimpleName());
		} catch (InstanceNotFoundException e) {
			// no duplicate
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Reservation> getReservationsByIdClient(int idClient) {
		return reservationDao.findAllByClientId(idClient);
	}

	@Override
	public void cancelReservation(int idReservation) throws InstanceNotFoundException {
		Reservation rsv = getReservationById(idReservation);
		rsv.setState(ReservationState.CANCELLED);
		reservationDao.save(rsv);
		return;
	}

}
