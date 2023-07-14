package it.unifi.ing.swam.components.startup;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.transaction.Transactional;

import it.unifi.ing.swam.components.PasswordManagerComponent;
import it.unifi.ing.swam.dao.CountryDao;
import it.unifi.ing.swam.dao.UserDao;
import it.unifi.ing.swam.dao.AirportDao;
import it.unifi.ing.swam.dao.FlightDao;
import it.unifi.ing.swam.model.*;
import it.unifi.ing.swam.util.Util;

import java.util.Calendar;
import java.util.Date;

@Singleton
@Startup
public class StartupComponent {

	@Inject
	private UserDao userDao;

	@Inject
	private FlightDao flightDao;

	@Inject
	private AirportDao airportDao;

	@Inject
	private CountryDao countryDao;

	@Inject
	private PasswordManagerComponent pwdManager;

	@PostConstruct
	@Transactional
	public void init() {
		// Default admin and user creation
		if (airportDao.getAllAirports() == null) { // una condizione come un'altra per capire se il DB è vuoto
			Country userCountry = initCountriesAirportsAndFlights(); // aggiunge Countries, Airports e Flights
			// ritorna italy solo per comodità del codice, è una bruttura che mi ha
			// risparmiato qualche minuto di refactoring
			userDao.save(getAdmin("admin", "pass")); // inizializza e persiste admin
			userDao.save(getUser("user", "pass", userCountry)); // inizializza e persiste l'utente
		}
	}

	private User getUser(String username, String password, Country userCountry) {
		User user = ModelFactory.user();
		user.setUserRole(UserRole.Registered);
		user.setUsername(username);
		user.setPassword(pwdManager.encode(password));
		user.setHomeCountry(userCountry);
		Calendar calendar = Calendar.getInstance();
		calendar.set(2017, Calendar.AUGUST, 30);
		user.setRegistrationDate(calendar.getTime());

		return user;
	}

	private User getAdmin(String username, String password) {
		User admin = ModelFactory.user();
		admin.setUserRole(UserRole.Admin);
		admin.setUsername(username);
		admin.setPassword(pwdManager.encode(password));

		return admin;
	}

