package pndCoverages;

import static org.junit.Assert.assertEquals;
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
import it.unifi.dinfo.stlab.flightmanager.poms.AddCountry;
import it.unifi.dinfo.stlab.flightmanager.poms.AddFlights;
import it.unifi.dinfo.stlab.flightmanager.poms.AdministratorPage;
import it.unifi.dinfo.stlab.flightmanager.poms.AirportList;
import it.unifi.dinfo.stlab.flightmanager.poms.AirportView;
import it.unifi.dinfo.stlab.flightmanager.poms.BookingDetails;
import it.unifi.dinfo.stlab.flightmanager.poms.BookingEdit;
import it.unifi.dinfo.stlab.flightmanager.poms.BookingList;
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
public class AllNavigations {

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
	private FlightsResult flightResults;
	private FlightDetails flightDetails;
	private BookingDetails bookingDetails;
	private BookingLogin bookingLogin;
	private BookingView bookingView;
	private BookingEdit bookingEdit;
	private BookingList bookingList;
	private Login loginPage;
	private AdministratorPage administratorPage;
	private AddCountry addCountry;
	private CountryList countryList;
	private AirportList airportListPage;
	private AirportView airportViewPage;
	private AddAirportsMain addAirportsMain;
	private AddAirportsDetails addAirportsDetails;
	private AddFlights addFlights;
	private InsertFlightsDetails insertFlightsDetails;
	private FlightsList flightsList;
	private FlightView flightView;

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

	// user test data
	private String userMail = "foo@foo.com";
	private String userName = "Foo";
	private String userSurname = "Foer";
	private String userIdCard = "12345678";
	private String userBirthDate = "01/01/1990";
	private String userNewName = "Bar";
	private String userNewSurname = "Baer";
	private String userNewIdCard = "87654321";
	private Date userNewBirthDate = new Date(1000 * 3600 * 24); // 02/01/1990

	// country test data
	private String originCountryName = "Italia";
	private Long originCountryCode = 111111L;
	private Float originCountryFee = 0.225f;
	private boolean originCountryEu = true;
	private String destinationCountryName = "Spagna";
	private String destinationCountryCode = "222222";
	private Float destinationCountryFee = 0.175f;
	private boolean destinationCountryEu = true;

	// airport test data
	private String originAirportCountry = "Italia";
	private String originAirportCity = "Firenze";
	private String originAirportName = "Amerigo Vespucci";
	private String originAirportGmt = "1";
	private String originAirportZip = "50127";
	private String originAirportAddress = "Via del Termine, 11";
	private boolean originAirportInternational = true;
	private String originAirportMail = "";
	private String originAirportPhone = "";
	private String destinationAirportCountry = "Spagna";
	private String destinationAirportCity = "Barcellona";
	private String destinationAirportName = "Josep Tarradellas";
	private String destinationAirportGmt = "1";
	private String destinationAirportZip = "08820";
	private String destinationAirportAddress = "Prat de Llobregat S/N";
	private boolean destinationAirportInternational = true;
	private String destinationAirportMail = "";
	private String destinationAirportPhone = "";

	// flight test data
	private String outboundFlightNumber = "F1994";
	private Date outboundFlightDepartureDate = new Date();
	private String outboundFlightDepartureTime = "03:27";
	private int outboundFlightDuration = 94;
	private int outboundFlightPassengers = 100;
	private float outboundFlightPricePerPerson = 100;
	private String outboundFlightOriginCity = "Firenze";
	private String outboundFlightDestinationCity = "Barcellona";
	private int outboundFlightRepetitions = 1;
	private boolean outboundFlightReturn = false;
	private String inboundFlightNumber = "F1995";
	private Date inboundFlightDepartureDate = new Date();
	private String inboundFlightDepartureTime = "05:24";
	private int inboundFlightDuration = 95;
	private int inboundFlightPassengers = 100;
	private float inboundFlightPricePerPerson = 100;
	private String inboundFlightOriginCity = "Barcellona";
	private String inboundFlightDestinationCity = "Firenze";
	private int inboundFlightRepetitions = 1;
	private boolean inboundFlightReturn = false;

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
		italy.setName(originCountryName);
		italy.setCountryCode(originCountryCode);
		italy.setCountryFee(originCountryFee);
		italy.setUE(originCountryEu);

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

		int numberOfBookings = 3; // must be greater than the number of deletions in any single test

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
			pisaPalermoBooking.setRegisteredUserOwner(user);

