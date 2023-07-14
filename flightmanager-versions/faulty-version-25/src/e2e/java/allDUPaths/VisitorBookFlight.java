package allDUPaths;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;
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

import it.unifi.dinfo.stlab.flightmanager.poms.BookingDetails;
import it.unifi.dinfo.stlab.flightmanager.poms.FlightDetails;
import it.unifi.dinfo.stlab.flightmanager.poms.FlightsResult;
import it.unifi.dinfo.stlab.flightmanager.poms.Home;
import it.unifi.dinfo.stlab.flightmanager.util.BeanUtils;
import it.unifi.dinfo.stlab.flightmanager.util.H2Manager;
import it.unifi.ing.swam.components.LoggedUserComponent;
import it.unifi.ing.swam.components.TemporaryReservationComponent;
import it.unifi.ing.swam.controller.booking.RegisteredBookingController;
import it.unifi.ing.swam.model.Airport;
import it.unifi.ing.swam.model.Country;
import it.unifi.ing.swam.model.Flight;
import it.unifi.ing.swam.model.ModelFactory;

@WarpTest
@RunWith(Arquillian.class)
@RunAsClient
public class VisitorBookFlight {
	
	//private static final int maxMinutesConversation = 15;
	
	//static final String packageName = "it.unifi.ing.swam.";
	//static final String managedBeanClassName = "class org.jboss.weld.bean.ManagedBean";
	
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
	
	// user credentials
	//private String username = "userName";
	//private String userPwd = "userpwd";
	
	// assets
	private Flight pisaPalermoFlight;
	private Airport pisaAirport;
	private Airport palermoAirport;

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
		/*User user = ModelFactory.user();
		user.setUserRole(UserRole.Registered);
		user.setUsername(username);
		user.setPassword(PasswordManagerComponent.encode(userPwd));
		user.setHomeCountry(italy);
		Calendar calendar = Calendar.getInstance();
		calendar.set(2017, Calendar.AUGUST, 30);
		user.setRegistrationDate(calendar.getTime());*/

		em.persist(italy);
		//em.persist(user);

		// XXX useless to the tests ends
		/*Booking booking;
		for (int i = 0; i < 9; i++) {
			booking = new Booking();
			booking.setRegisteredUserOwner(user);
			em.persist(booking);
		}*/

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

		em.getTransaction().commit();
		
		 initializeBeansState();
		
		// redirect to BookingDetails page and initialize controller
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				driver.manage().timeouts().setScriptTimeout(100, TimeUnit.SECONDS);
				driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
				
				/* TODO: start at home, search, and confirm a flight
				 * REASON: a visitor user can't go directly to booking-details
				*/
				/*String url = contextPath.toString() + "booking-details.xhtml";
				driver.get(url);*/

				String url = contextPath.toString() + "index.xhtml";
				driver.get(url);
				
				homePage = new Home(driver);
				Date date = new Date();
				homePage.searchFlights(pisaAirport.getCityName(), palermoAirport.getCityName(), date, "1");
				
				flightResultsPage = new FlightsResult(driver);
				flightResultsPage.clickConfirmButton();
				
				/*int flights = flightResultsPage.getTotalFlightVisualized();
				assertEquals(1, flights);*/
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
		
		// (0 - 1)

		// 1 - 2 - 3 - 6
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				bookingDetailsPage = new BookingDetails(driver);
				bookingDetailsPage.compileBookingDataFormAndConfirm(
						"user@mail.com", "userName", "userSurname", "1212", "18/08/1990");
				bookingDetailsPage.clickBackButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
			
			@Inject
			TemporaryReservationComponent temporaryReservationComponent;

			Long flightId = pisaPalermoFlight.getId();
			
			@BeforeServlet
			public void checkTRSBefore() {
				Flight mockedFlight = new Flight();
				mockedFlight.setId(flightId);
				assertEquals(1, temporaryReservationComponent.getTemporaryReservedSeats(mockedFlight));
			}
		});
		
		// This is just a random action to make an assertion after the previous action
		// This is workaround, the action is not needed.
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				String url = contextPath.toString() + "index.xhtml";
				driver.get(url);
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;

			@Inject
			TemporaryReservationComponent temporaryReservationComponent;

			Long flightId = pisaPalermoFlight.getId();

			@BeforeServlet
			public void checkTRSBefore() {
				Flight mockedFlight = new Flight();
				mockedFlight.setId(flightId);
				assertEquals(0, temporaryReservationComponent.getTemporaryReservedSeats(mockedFlight));
			}
		});
	}
	
	@Test
	public void test2() {
		
		// (0 - 1)
		
		// 1 - 4 - 5
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				bookingDetailsPage = new BookingDetails(driver);
				bookingDetailsPage.clickCancelButton();
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;
			
			@Inject
			TemporaryReservationComponent temporaryReservationComponent;

			Long flightId = pisaPalermoFlight.getId();
			
			@BeforeServlet
			public void checkTRSBefore() {
				Flight mockedFlight = new Flight();
				mockedFlight.setId(flightId);
				assertEquals(1, temporaryReservationComponent.getTemporaryReservedSeats(mockedFlight));
			}
		});
		
		// This is just a random action to make an assertion after the previous action
		// This is workaround, the action is not needed.
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				String url = contextPath.toString() + "index.xhtml";
				driver.get(url);
			}
		}).inspect(new Inspection() {
			private static final long serialVersionUID = 1L;

			@Inject
			TemporaryReservationComponent temporaryReservationComponent;

			Long flightId = pisaPalermoFlight.getId();

			@BeforeServlet
			public void checkTRSBefore() {
				Flight mockedFlight = new Flight();
				mockedFlight.setId(flightId);
				assertEquals(0, temporaryReservationComponent.getTemporaryReservedSeats(mockedFlight));
			}
		});
	}
	
	public void initializeBeansState() {
		
		Warp.initiate(new Activity() {
			@Override
			public void perform() {
				String url = contextPath.toString() + "index.xhtml";
				driver.get(url);
			}
		}).inspect(new Inspection() {
			
			private static final long serialVersionUID = 1L;
			
			@Inject
			RegisteredBookingController bookingController;
			
			Long flightId = pisaPalermoFlight.getId();

			@AfterServlet
			public void bookingControllerInit() {
				bookingController.setFlightOut(flightId);
				bookingController.setnPassengers(1);
			}
		});
	}
}
