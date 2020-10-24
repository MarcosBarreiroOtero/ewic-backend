package es.ewic.backend.controller;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import es.ewic.backend.model.client.Client;
import es.ewic.backend.model.reservation.Reservation;
import es.ewic.backend.model.shop.Shop;
import es.ewic.backend.modelutil.DateUtils;
import es.ewic.backend.modelutil.exceptions.DuplicateInstanceException;
import es.ewic.backend.modelutil.exceptions.InstanceNotFoundException;
import es.ewic.backend.modelutil.exceptions.NoAuthorizedException;
import es.ewic.backend.service.clientService.ClientService;
import es.ewic.backend.service.mailService.MailService;
import es.ewic.backend.service.reservationService.ReservationDetails;
import es.ewic.backend.service.reservationService.ReservationService;
import es.ewic.backend.service.shopService.ShopService;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

	@Autowired
	private ReservationService reservationService;
	@Autowired
	private ClientService clientService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private MailService mailService;

	private Reservation saveNewReservation(ReservationDetails reservationDetails)
			throws InstanceNotFoundException, DuplicateInstanceException, NoAuthorizedException {
		Client client = clientService.getClientByIdGoogleLogin(reservationDetails.getIdGoogleLoginClient());
		Shop shop = shopService.getShopById(reservationDetails.getIdShop());
		Calendar rsvDate = DateUtils.parseDateLong(reservationDetails.getDate());
		if (rsvDate == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date");
		}
		Reservation rsv = new Reservation(rsvDate, reservationDetails.getState(), reservationDetails.getRemarks(),
				client, shop);
		return reservationService.saveOrUpdateReservation(rsv);
	}

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

	@PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ReservationDetails updateReservation(@PathVariable("id") int idReservation,
			@RequestBody ReservationDetails reservationDetails) {

		try {
			Reservation reservation = reservationService.getReservationById(idReservation);
			Shop shop = shopService.getShopById(reservationDetails.getIdShop());
			Client client = clientService.getClientByIdGoogleLogin(reservationDetails.getIdGoogleLoginClient());

			if (reservation.getShop().getIdShop() == shop.getIdShop()
					&& reservation.getClient().getIdClient() == client.getIdClient()) {

				Calendar rsvDate = DateUtils.parseDateLong(reservationDetails.getDate());
				if (rsvDate == null) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date");
				}
				reservation.setDate(rsvDate);
				reservation.setRemarks(reservationDetails.getRemarks());

				reservationService.saveOrUpdateReservation(reservation);
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
}
