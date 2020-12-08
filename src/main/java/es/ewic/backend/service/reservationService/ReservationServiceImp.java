package es.ewic.backend.service.reservationService;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.ewic.backend.model.reservation.Reservation;
import es.ewic.backend.model.reservation.Reservation.ReservationState;
import es.ewic.backend.model.reservation.ReservationDao;
import es.ewic.backend.model.shop.Shop;
import es.ewic.backend.model.shop.ShopDao;
import es.ewic.backend.modelutil.ConfigurationGlobalNames;
import es.ewic.backend.modelutil.DateUtils;
import es.ewic.backend.modelutil.NoAuthorizedOperationsNames;
import es.ewic.backend.modelutil.exceptions.DuplicateInstanceException;
import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;
import es.ewic.backend.modelutil.exceptions.NoAuthorizedException;
import es.ewic.backend.service.configurationService.ConfigurationService;

@Service("reservationService")
@Transactional
public class ReservationServiceImp implements ReservationService {

	@Autowired
	private ReservationDao reservationDao;
	@Autowired
	private ShopDao shopDao;
	@Autowired
	private ConfigurationService configurationService;

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
				checkShopFull(reservation.getShop(), reservation.getDate());
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
			checkShopFull(reservation.getShop(), reservation.getDate());
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

	private void checkShopFull(Shop shop, Calendar date) throws NoAuthorizedException {
		List<Reservation> reservations = reservationDao.getReservationsByShopAndDate(date, shop.getIdShop());
		System.out.println("Número de reservas" + reservations.size());
		if (reservations.size() >= shop.getMaxCapacity()) {
			throw new NoAuthorizedException(NoAuthorizedOperationsNames.RESERVATION_WHEN_SHOP_FULL,
					Reservation.class.getSimpleName());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Reservation> getReservationsByIdClient(int idClient) {
		return reservationDao.findAllByClientId(idClient);
	}

	@Override
	public void cancelReservation(int idReservation) throws InstanceNotFoundException, NoAuthorizedException {
		Reservation rsv = getReservationById(idReservation);

		if (rsv.getState() == ReservationState.ACTIVE || rsv.getState() == ReservationState.WAITING) {

			if (rsv.getState() == ReservationState.WAITING) {
				Shop shop = rsv.getShop();
				shop.setActualCapacity(shop.getActualCapacity() - 1);
				shopDao.save(shop);
			}

			rsv.setState(ReservationState.CANCELLED);
			reservationDao.save(rsv);

		} else {
			throw new NoAuthorizedException(NoAuthorizedOperationsNames.RESERVATION_NOT_MUTABLE,
					Reservation.class.getSimpleName());
		}
	}

	@Override
	public void reservationScheduledTask() {
		Calendar now = Calendar.getInstance();
		// Change active reservations to waiting
		List<Reservation> futureActiveReservations = reservationDao.getFutureActiveReservations(now);
		Map<Shop, List<Reservation>> reservationGrouped = futureActiveReservations.stream()
				.collect(Collectors.groupingBy(Reservation::getShop));
		for (Map.Entry<Shop, List<Reservation>> entry : reservationGrouped.entrySet()) {
			Shop shop = entry.getKey();
			List<Reservation> reservations = entry.getValue();
			int increaseActualCapacity = 0;
			for (Reservation reservation : reservations) {
				if (DateUtils.getMinutesDifference(now, reservation.getDate()) <= 1) {
					System.out.println("Reservation " + reservation.getIdReservation() + " waiting");
					reservation.setState(ReservationState.WAITING);
					reservationDao.save(reservation);
					increaseActualCapacity++;
				}
			}
			shop.setActualCapacity(shop.getActualCapacity() + increaseActualCapacity);
			shopDao.save(shop);
		}
		// Change waiting to not appear

		List<Reservation> waitingReservations = reservationDao.getWaitingReservations();
		Map<Shop, List<Reservation>> waitingReservationsGrouped = waitingReservations.stream()
				.collect(Collectors.groupingBy(Reservation::getShop));
		for (Map.Entry<Shop, List<Reservation>> entry : waitingReservationsGrouped.entrySet()) {
			Shop shop = entry.getKey();
			List<Reservation> reservations = entry.getValue();

			int minutesWaiting = 10;
			String shopWaitingMinutes = configurationService.readControlParameterByNameAndShop(
					ConfigurationGlobalNames.RESERVATION_WAIT_MINUTES, shop.getIdShop());
			if (!shopWaitingMinutes.equals("")) {
				minutesWaiting = Integer.parseInt(shopWaitingMinutes);
			}
			int reduceActualCapacity = 0;
			for (Reservation reservation : reservations) {
				Calendar rsvDate = (Calendar) reservation.getDate().clone();
				rsvDate.add(Calendar.MINUTE, minutesWaiting);
				if (rsvDate.before(now)) {
					System.out.println("Reservation " + reservation.getIdReservation() + " not appear");
					reservation.setState(ReservationState.NOT_APPEAR);
					reservationDao.save(reservation);
					reduceActualCapacity++;
				}
			}
			shop.setActualCapacity(shop.getActualCapacity() - reduceActualCapacity);
		}
	}

	@Override
	public Reservation getCloseReservationByClient(Calendar now, int idClient) {

		List<Reservation> reservations = reservationDao.getActiveAndWaitingReservationsByClientAndDay(now, idClient);
		System.out.println(reservations.size());
		Reservation waitingRsv = reservations.stream().filter(r -> r.getState() == ReservationState.WAITING).findFirst()
				.orElse(null);
		if (waitingRsv != null) {
			System.out.println("Reserva esperando");
			return waitingRsv;
		} else {
			for (Reservation reservation : reservations) {
				System.out.println(
						"Minutos de diferencia: " + DateUtils.getMinutesDifference(now, reservation.getDate()));

				if (DateUtils.getMinutesDifference(now, reservation.getDate()) <= 5) {
					return reservation;
				}
			}
		}
		return null;
	}

}
