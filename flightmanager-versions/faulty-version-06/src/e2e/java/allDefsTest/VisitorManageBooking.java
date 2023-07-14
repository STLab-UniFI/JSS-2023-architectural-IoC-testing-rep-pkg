package allDefsTest;

//import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
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

import it.unifi.dinfo.stlab.flightmanager.poms.BookingEdit;
import it.unifi.dinfo.stlab.flightmanager.poms.BookingLogin;
import it.unifi.dinfo.stlab.flightmanager.poms.BookingView;
import it.unifi.dinfo.stlab.flightmanager.poms.Home;
import it.unifi.dinfo.stlab.flightmanager.util.BeanUtils;
import it.unifi.dinfo.stlab.flightmanager.util.H2Manager;
import it.unifi.ing.swam.model.Airport;
import it.unifi.ing.swam.model.Booking;
import it.unifi.ing.swam.model.Country;
import it.unifi.ing.swam.model.Flight;
import it.unifi.ing.swam.model.ModelFactory;
import it.unifi.ing.swam.model.Passenger;

@WarpTest
@RunWith(Arquillian.class)
@RunAsClient
public class VisitorManageBooking {
	
	private static final String WEBAPP_SRC = "src/main/webapp";
	
	private static EntityManagerFactory entityManagerFactory;
	private static EntityManager em;
	
	@ArquillianResource
	public static URL contextPath;
	
	@Drone
	static WebDriver driver;
	
	// pages
	private Home homePage;
	private BookingLogin bookingLogin;
	private BookingView bookingView;
	private BookingEdit bookingEdit;
	
	// assets
	private Airport pisaAirport;
	private Airport palermoAirport;
	private Flight pisaPalermoFlight;
	private Flight palermoPisaFlight;
	private Passenger fooPassenger;
	private Booking pisaPalermoBooking;
	private String resCode;
	private int resIndex;
	
	@Deployment(testable = true)
	public static WebArchive createDeployment() {
		
        File[] primefaces = Maven.resolver()
                .loadPomFromFile("pom.xml")
                .resolve("org.primefaces:primefaces:8.0")
                .withoutTransitivity()
                .asFile();
		
		WebArchive war = ShrinkWrap.create(WebArchive.class, "search.war").addPackages(true, "it.unifi.ing.swam")
				.addClass(BeanUtils.class)
				.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class).importDirectory(WEBAPP_SRC)
						.as(GenericArchive.class), "/", Filters.include(".*\\.xhtml$"))
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(new File(WEBAPP_SRC + "/WEB-INF/faces-config.xml"), "faces-config.xml")
	            .setWebXML(new File(WEBAPP_SRC,"WEB-INF/web.xml"))
				.addAsWebInfResource("arquillian-ds.xml")
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
		
		em.persist(italy);
		
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
		
		// flights creation
		pisaPalermoFlight = ModelFactory.flight();
		pisaPalermoFlight.setFlightNumber("Pi-Pa-1");
		pisaPalermoFlight.setDepartureDate(new Date());
		pisaPalermoFlight.setDepartureTime(new Date());
		pisaPalermoFlight.setTotalSeats(100);
		pisaPalermoFlight.setPricePerPerson(80);
		pisaPalermoFlight.setFlightDuration(120);
		pisaPalermoFlight.setSourceAirport(pisaAirport);
		pisaPalermoFlight.setDestinationAirport(palermoAirport);
		
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
		
		em.persist(pisaPalermoFlight);
		em.persist(palermoPisaFlight);

		// passengers/bookings creation
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
		
		// redirect to homepage
		Warp.initiate(new Activity() {
			
			@Override
			public void perform() {
				
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				driver.manage().timeouts().setScriptTimeout(100, TimeUnit.SECONDS);
				driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
				
				String url = contextPath.toString() + "index.xhtml";
				driver.get(url);
				
				homePage = new Home(driver);
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
		
		// 0 - 1
		Warp.initiate(new Activity() {
			
			@Override
			public void perform() {
				
				homePage = new Home(driver);
				homePage.clickSearchBookingButton();
			}
		}).inspect(new Inspection() {
			
			private static final long serialVersionUID = 1L;
		});
		
		// 1 - 2
		Warp.initiate(new Activity() {
			
			@Override
			public void perform() {
				
				bookingLogin = new BookingLogin(driver);
				bookingLogin.clickBackButton();
			}
		}).inspect(new Inspection() {
			
			private static final long serialVersionUID = 1L;
		});
		
		// 2 - 1
		Warp.initiate(new Activity() {
			
			@Override
			public void perform() {
				
				homePage = new Home(driver);
				homePage.clickSearchBookingButton();
			}
		}).inspect(new Inspection() {
			
			private static final long serialVersionUID = 1L;
		});
		
		// 1 - 3 - 4
		Warp.initiate(new Activity() {
			
			@Override
			public void perform() {
				
				bookingLogin = new BookingLogin(driver);
				bookingLogin.login(resCode + resIndex, pisaPalermoBooking.getEmail());
			}
		}).inspect(new Inspection() {
			
			private static final long serialVersionUID = 1L;
		});
		
		// 4 - 7
		Warp.initiate(new Activity() {
			
			@Override
			public void perform() {
				
				bookingView = new BookingView(driver);
				bookingView.goToBookingEdit();
			}
		}).inspect(new Inspection() {
			
			private static final long serialVersionUID = 1L;
		});
		
		// 7 - 8 - 9
		Warp.initiate(new Activity() {
			
			@Override
			public void perform() {
				
				bookingEdit = new BookingEdit(driver);
				bookingEdit.updateBooking("Bar", "Baer", "87654321", new Date(1000 * 3600 * 24));
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
		
		// 0 - 1
		Warp.initiate(new Activity() {
			
			@Override
			public void perform() {
				
				homePage = new Home(driver);
				homePage.clickSearchBookingButton();
			}
		}).inspect(new Inspection() {
			
			private static final long serialVersionUID = 1L;
		});
		
		// 1 - 3 - 4
		Warp.initiate(new Activity() {
			
			@Override
			public void perform() {
				
				bookingLogin = new BookingLogin(driver);
				bookingLogin.login(resCode + resIndex, pisaPalermoBooking.getEmail());
			}
		}).inspect(new Inspection() {
			
			private static final long serialVersionUID = 1L;
		});
		
		// 4 - 6 - 10 - 11
		Warp.initiate(new Activity() {
			
			@Override
			public void perform() {
				
				bookingView = new BookingView(driver);
				bookingView.deleteBooking();
				resIndex++;
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
