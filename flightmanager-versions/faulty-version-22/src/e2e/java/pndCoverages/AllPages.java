package pndCoverages;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.servlet.AfterServlet;
import org.jboss.arquillian.warp.servlet.BeforeServlet;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import it.unifi.dinfo.stlab.flightmanager.poms.AddAirportsDetails;
import it.unifi.dinfo.stlab.flightmanager.poms.AddAirportsMain;
import it.unifi.dinfo.stlab.flightmanager.poms.AddFlights;
import it.unifi.dinfo.stlab.flightmanager.poms.AdministratorPage;
import it.unifi.dinfo.stlab.flightmanager.poms.AirportList;
import it.unifi.dinfo.stlab.flightmanager.poms.AirportView;
import it.unifi.dinfo.stlab.flightmanager.poms.BookingDetails;
import it.unifi.dinfo.stlab.flightmanager.poms.BookingEdit;
import it.unifi.dinfo.stlab.flightmanager.poms.BookingLogin;
import it.unifi.dinfo.stlab.flightmanager.poms.BookingView;
import it.unifi.dinfo.stlab.flightmanager.poms.CountryList;
import it.unifi.dinfo.stlab.flightmanager.poms.FlightDetails;
import it.unifi.dinfo.stlab.flightmanager.poms.FlightView;
import it.unifi.dinfo.stlab.flightmanager.poms.FlightsList;
import it.unifi.dinfo.stlab.flightmanager.poms.FlightsResult;
import it.unifi.dinfo.stlab.flightmanager.poms.Home;
import it.unifi.dinfo.stlab.flightmanager.poms.InsertFlightsDetails;
import it.unifi.dinfo.stlab.flightmanager.poms.Login;
import it.unifi.dinfo.stlab.flightmanager.util.BeanUtils;
import it.unifi.dinfo.stlab.flightmanager.util.H2Manager;
import it.unifi.ing.swam.components.PasswordManagerComponent;
import it.unifi.ing.swam.model.Airport;
import it.unifi.ing.swam.model.Booking;
import it.unifi.ing.swam.model.Country;
import it.unifi.ing.swam.model.Flight;
import it.unifi.ing.swam.model.ModelFactory;
import it.unifi.ing.swam.model.Passenger;
import it.unifi.ing.swam.model.User;
import it.unifi.ing.swam.model.UserRole;

@WarpTest
@RunWith(Arquillian.class)
@RunAsClient
public class AllPages {

	// private static final int maxMinutesConversation = 15;

	// static final String packageName = "it.unifi.ing.swam.";
	// static final String managedBeanClassName = "class
	// org.jboss.weld.bean.ManagedBean";

	private static final String WEBAPP_SRC = "src/main/webapp";

	private static EntityManagerFactory entityManagerFactory;

	private static EntityManager em;

	@ArquillianResource
	public static URL contextPath;

	@Drone
	static WebDriver driver;

	// pages
	private Home homePage;
	private FlightsResult flightResultsPage;
	private FlightDetails flightDetailsPage;
	private BookingDetails bookingDetailsPage;
	private BookingLogin bookingLogin;
	private BookingView bookingView;
	private BookingEdit bookingEdit;
	private Login loginPage;
	private AdministratorPage administratorPage;
	private AirportList airportListPage;
	private AirportView airportViewPage;
	private AddAirportsMain addAirportsMain;
	private AddAirportsDetails addAirportsDetails;
	private AddFlights addFlights;
	private InsertFlightsDetails insertFlightsDetails;
	private FlightsList flightsList;
	private FlightView flightView;
	private CountryList countryListPage;

	private Passenger fooPassenger;
	private Booking pisaPalermoBooking;
	private String resCode;
	private int resIndex;

	// user credentials
	private String username = "userName";
	private String userPwd = "userpwd";
	
	// credentials
	private String adminName = "admin";
	private String adminPwd = "pass";

	// assets
	private Flight pisaPalermoFlight;
	private Flight palermoPisaFlight;
	private Airport pisaAirport;
	private Airport palermoAirport;
	
	// test data
	private String flightNumber;
	private Date departureDate;
	private String departureTime;
	private int duration;
	private int passengers;
	private float pricePerPerson;
	private String origin;
	private String destination;
	private int flightRepetitions;
	private boolean returnFlight;