	private Country initCountriesAirportsAndFlights() {
		// costruzione ed inserimento di una Country
		Country italy = ModelFactory.country();
		italy.setCountryCode(111111L);
		italy.setName("Italia");
		italy.setUE(true);
		italy.setCountryFee((float) 0.225);
		countryDao.save(italy);

		Country france = ModelFactory.country();
		france.setCountryCode(000000L);
		france.setName("Francia");
		france.setUE(true);
		france.setCountryFee((float) 0.18);
		countryDao.save(france);

		Country colombia = ModelFactory.country();
		colombia.setCountryCode(123123L);
		colombia.setName("Colombia");
		colombia.setUE(false);
		colombia.setCountryFee((float) 0.05);
		countryDao.save(colombia);

		// inizio inserimento aeroporti - ognuno è associato ad una country

		Airport pisaAirport = ModelFactory.airport();
		pisaAirport.setAirportCountry(italy);
		pisaAirport.setCityName("Pisa");
		pisaAirport.setAirportFullName("Galileo Galilei");
		pisaAirport.setZIP(12345);
		pisaAirport.setGMT(1);
		airportDao.save(pisaAirport);

		Airport palermoAirport = ModelFactory.airport();
		palermoAirport.setAirportCountry(italy);
		palermoAirport.setCityName("Palermo");
		pisaAirport.setAirportFullName("Palermo Airport");
		palermoAirport.setZIP(54321);
		palermoAirport.setGMT(1);
		airportDao.save(palermoAirport);

		Airport bolognaAirport = ModelFactory.airport();
		bolognaAirport.setAirportCountry(italy);
		bolognaAirport.setCityName("Bologna");
		pisaAirport.setAirportFullName("Bologna Airport");
		bolognaAirport.setZIP(23456);
		bolognaAirport.setGMT(1);
		airportDao.save(bolognaAirport);

		Airport romaAirport = ModelFactory.airport();
		romaAirport.setAirportCountry(italy);
		romaAirport.setCityName("Roma");
		pisaAirport.setAirportFullName("Roma Airport");
		romaAirport.setZIP(56789);
		romaAirport.setGMT(1);
		airportDao.save(romaAirport);

		Airport parigiAirport = ModelFactory.airport();
		parigiAirport.setAirportCountry(france);
		parigiAirport.setCityName("Parigi");
		parigiAirport.setAirportFullName("Paris Airport");
		parigiAirport.setZIP(20011);
		parigiAirport.setGMT(1);
		airportDao.save(parigiAirport);

		Airport bogotaAirport = ModelFactory.airport();
		bogotaAirport.setAirportCountry(colombia);
		bogotaAirport.setCityName("Bogotà");
		pisaAirport.setAirportFullName("Bogotà Airport");
		bogotaAirport.setZIP(12309);
		bogotaAirport.setGMT(-5);
		airportDao.save(bogotaAirport);

		// inizio inserimento flights, ognuno è associato a due aeroporti
		Date date;

		Flight voloRomaParigi = ModelFactory.flight();
		voloRomaParigi.setFlightNumber("Roma-Parigi-1");
		voloRomaParigi.setDepartureDate(new Date());
		voloRomaParigi.setDepartureTime(new Date());
		voloRomaParigi.setTotalSeats(300);
		voloRomaParigi.setPricePerPerson(280);
		voloRomaParigi.setFlightDuration(320);
		voloRomaParigi.setSourceAirport(romaAirport);
		voloRomaParigi.setDestinationAirport(parigiAirport);
		flightDao.save(voloRomaParigi);
		date = voloRomaParigi.getDepartureDate();
		for (int i = 0; i < 30; i++) { // copio il volo per 30 giorni (così ripeto il volo per ogni giorno del mese)
			date = Util.incrementDay(date);
			Flight fl = ModelFactory.flight();
			fl.copyFlight(voloRomaParigi);
			fl.setDepartureDate(date);
			flightDao.save(fl);
		}

		Flight voloParigiRoma = ModelFactory.flight();
		voloParigiRoma.setFlightNumber("Parigi-Roma-1");
		voloParigiRoma.setDepartureDate(new Date());
		voloParigiRoma.setDepartureTime(new Date());
		voloParigiRoma.setTotalSeats(300);
		voloParigiRoma.setPricePerPerson(280);
		voloParigiRoma.setFlightDuration(320);
		voloParigiRoma.setSourceAirport(parigiAirport);
		voloParigiRoma.setDestinationAirport(romaAirport);
		flightDao.save(voloParigiRoma);
		date = voloParigiRoma.getDepartureDate();
		for (int i = 0; i < 30; i++) {
			date = Util.incrementDay(date);
			Flight fl = ModelFactory.flight();
			fl.copyFlight(voloParigiRoma);
			fl.setDepartureDate(date);
			flightDao.save(fl);
		}

		Flight voloParigiBogota = ModelFactory.flight();
		voloParigiBogota.setFlightNumber("Parigi-Bogota-1");
		voloParigiBogota.setDepartureDate(new Date());
		voloParigiBogota.setDepartureTime(new Date());
		voloParigiBogota.setTotalSeats(600);
		voloParigiBogota.setPricePerPerson(640);
		voloParigiBogota.setFlightDuration(1024);
		voloParigiBogota.setSourceAirport(parigiAirport);
		voloParigiBogota.setDestinationAirport(bogotaAirport);
		flightDao.save(voloParigiBogota);
		date = voloParigiBogota.getDepartureDate();
		for (int i = 0; i < 30; i++) {
			date = Util.incrementDay(date);
			Flight fl = ModelFactory.flight();
			fl.copyFlight(voloParigiBogota);
			fl.setDepartureDate(date);
			flightDao.save(fl);
		}

		Flight voloBogotaParigi = ModelFactory.flight();
		voloBogotaParigi.setFlightNumber("Bogota-Parigi-1");
		voloBogotaParigi.setDepartureDate(new Date());
		voloBogotaParigi.setDepartureTime(new Date());
		voloBogotaParigi.setTotalSeats(600);
		voloBogotaParigi.setPricePerPerson(640);
		voloBogotaParigi.setFlightDuration(1024);
		voloBogotaParigi.setSourceAirport(bogotaAirport);
		voloBogotaParigi.setDestinationAirport(parigiAirport);
		flightDao.save(voloBogotaParigi);
		date = voloBogotaParigi.getDepartureDate();
		for (int i = 0; i < 30; i++) {
			date = Util.incrementDay(date);
			Flight fl = ModelFactory.flight();
			fl.copyFlight(voloBogotaParigi);
			fl.setDepartureDate(date);
			flightDao.save(fl);
		}

		Flight voloPisaPalermo = ModelFactory.flight();
		voloPisaPalermo.setFlightNumber("Pi-Pa-1");
		voloPisaPalermo.setDepartureDate(new Date());
		voloPisaPalermo.setDepartureTime(new Date());
		voloPisaPalermo.setTotalSeats(100);
		voloPisaPalermo.setPricePerPerson(80);
		voloPisaPalermo.setFlightDuration(120);
		voloPisaPalermo.setSourceAirport(pisaAirport);
		voloPisaPalermo.setDestinationAirport(palermoAirport);
		flightDao.save(voloPisaPalermo);
		date = voloPisaPalermo.getDepartureDate();
		for (int i = 0; i < 30; i++) {
			date = Util.incrementDay(date);
			Flight fl = ModelFactory.flight();
			fl.copyFlight(voloPisaPalermo);
			fl.setDepartureDate(date);
			flightDao.save(fl);
		}

		Flight voloPalermoPisa = ModelFactory.flight();
		voloPalermoPisa.setFlightNumber("Pa-Pi-1");
		voloPalermoPisa.setDepartureDate(new Date());
		voloPalermoPisa.setDepartureTime(new Date());
		voloPalermoPisa.setTotalSeats(100);
		voloPalermoPisa.setPricePerPerson(80);
		voloPalermoPisa.setFlightDuration(120);
		voloPalermoPisa.setSourceAirport(palermoAirport);
		voloPalermoPisa.setDestinationAirport(pisaAirport);
		voloPalermoPisa.setFlightDuration(30);
		flightDao.save(voloPalermoPisa);
		date = voloPalermoPisa.getDepartureDate();
		for (int i = 0; i < 30; i++) {
			date = Util.incrementDay(date);
			Flight fl = ModelFactory.flight();
			fl.copyFlight(voloPalermoPisa);
			fl.setDepartureDate(date);
			flightDao.save(fl);
		}

		Flight voloPisaRoma = ModelFactory.flight();
		voloPisaRoma.setFlightNumber("Pi-Ro-1");
		voloPisaRoma.setDepartureDate(new Date());
		voloPisaRoma.setDepartureTime(new Date());
		voloPisaRoma.setTotalSeats(100);
		voloPisaRoma.setPricePerPerson(40);
		voloPisaRoma.setFlightDuration(60);
		voloPisaRoma.setSourceAirport(pisaAirport);
		voloPisaRoma.setDestinationAirport(romaAirport);
		flightDao.save(voloPisaRoma);
		date = voloPisaRoma.getDepartureDate();
		for (int i = 0; i < 30; i++) {
			date = Util.incrementDay(date);
			Flight fl = ModelFactory.flight();
			fl.copyFlight(voloPisaRoma);
			fl.setDepartureDate(date);
			flightDao.save(fl);
		}

		Flight voloRomaPisa = ModelFactory.flight();
		voloRomaPisa.setFlightNumber("Ro-Pi-1");
		voloRomaPisa.setDepartureDate(new Date());
		voloRomaPisa.setDepartureTime(new Date());
		voloRomaPisa.setTotalSeats(100);
		voloRomaPisa.setPricePerPerson(40);
		voloRomaPisa.setFlightDuration(60);
		voloRomaPisa.setSourceAirport(romaAirport);
		voloRomaPisa.setDestinationAirport(pisaAirport);
		flightDao.save(voloRomaPisa);
		date = voloRomaPisa.getDepartureDate();
		for (int i = 0; i < 30; i++) {
			date = Util.incrementDay(date);
			Flight fl = ModelFactory.flight();
			fl.copyFlight(voloRomaPisa);
			fl.setDepartureDate(date);
			flightDao.save(fl);
		}

		Flight voloBolognaRoma = ModelFactory.flight();
		voloBolognaRoma.setFlightNumber("Bo-Ro-1");
		voloBolognaRoma.setDepartureDate(new Date());
		voloBolognaRoma.setDepartureTime(new Date());
		voloBolognaRoma.setTotalSeats(100);
		voloBolognaRoma.setPricePerPerson(50);
		voloBolognaRoma.setFlightDuration(90);
		voloBolognaRoma.setSourceAirport(bolognaAirport);
		voloBolognaRoma.setDestinationAirport(romaAirport);
		flightDao.save(voloBolognaRoma);
		date = voloBolognaRoma.getDepartureDate();
		for (int i = 0; i < 30; i++) {
			date = Util.incrementDay(date);
			Flight fl = ModelFactory.flight();
			fl.copyFlight(voloBolognaRoma);
			fl.setDepartureDate(date);
			flightDao.save(fl);
		}

		Flight voloRomaBologna = ModelFactory.flight();
		voloRomaBologna.setFlightNumber("Ro-Bo-1");
		voloRomaBologna.setDepartureDate(new Date());
		voloRomaBologna.setDepartureTime(new Date());
		voloRomaBologna.setTotalSeats(100);
		voloRomaBologna.setPricePerPerson(50);
		voloRomaBologna.setFlightDuration(90);
		voloRomaBologna.setSourceAirport(romaAirport);
		voloRomaBologna.setDestinationAirport(bolognaAirport);
		flightDao.save(voloRomaBologna);
		date = voloRomaBologna.getDepartureDate();
		for (int i = 0; i < 30; i++) {
			date = Util.incrementDay(date);
			Flight fl = ModelFactory.flight();
			fl.copyFlight(voloRomaBologna);
			fl.setDepartureDate(date);
			flightDao.save(fl);
		}

		Flight voloBolognaPalermo = ModelFactory.flight();
		voloBolognaPalermo.setFlightNumber("Bo-Pa-1");
		voloBolognaPalermo.setDepartureDate(new Date());
		voloBolognaPalermo.setDepartureTime(new Date());
		voloBolognaPalermo.setTotalSeats(100);
		voloBolognaPalermo.setPricePerPerson(50);
		voloBolognaPalermo.setFlightDuration(90);
		voloBolognaPalermo.setSourceAirport(bolognaAirport);
		voloBolognaPalermo.setDestinationAirport(palermoAirport);
		flightDao.save(voloBolognaPalermo);
		date = voloBolognaPalermo.getDepartureDate();
		for (int i = 0; i < 30; i++) {
			date = Util.incrementDay(date);
			Flight fl = ModelFactory.flight();
			fl.copyFlight(voloBolognaPalermo);
			fl.setDepartureDate(date);
			flightDao.save(fl);
		}

		Flight voloPalermoBologna = ModelFactory.flight();
		voloPalermoBologna.setFlightNumber("Pa-Bo-1");
		voloPalermoBologna.setDepartureDate(new Date());
		voloPalermoBologna.setDepartureTime(new Date());
		voloPalermoBologna.setTotalSeats(100);
		voloPalermoBologna.setPricePerPerson(50);
		voloPalermoBologna.setFlightDuration(90);
		voloPalermoBologna.setSourceAirport(palermoAirport);
		voloPalermoBologna.setDestinationAirport(bolognaAirport);
		flightDao.save(voloPalermoBologna);
		date = voloPalermoBologna.getDepartureDate();
		for (int i = 0; i < 30; i++) {
			date = Util.incrementDay(date);
			Flight fl = ModelFactory.flight();
			fl.copyFlight(voloPalermoBologna);
			fl.setDepartureDate(date);
			flightDao.save(fl);
		}
		return italy;
	}

}