			em.persist(pisaPalermoBooking);
		}

		resCode = "FMID-";
		resIndex = 1;

		em.getTransaction().commit();

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

		// [Home] --search--> [FlightsResult]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				homePage = new Home(driver);
				Date date = new Date();
				homePage.searchFlights(pisaAirport.getCityName(), palermoAirport.getCityName(), date, "2");
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [FlightsResult] --cancel--> [Home]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				flightResults = new FlightsResult(driver);
				flightResults.clickCancelButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [Home] --search--> [FlightsResult]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				homePage = new Home(driver);
				Date date = new Date();
				homePage.searchFlights(pisaAirport.getCityName(), palermoAirport.getCityName(), date, "2");
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [FlightsResult] --select-flight--> [BookingDetails]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				flightResults = new FlightsResult(driver);
				flightResults.clickConfirmButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [BookingDetails] --cancel--> [Home]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				bookingDetails = new BookingDetails(driver);
				bookingDetails.clickCancelButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [Home] --search--> [FlightsResult]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				homePage = new Home(driver);
				Date date = new Date();
				homePage.searchFlights(pisaAirport.getCityName(), palermoAirport.getCityName(), date, "2");
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [FlightsResult] --view-details--> [FlightDetails]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				flightResults = new FlightsResult(driver);
				flightResults.clickDetailsButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [FlightDetails] --back--> [FlightsResult]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				flightDetails = new FlightDetails(driver);
				flightDetails.clickBackButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [FlightsResult] --view-details--> [FlightDetails]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				flightResults = new FlightsResult(driver);
				flightResults.clickDetailsButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [FlightDetails] --new-search--> [FlightsResult]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				flightDetails = new FlightDetails(driver);
				Date date = new Date();
				flightDetails.searchAnotherFlight(palermoAirport.getCityName(), pisaAirport.getCityName(), date, "2");
				
				String displayedSourceAirport = flightResults.getSourceAirportOfFirstResult();
				String displayedDestinationAirport = flightResults.getDestinationAirportOfFirstResult();
				assertEquals(palermoAirport.getCityName(), displayedSourceAirport);
				assertEquals(pisaAirport.getCityName(), displayedDestinationAirport);
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [FlightsResult] --view-details--> [FlightDetails]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				flightResults = new FlightsResult(driver);
				flightResults.clickDetailsButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [FlightDetails] --confirm--> [BookingDetails]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				flightDetails = new FlightDetails(driver);
				flightDetails.clickConfirmButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [BookingDetails] --confirm--> [Confirmation]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				bookingDetails = new BookingDetails(driver);
				bookingDetails.compileBookingDataFormAndConfirm(userMail, userName, userSurname, userIdCard,
						userBirthDate);
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [Confirmation] --back--> [Home]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				bookingDetails = new BookingDetails(driver);
				bookingDetails.clickBackButton();
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

		// [Home] --my-booking--> [BookingLogin]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				homePage = new Home(driver);
				homePage.clickSearchBookingButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [BookingLogin] --back-to-home--> [Home]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				bookingLogin = new BookingLogin(driver);
				bookingLogin.clickBackButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [Home] --my-booking--> [BookingLogin]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				homePage = new Home(driver);
				homePage.clickSearchBookingButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [BookingLogin] --enter--> [BookingView]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				bookingLogin = new BookingLogin(driver);
				bookingLogin.login(resCode + resIndex, pisaPalermoBooking.getEmail());
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [BookingView] --back-to-home--> [Home]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				bookingView = new BookingView(driver);
				bookingView.backToHome();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [Home] --my-booking--> [BookingLogin]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				homePage = new Home(driver);
				homePage.clickSearchBookingButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [BookingLogin] --enter--> [BookingView]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				bookingLogin = new BookingLogin(driver);
				bookingLogin.login(resCode + resIndex, pisaPalermoBooking.getEmail());
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [BookingView] --edit-passengers--> [BookingEdit]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				bookingView = new BookingView(driver);
				bookingView.goToBookingEdit();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [BookingEdit] --cancel--> [BookingView]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				bookingEdit = new BookingEdit(driver);
				bookingEdit.clickCancelButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [BookingView] --edit-passengers--> [BookingEdit]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				bookingView = new BookingView(driver);
				bookingView.goToBookingEdit();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [BookingEdit] --update--> [updatePassengersSuccess] --return--> [BookingView]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				bookingEdit = new BookingEdit(driver);
				bookingEdit.updateBooking(userNewName, userNewSurname, userNewIdCard, userNewBirthDate);
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [BookingView] --delete--> [DeleteBookingSuccess] --return--> [Home]
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

		// [Home] --login--> [Login]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				homePage = new Home(driver);
				homePage.clickLoginButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [Login] --login-as-user--> [Home]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				loginPage = new Login(driver);
				loginPage.loginAsUser(username, userPwd);
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [Home] --logged-my-bookings--> [BookingList]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				homePage = new Home(driver);
				homePage.clickMyBookingsButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [BookingList] --details--> [BookingView]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				bookingList = new BookingList(driver);
				bookingList.goToBookingView();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [BookingView] --my-bookings--> [BookingList]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				bookingView = new BookingView(driver);
				bookingView.goToBookingList();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [BookingList] --details--> [BookingView]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				bookingList = new BookingList(driver);
				bookingList.goToBookingView();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [BookingView] --delete--> [DeleteBookingSuccess] --return--> [BookingList]
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

		// [BookingList] --back-to-home--> [Home]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				bookingList = new BookingList(driver);
				bookingList.backToHome();
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
	public void test3() {

		// [Home] --login--> [Login]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				homePage = new Home(driver);
				homePage.clickLoginButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [Login] --login-as-admin--> [AdministratorPage]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				loginPage = new Login(driver);
				loginPage.loginAsAdmin(adminName, adminPwd);
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [AdministratorPage] --add-country--> [AddCountry]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				administratorPage = new AdministratorPage(driver);
				administratorPage.clickAddCountryButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [AddCountry] --cancel--> [AdministratorPage]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				addCountry = new AddCountry(driver);
				addCountry.clickCancelButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [AdministratorPage] --add-country--> [AddCountry]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				administratorPage = new AdministratorPage(driver);
				administratorPage.clickAddCountryButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [AddCountry] --save--> [AdministratorPage]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				addCountry = new AddCountry(driver);
				addCountry.insertCountryInfo(destinationCountryName, destinationCountryCode, destinationCountryFee,
						destinationCountryEu);
				addCountry.clickSaveButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [AdministratorPage] --view-countries--> [CountryList]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				administratorPage = new AdministratorPage(driver);
				administratorPage.clickCountryListButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [CountryList] --back--> [AdministratorPage]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				countryList = new CountryList(driver);
				countryList.clickBackButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [AdministratorPage] --add-airport--> [AddAirportsMain]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				administratorPage = new AdministratorPage(driver);
				administratorPage.clickAddAirportButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [AddAirportsMain] --next--> [AddAirportsDetails]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				addAirportsMain = new AddAirportsMain(driver);
				addAirportsMain.insertAirportInfo(originAirportCountry, originAirportCity, originAirportName,
						originAirportGmt, originAirportZip);
				addAirportsMain.clickNextButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [AddAirportsDetails] --back--> [AddAirportsMain]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				addAirportsDetails = new AddAirportsDetails(driver);
				addAirportsDetails.clickBackButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [AddAirportsMain] --cancel--> [AdministratorPage]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				addAirportsMain = new AddAirportsMain(driver);
				addAirportsMain.clickCancelButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [AdministratorPage] --add-airport--> [AddAirportsMain]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				administratorPage = new AdministratorPage(driver);
				administratorPage.clickAddAirportButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [AddAirportsMain] --next--> [AddAirportsDetails]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				addAirportsMain = new AddAirportsMain(driver);
				addAirportsMain.insertAirportInfo(originAirportCountry, originAirportCity, originAirportName,
						originAirportGmt, originAirportZip);
				addAirportsMain.clickNextButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [AddAirportsDetails] --cancel--> [AdministratorPage]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				addAirportsDetails = new AddAirportsDetails(driver);
				addAirportsDetails.clickCancelButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [AdministratorPage] --add-airport--> [AddAirportsMain]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				administratorPage = new AdministratorPage(driver);
				administratorPage.clickAddAirportButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [AddAirportsMain] --next--> [AddAirportsDetails]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				addAirportsMain = new AddAirportsMain(driver);
				addAirportsMain.insertAirportInfo(originAirportCountry, originAirportCity, originAirportName,
						originAirportGmt, originAirportZip);
				addAirportsMain.clickNextButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [AddAirportsDetails] --save-and-add-new--> [AddAirportsMain]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				addAirportsDetails = new AddAirportsDetails(driver);
				addAirportsDetails.insertAirportDetails(originAirportAddress, originAirportInternational,
						originAirportMail, originAirportPhone);
				addAirportsDetails.clickSaveAndAddNewButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [AddAirportsMain] --next--> [AddAirportsDetails]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				addAirportsMain = new AddAirportsMain(driver);
				addAirportsMain.insertAirportInfo(destinationAirportCountry, destinationAirportCity,
						destinationAirportName, destinationAirportGmt, destinationAirportZip);
				addAirportsMain.clickNextButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [AddAirportsDetails] --save--> [AdministratorPage]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				addAirportsDetails = new AddAirportsDetails(driver);
				addAirportsDetails.insertAirportDetails(destinationAirportAddress, destinationAirportInternational,
						destinationAirportMail, destinationAirportPhone);
				addAirportsDetails.clickSaveButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [AdministratorPage] --view-airport--> [AirportList]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				administratorPage = new AdministratorPage(driver);
				administratorPage.clickAirportListButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [AirportList] --view-details--> [AirportView]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				airportListPage = new AirportList(driver);
				airportListPage.clickDetailsButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [AirportView] --return--> [AirportList]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				airportViewPage = new AirportView(driver);
				airportViewPage.clickReturnButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [AirportList] --back--> [AdministratorPage]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				airportListPage = new AirportList(driver);
				airportListPage.clickBackButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [AdministratorPage] --add-flight--> [AddFlights]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				administratorPage = new AdministratorPage(driver);
				administratorPage.clickAddFlightButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [AddFlights] --cancel--> [AdministratorPage]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				addFlights = new AddFlights(driver);
				addFlights.clickCancelButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [AdministratorPage] --add-flight--> [AddFlights]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				administratorPage = new AdministratorPage(driver);
				administratorPage.clickAddFlightButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [AddFlights] --next--> [InsertFlightsDetails]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				addFlights = new AddFlights(driver);
				addFlights.insertFlightInfo(outboundFlightNumber, outboundFlightDepartureDate,
						outboundFlightDepartureTime, outboundFlightDuration, outboundFlightPassengers,
						outboundFlightPricePerPerson, outboundFlightOriginCity, outboundFlightDestinationCity);
				addFlights.clickNextButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [InsertFlightsDetails] --cancel--> [AdministratorPage]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				insertFlightsDetails = new InsertFlightsDetails(driver);
				insertFlightsDetails.clickCancelButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [AdministratorPage] --add-flight--> [AddFlights]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				administratorPage = new AdministratorPage(driver);
				administratorPage.clickAddFlightButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [AddFlights] --next--> [InsertFlightsDetails]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				addFlights = new AddFlights(driver);
				addFlights.insertFlightInfo(outboundFlightNumber, outboundFlightDepartureDate,
						outboundFlightDepartureTime, outboundFlightDuration, outboundFlightPassengers,
						outboundFlightPricePerPerson, outboundFlightOriginCity, outboundFlightDestinationCity);
				addFlights.clickNextButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [InsertFlightsDetails] --save-and-add-new--> [AddFlights]
		insertFlightsDetails = new InsertFlightsDetails(driver);
		insertFlightsDetails.insertFlightDetails(outboundFlightRepetitions, outboundFlightReturn);
		insertFlightsDetails.clickSaveAndAddNewButton();

		// [AddFlights] --next--> [InsertFlightsDetails]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				addFlights = new AddFlights(driver);
				addFlights.insertFlightInfo(inboundFlightNumber, inboundFlightDepartureDate,
						inboundFlightDepartureTime, inboundFlightDuration, inboundFlightPassengers,
						inboundFlightPricePerPerson, inboundFlightOriginCity, inboundFlightDestinationCity);
				addFlights.clickNextButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [InsertFlightsDetails] --save--> [AdministratorPage]
		insertFlightsDetails = new InsertFlightsDetails(driver);
		insertFlightsDetails.insertFlightDetails(inboundFlightRepetitions, inboundFlightReturn);
		insertFlightsDetails.clickSaveButton();

		// [AdministratorPage] --view-flights--> [FlightsList]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				administratorPage = new AdministratorPage(driver);
				administratorPage.clickFlightsListButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [FlightsList] --search--> [FlightsList]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				flightsList = new FlightsList(driver);
				flightsList.search(outboundFlightDepartureDate, outboundFlightOriginCity,
						outboundFlightDestinationCity);
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [FlightsList] --view-details--> [FlightView]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				flightsList = new FlightsList(driver);
				flightsList.clickDetailsButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [FlightView] --return--> [FlightsList]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				flightView = new FlightView(driver);
				flightView.clickReturnButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [FlightsList] --search--> [FlightsList]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				flightsList = new FlightsList(driver);
				flightsList.search(inboundFlightDepartureDate, inboundFlightOriginCity, inboundFlightDestinationCity);
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [FlightsList] --delete-flight--> [DeleteFlightSuccess] --back-to-show-flights--> [FlightsList]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				flightsList = new FlightsList(driver);
				flightsList.clickDeleteButtonAndReturn();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
		});

		// [FlightsList] --back--> [AdministratorPage]
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				flightsList = new FlightsList(driver);
				flightsList.clickBackButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;

			@AfterServlet
			public void checkfinal() {
				assertTrue(true);
			}
		});
	}
}