	@Deployment(testable = true)
	public static WebArchive createDeployment() {

		File[] primefaces = Maven.resolver().loadPomFromFile("pom.xml").resolve("org.primefaces:primefaces:8.0")
				.withoutTransitivity().asFile();

		WebArchive war = ShrinkWrap.create(WebArchive.class, "search.war").addPackages(true, "it.unifi.ing.swam")
				.addClass(BeanUtils.class)
				.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class).importDirectory(WEBAPP_SRC)
						.as(GenericArchive.class), "/", Filters.include(".*\\.xhtml$"))
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF/faces-config.xml"), "faces-config.xml")
				.setWebXML(new File(WEBAPP_SRC, "WEB-INF/web.xml")).addAsWebInfResource("arquillian-ds.xml")
				.addAsLibraries(primefaces);

		war.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
				.importDirectory(WEBAPP_SRC + "/img").as(GenericArchive.class), "/img");
		war.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
				.importDirectory(WEBAPP_SRC + "/css").as(GenericArchive.class), "/css");
		war.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
				.importDirectory(WEBAPP_SRC + "/layout").as(GenericArchive.class), "/layout");

		System.out.println(war.toString(true));

		return war;

	}

	@BeforeClass
	public static void setUpClass() {
		entityManagerFactory = Persistence.createEntityManagerFactory("arquillian-test");
	}

	@Before
	public void setUp() {
		em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();

		H2Manager.deleteFromTables(em);

		// country creation
		Country italy = ModelFactory.country();
		italy.setCountryCode(111111L);
		italy.setName("Italia");
		italy.setUE(true);
		italy.setCountryFee((float) 0.225);

		// standard user creation
		User user = ModelFactory.user();
		user.setUserRole(UserRole.Registered);
		user.setUsername(username);
		user.setPassword(PasswordManagerComponent.encode(userPwd));
		user.setHomeCountry(italy);
		Calendar calendar = Calendar.getInstance();
		calendar.set(2017, Calendar.AUGUST, 30);
		user.setRegistrationDate(calendar.getTime());
		
		// standard admin creation
		User admin = ModelFactory.user();
		admin.setUserRole(UserRole.Admin);
		admin.setUsername(adminName);
		admin.setPassword(PasswordManagerComponent.encode(adminPwd));
		admin.setHomeCountry(italy);
		calendar.set(2016, Calendar.SEPTEMBER, 12);
		admin.setRegistrationDate(calendar.getTime());
		

		em.persist(italy);
		em.persist(user);
		em.persist(admin);

		// airports creation
		pisaAirport = ModelFactory.airport();
		pisaAirport.setAirportCountry(italy);
		pisaAirport.setCityName("Pisa");
		pisaAirport.setAirportFullName("Galileo Galilei");
		pisaAirport.setZIP(12345);
		pisaAirport.setGMT(1);
		palermoAirport = ModelFactory.airport();
		palermoAirport.setAirportCountry(italy);
		palermoAirport.setCityName("Palermo");
		palermoAirport.setAirportFullName("Palermo Airport");
		palermoAirport.setZIP(54321);
		palermoAirport.setGMT(1);

		em.persist(pisaAirport);
		em.persist(palermoAirport);

		// flight creation
		pisaPalermoFlight = ModelFactory.flight();
		pisaPalermoFlight.setFlightNumber("Pi-Pa-1");
		pisaPalermoFlight.setDepartureDate(new Date());
		pisaPalermoFlight.setDepartureTime(new Date());
		pisaPalermoFlight.setTotalSeats(100);
		pisaPalermoFlight.setPricePerPerson(80);
		pisaPalermoFlight.setFlightDuration(120);
		pisaPalermoFlight.setSourceAirport(pisaAirport);
		pisaPalermoFlight.setDestinationAirport(palermoAirport);

		em.persist(pisaPalermoFlight);
		
		palermoPisaFlight = ModelFactory.flight();
		palermoPisaFlight.setFlightNumber("Pa-Pi-1");
		palermoPisaFlight.setDepartureDate(new Date());
		palermoPisaFlight.setDepartureTime(new Date());
		palermoPisaFlight.setTotalSeats(100);
		palermoPisaFlight.setPricePerPerson(80);
		palermoPisaFlight.setFlightDuration(120);
		palermoPisaFlight.setSourceAirport(palermoAirport);
		palermoPisaFlight.setDestinationAirport(pisaAirport);
		palermoPisaFlight.setFlightDuration(30);
		
		em.persist(palermoPisaFlight);
		
		
		int numberOfBookings = 5; // must be greater than the number of deletions in any single test

		for (int i = 0; i < numberOfBookings; i++) {
			fooPassenger = ModelFactory.passenger();
			fooPassenger.setFirstName("Foo");
			fooPassenger.setSurname("Foer");
			fooPassenger.setIdCard("12345678");
			fooPassenger.setDateOfBirth(new Date(0));
			
			em.persist(fooPassenger);
			
			pisaPalermoBooking = ModelFactory.reservation();
			pisaPalermoBooking.setDate(new Date());
			pisaPalermoBooking.setPassengers(Arrays.asList(fooPassenger));
			pisaPalermoBooking.setFlight(pisaPalermoFlight);
			pisaPalermoBooking.setPrice(80);
			pisaPalermoBooking.setFinalPrice(80);
			pisaPalermoBooking.setReservationId("FMID-" + i);
			pisaPalermoBooking.setEmail("foo@foo.com");
			
			em.persist(pisaPalermoBooking);
		}
		
		resCode = "FMID-";
		resIndex = 1;

		em.getTransaction().commit();
		
		// flight data initialization
		flightNumber = "F1994";
		departureDate = new Date();
		departureTime = "03:27";
		duration = 94;
		passengers = 100;
		pricePerPerson = 100;
		origin = pisaAirport.getCityName();
		destination = palermoAirport.getCityName();
		flightRepetitions = 1;
		returnFlight = false;

		// redirect to BookingDetails page and initialize controller
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				driver.manage().timeouts().setScriptTimeout(100, TimeUnit.SECONDS);
				driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
				String url = contextPath.toString() + "index.xhtml";
				driver.get(url);
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});
	}

	@After
	public void tearDown() {

		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				String url = contextPath.toString() + "index.xhtml";
				driver.get(url);
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;

			@Inject
			BeanManager beanManager;

			@BeforeServlet
			public void teardown() {
				BeanUtils helper = new BeanUtils(beanManager);
				helper.cleanContexts();
			}
		});

		em.clear();
		em.close();
	}

	@AfterClass
	public static void tearDownClass() {
		em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();

		H2Manager.deleteFromTables(em);

		em.getTransaction().commit();
		entityManagerFactory.close();
	}

	@Test
	public void test1() {

		// Home - FlightsResult
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				homePage = new Home(driver);
				Date date = new Date();
				homePage.searchFlights(pisaAirport.getCityName(), palermoAirport.getCityName(), date, "1");
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// FlightsResult - FlightDetails
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				flightResultsPage = new FlightsResult(driver);
				flightResultsPage.clickDetailsButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// FlightDetails - BookingDetails
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				flightDetailsPage = new FlightDetails(driver);
				flightDetailsPage.clickConfirmButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// BookingDetails - Confirmation
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				bookingDetailsPage = new BookingDetails(driver);
				bookingDetailsPage.compileBookingDataFormAndConfirm("user@mail.com", "userName", "userSurname", "1212",
						"18/08/1990");
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;

		});

		// Confirmation - Home
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				bookingDetailsPage = new BookingDetails(driver);
				bookingDetailsPage.clickBackButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// Home - BookingLogin
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				homePage = new Home(driver);
				homePage.clickSearchBookingButton();
			}
		}).inspect(new Inspection() {

			private static final long serialVersionUID = 1L;
		});

		// BookingLogin - BookingView
		Warp.initiate(new Activity() {

			@Override
			public void perform() {

				bookingLogin = new BookingLogin(driver);
				bookingLogin.login(resCode + resIndex, pisaPalermoBooking.getEmail());
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// BookingView - BookingEdit
		Warp.initiate(new Activity() {
			
			@Override
			public void perform() {
				
				bookingView = new BookingView(driver);
				bookingView.goToBookingEdit();
			}
		}).inspect(new Inspection() {
			
			private static final long serialVersionUID = 1L;
		});
		
		// BookingEdit - UpdatePassengersSuccess - BookingView
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				bookingEdit = new BookingEdit(driver);
				bookingEdit.updateBooking("Bar", "Baer", "87654321", new Date(1000 * 3600 * 24));
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// BookingView - DeleteBookingSuccess - Home
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				
				bookingView = new BookingView(driver);
				bookingView.deleteBooking();
				resIndex++;
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});
		
		// Home - Login
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				homePage = new Home(driver);
				homePage.clickLoginButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});
		
		// Login - Home
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				loginPage = new Login(driver);
				loginPage.loginAsUser(username, userPwd);
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});
		
		// Home - BookingList
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				homePage = new Home(driver);
				homePage.clickMyBookingsButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
			
			@AfterServlet
			public void checkfinal() {
				assertTrue(true);
			}
		});
	}
	
	
	@Test
	public void test2() {
		
		// Home - Login
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				homePage = new Home(driver);
				homePage.clickLoginButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});
		
		// Login - AdministratorPage
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				loginPage = new Login(driver);
				loginPage.loginAsAdmin(adminName, adminPwd);
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});
		
		// AdministratorPage - AirportList
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				administratorPage = new AdministratorPage(driver);
				administratorPage.clickAirportListButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});
		
		// AirportList - AirportView
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				airportListPage = new AirportList(driver);
				airportListPage.clickDetailsButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});
		
		// AirportView - AirportList 
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				airportViewPage = new AirportView(driver);
				airportViewPage.clickReturnButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});
		
		// AirportList - AdministratorPage
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				airportListPage = new AirportList(driver);
				airportListPage.clickBackButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});
		
		// AdministratorPage - AddAirportsMain
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				administratorPage = new AdministratorPage(driver);
				administratorPage.clickAddAirportButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});
		
		// AddAirportsMain - AddAirportsDetails
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				addAirportsMain = new AddAirportsMain(driver);
				addAirportsMain.insertAirportInfo("Italia", "Firenze", "Amerigo Vespucci", "1", "50127");
				addAirportsMain.clickNextButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});
		
		// AddAirportsDetails - AdministratorPage
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				addAirportsDetails = new AddAirportsDetails(driver);
				addAirportsDetails.insertAirportDetails("Via del Termine, 11", true, "", "");
				addAirportsDetails.clickSaveButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});
		
		// AdministratorPage - AddFlights
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				administratorPage = new AdministratorPage(driver);
				administratorPage.clickAddFlightButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});
		
		// AddFlights - InsertFlightDetails
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				addFlights = new AddFlights(driver);
				addFlights.insertFlightInfo(flightNumber, departureDate, departureTime, duration,
						passengers, pricePerPerson, origin, destination);
				addFlights.clickNextButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});
		
		// InsertFlightDetails - AdministratorPage
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				insertFlightsDetails = new InsertFlightsDetails(driver);
				insertFlightsDetails.insertFlightDetails(flightRepetitions, returnFlight);
				insertFlightsDetails.clickSaveButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});
		
		// AdministratorPage - FlightsList
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				administratorPage = new AdministratorPage(driver);
				administratorPage.clickFlightsListButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});
		
		// FlightsList - FlightView
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				flightsList = new FlightsList(driver);
				flightsList.clickDetailsButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});
		
		// FlightView - FlightsList 
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				flightView = new FlightView(driver);
				flightView.clickReturnButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});
		
		// FlightsList - DeleteFlightSuccess - FlightsList 
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				flightsList = new FlightsList(driver);
				flightsList.clickDeleteButtonAndReturn();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});
		
		// FlightsList - AdministratorPage 
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				flightsList = new FlightsList(driver);
				flightsList.clickBackButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});
		
		// AdministratorPage - CountryList
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				administratorPage = new AdministratorPage(driver);
				administratorPage.clickCountryListButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});
		
		// CountryList - AdministratorPage 
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				countryListPage = new CountryList(driver);
				countryListPage.clickBackButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});
		
		// AdministratorPage - AddCountry  
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				administratorPage = new AdministratorPage(driver);
				administratorPage.clickAddCountryButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});
	}
}
