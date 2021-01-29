package es.ewic.backend.controller;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import es.ewic.backend.model.client.Client;
import es.ewic.backend.model.reservation.Reservation;
import es.ewic.backend.model.reservation.Reservation.ReservationState;
import es.ewic.backend.model.seller.Seller;
import es.ewic.backend.model.shop.Shop;
import es.ewic.backend.modelutil.DateUtils;
import es.ewic.backend.modelutil.NoAuthorizedOperationsNames;
import es.ewic.backend.modelutil.exceptions.DuplicateInstanceException;
import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;
import es.ewic.backend.modelutil.exceptions.NoAuthorizedException;
import es.ewic.backend.service.clientService.ClientService;
import es.ewic.backend.service.mailService.MailService;
import es.ewic.backend.service.reservationService.ReservationDetails;
import es.ewic.backend.service.reservationService.ReservationService;
import es.ewic.backend.service.sellerService.SellerService;
import es.ewic.backend.service.shopService.ShopService;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

	@Autowired
	private ReservationService reservationService;
	@Autowired
	private ClientService clientService;
	@Autowired
	private SellerService sellerService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private MailService mailService;

	private static final String INVALID_DATE = "Invalid date";

	private Reservation saveNewReservation(ReservationDetails reservationDetails)
			throws InstanceNotFoundException, DuplicateInstanceException, NoAuthorizedException {
		Client client = clientService.getClientByIdGoogleLogin(reservationDetails.getIdGoogleLoginClient());
		Shop shop = shopService.getShopById(reservationDetails.getIdShop());
		Calendar rsvDate = DateUtils.parseDateLong(reservationDetails.getDate());
		if (rsvDate == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_DATE);
		}
		Reservation rsv = new Reservation(rsvDate, reservationDetails.getState(), reservationDetails.getRemarks(),
				reservationDetails.getnClients(), client, shop);
		return reservationService.saveOrUpdateReservation(rsv);
	}

	private Reservation updateReservation(Reservation reservation, ReservationDetails reservationDetails)
			throws InstanceNotFoundException, DuplicateInstanceException, NoAuthorizedException {
		Shop shop = shopService.getShopById(reservationDetails.getIdShop());
		Client client = clientService.getClientByIdGoogleLogin(reservationDetails.getIdGoogleLoginClient());

		if (reservation.getShop().getIdShop() == shop.getIdShop()
				&& reservation.getClient().getIdClient() == client.getIdClient()) {

			Calendar rsvDate = DateUtils.parseDateLong(reservationDetails.getDate());
			if (rsvDate == null) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_DATE);
			}
			reservation.setDate(rsvDate);
			reservation.setRemarks(reservationDetails.getRemarks());
			reservation.setnClients(reservationDetails.getnClients());

			reservationService.saveOrUpdateReservation(reservation);
			return reservation;
		} else {
			throw new InstanceNotFoundException(reservation.getIdReservation(), Reservation.class.getSimpleName());
		}
	}

	// CLIENTS ENDPOINT
	@PostMapping(path = "/client", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ReservationDetails addReservationClient(@RequestBody ReservationDetails reservationDetails) {
		try {
			Reservation rsv = saveNewReservation(reservationDetails);
			mailService.sendSellerNewReservation(rsv);
			return new ReservationDetails(rsv);
		} catch (DuplicateInstanceException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (InstanceNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (NoAuthorizedException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}

	@PutMapping(path = "/client/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ReservationDetails updateReservationClient(@PathVariable("id") int idReservation,
			@RequestBody ReservationDetails reservationDetails) {

		try {
			Reservation reservation = reservationService.getReservationById(idReservation);

			Calendar oldDate = (Calendar) reservation.getDate().clone();
			reservation = updateReservation(reservation, reservationDetails);
			if (!DateUtils.compareDatesExtensiveByGet(oldDate, reservation.getDate())) {
				mailService.sendSellerUpdateReservation(reservation, oldDate);
			}
			return new ReservationDetails(reservation);
		} catch (InstanceNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (DuplicateInstanceException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (NoAuthorizedException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}

	}

	@DeleteMapping(path = "/client/{id}")
	public void deleteReservationClient(@PathVariable("id") int idReservation,
			@RequestParam(required = true, name = "idGoogleLogin") String idGoogleLogin) {
		try {
			Client client = clientService.getClientByIdGoogleLogin(idGoogleLogin);
			Reservation rsv = reservationService.getReservationById(idReservation);

			if (rsv.getState() == ReservationState.CANCELLED) {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
						NoAuthorizedOperationsNames.RESERVATION_NOT_MUTABLE);
			}

			if (rsv.getClient().getIdGoogleLogin().equals(client.getIdGoogleLogin())) {
				reservationService.cancelReservation(idReservation);
				mailService.sendSellerDeleteReservation(rsv);
			} else {
				throw new InstanceNotFoundException(idReservation, Reservation.class.getSimpleName());
			}
		} catch (InstanceNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (NoAuthorizedException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}

	@GetMapping(path = "client/{idGoogleLogin}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ReservationDetails> getClientReservations(@PathVariable("idGoogleLogin") String idGoogleLogin) {

		try {
			Client client = clientService.getClientByIdGoogleLogin(idGoogleLogin);

			List<Reservation> reservations = reservationService.getReservationsByIdClient(client.getIdClient());
			return TransformationUtils.reservationsToReservationsDetails(reservations);
		} catch (InstanceNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}

	}

	// SELLER ENDPOINTS
	@PostMapping(path = "/seller", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ReservationDetails addReservationSeller(@RequestBody ReservationDetails reservationDetails) {
		try {
			Reservation rsv = saveNewReservation(reservationDetails);
			mailService.sendClientNewReservation(rsv);
			return new ReservationDetails(rsv);
		} catch (DuplicateInstanceException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (InstanceNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (NoAuthorizedException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}

	@PutMapping(path = "/seller/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ReservationDetails updateReservationSeller(@PathVariable("id") int idReservation,
			@RequestBody ReservationDetails reservationDetails) {

		try {
			Reservation reservation = reservationService.getReservationById(idReservation);
			Shop shop = shopService.getShopById(reservationDetails.getIdShop());
			Client client = clientService.getClientByIdGoogleLogin(reservationDetails.getIdGoogleLoginClient());

			if (reservation.getShop().getIdShop() == shop.getIdShop()
					&& reservation.getClient().getIdClient() == client.getIdClient()) {

				Calendar oldDate = (Calendar) reservation.getDate().clone();
				Calendar rsvDate = DateUtils.parseDateLong(reservationDetails.getDate());
				if (rsvDate == null) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_DATE);
				}
				reservation.setDate(rsvDate);
				reservation.setRemarks(reservationDetails.getRemarks());

				reservationService.saveOrUpdateReservation(reservation);
				if (!DateUtils.compareDatesExtensiveByGet(oldDate, rsvDate)) {
					mailService.sendClientUpdateReservation(reservation, oldDate);
				}
				return new ReservationDetails(reservation);
			} else {
				throw new InstanceNotFoundException(idReservation, Reservation.class.getSimpleName());
			}
		} catch (InstanceNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (DuplicateInstanceException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (NoAuthorizedException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}

	@DeleteMapping(path = "/seller/{id}")
	public void deleteReservationSeller(@PathVariable("id") int idReservation,
			@RequestParam(required = true, name = "loginName") String loginName) {

		try {
			Seller seller = sellerService.getSellerByLoginName(loginName);
			Reservation rsv = reservationService.getReservationById(idReservation);

			if (rsv.getShop().getSeller().getLoginName().equals(seller.getLoginName())) {
				reservationService.cancelReservation(idReservation);
				mailService.sendClientDeleteReservation(rsv);
			} else {
				throw new InstanceNotFoundException(idReservation, Reservation.class.getSimpleName());
			}
		} catch (InstanceNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (NoAuthorizedException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}

	@GetMapping(path = "seller/{idShop}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ReservationDetails> getFutureShopReservations(@PathVariable("idShop") int idShop) {
		try {
			Shop shop = shopService.getShopById(idShop);
			Calendar now = Calendar.getInstance();
			List<Reservation> reservations = reservationService.getFutureReservations(now, shop.getIdShop());
			return TransformationUtils.reservationsToReservationsDetails(reservations);
		} catch (InstanceNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}

	}
}
