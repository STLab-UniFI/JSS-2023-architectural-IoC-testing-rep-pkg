package allEdgesTest;

import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
//import javax.servlet.http.HttpServletRequest;

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

import it.unifi.dinfo.stlab.flightmanager.poms.AdministratorPage;
import it.unifi.dinfo.stlab.flightmanager.poms.FlightView;
import it.unifi.dinfo.stlab.flightmanager.poms.FlightsList;
import it.unifi.dinfo.stlab.flightmanager.util.BeanUtils;
import it.unifi.dinfo.stlab.flightmanager.util.H2Manager;
import it.unifi.ing.swam.components.PasswordManagerComponent;
import it.unifi.ing.swam.controller.FlightController;
import it.unifi.ing.swam.controller.LoginController;
import it.unifi.ing.swam.model.Airport;
import it.unifi.ing.swam.model.Country;
import it.unifi.ing.swam.model.Flight;
import it.unifi.ing.swam.model.ModelFactory;
import it.unifi.ing.swam.model.User;
import it.unifi.ing.swam.model.UserRole;

@WarpTest
@RunWith(Arquillian.class)
@RunAsClient
public class AdminViewFlights {
	
	private static final String WEBAPP_SRC = "src/main/webapp";
	private static EntityManagerFactory entityManagerFactory;
	private static EntityManager em;
	
	@ArquillianResource
	public static URL contextPath;
	
	@Drone
	static WebDriver driver;
	
	// pages
	private AdministratorPage administratorPage;
	private FlightsList flightsList;
	private FlightView flightView;
	
	// credentials
	private String username = "admin";
	private String userPwd = "pass";
	
	// assets
	private Flight pisaPalermoFlight;
	private Flight palermoPisaFlight;
	private Airport pisaAirport;
	private Airport palermoAirport;
	
	// test data
	private Date startingDate;
	private String origin;
	private String destination;
	
	
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
		
		// standard admin creation
		User user = ModelFactory.user();
		user.setUserRole(UserRole.Admin);
		user.setUsername(username);
		user.setPassword(PasswordManagerComponent.encode(userPwd));
		user.setHomeCountry(italy);
		Calendar calendar = Calendar.getInstance();
		calendar.set(2017, Calendar.AUGUST, 30);
		user.setRegistrationDate(calendar.getTime());
		
		em.persist(user);
		
		// airports creation
		pisaAirport = ModelFactory.airport();
		pisaAirport.setAirportCountry(italy);
		pisaAirport.setCityName("Pisa");
		pisaAirport.setAirportFullName("Galileo Galilei");
		pisaAirport.setZIP(12345);
		pisaAirport.setGMT(1);
		
		em.persist(pisaAirport);
		
		palermoAirport = ModelFactory.airport();
		palermoAirport.setAirportCountry(italy);
		palermoAirport.setCityName("Palermo");
		palermoAirport.setAirportFullName("Palermo Airport");
		palermoAirport.setZIP(54321);
		palermoAirport.setGMT(1);
		
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
		
		em.getTransaction().commit();
		
		// flight data initialization
		startingDate = new Date();
		origin = pisaAirport.getCityName();
		destination = palermoAirport.getCityName();
		
		initializeBeansState();
		
		// redirect to administrator page
		Warp.initiate(new Activity() {
			
			@Override
			public void perform() {
				
				// test timeouts
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				driver.manage().timeouts().setScriptTimeout(100, TimeUnit.SECONDS);
				driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
				
				String url = contextPath.toString() + "administrator-page.xhtml";
				driver.get(url);
			}
		}).inspect(new Inspection() {
			
			private static final long serialVersionUID = 1L;
		});
	}
	
	@After
	public void tearDown() {
		
		// redirect to homepage
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
				
				// clean all active contexts
				BeanUtils helper = new BeanUtils(beanManager);
				helper.cleanContexts();
			}
		});
		
		em.clear();
		em.close();
	}
	
	@AfterClass
	public static void tearDownClass() {
		
		// clean up the database
		em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();
		
		H2Manager.deleteFromTables(em);
		
		em.getTransaction().commit();
		entityManagerFactory.close();
	}
	
	@Test
	public void test1() {
		
		// 0 - 1 - 2
		Warp.initiate(new Activity() {
			
			@Override
			public void perform() {
				
				administratorPage = new AdministratorPage(driver);
				administratorPage.clickFlightsListButton();
			}
		}).inspect(new Inspection() {
			
			private static final long serialVersionUID = 1L;
		});
		
		// 2 - 3 - 0
		Warp.initiate(new Activity() {
			
			@Override
			public void perform() {
				
				flightsList = new FlightsList(driver);
				flightsList.clickBackButton();
			}
		}).inspect(new Inspection() {
			
			private static final long serialVersionUID = 1L;

			@Inject
			FlightController flightController;

			@BeforeServlet
			public void checkAirports() {
				assertNotNull(flightController.getFlights());
			}
		});
		
		// 0 - 1 - 2
		Warp.initiate(new Activity() {
			
			@Override
			public void perform() {
				
				administratorPage = new AdministratorPage(driver);
				administratorPage.clickFlightsListButton();
			}
		}).inspect(new Inspection() {
			
			private static final long serialVersionUID = 1L;
		});
		
		// 2 - 4 - 2
		Warp.initiate(new Activity() {
			
			@Override
			public void perform() {
				
				flightsList = new FlightsList(driver);
				flightsList.search(startingDate, origin, destination);
			}
		}).inspect(new Inspection() {
			
			private static final long serialVersionUID = 1L;
		});
		
		// 2 - 5
		Warp.initiate(new Activity() {
			
			@Override
			public void perform() {
				
				flightsList = new FlightsList(driver);
				flightsList.clickDetailsButton();
			}
		}).inspect(new Inspection() {
			
			private static final long serialVersionUID = 1L;
		});
		
		// 5 - 6
		Warp.initiate(new Activity() {
			
			@Override
			public void perform() {
				
				flightView = new FlightView(driver);
				flightView.clickReturnButton();
			}
		}).inspect(new Inspection() {
			
			private static final long serialVersionUID = 1L;
		});
		
		// 6 - 5
		Warp.initiate(new Activity() {
			
			@Override
			public void perform() {
				
				flightsList = new FlightsList(driver);
				flightsList.clickDetailsButton();
			}
		}).inspect(new Inspection() {
			
			private static final long serialVersionUID = 1L;
		});
		
		// 5 - 6
		Warp.initiate(new Activity() {
			
			@Override
			public void perform() {
				
				flightView = new FlightView(driver);
				flightView.clickReturnButton();
			}
		}).inspect(new Inspection() {
			
			private static final long serialVersionUID = 1L;
		});
		
		// 6 - 4 - 2
		Warp.initiate(new Activity() {
			
			@Override
			public void perform() {
				
				flightsList = new FlightsList(driver);
				flightsList.search(startingDate, origin, destination);
			}
		}).inspect(new Inspection() {
			
			private static final long serialVersionUID = 1L;
		});
		
		// 2 - 5
		Warp.initiate(new Activity() {
			
			@Override
			public void perform() {
				
				flightsList = new FlightsList(driver);
				flightsList.clickDetailsButton();
			}
		}).inspect(new Inspection() {
			
			private static final long serialVersionUID = 1L;
		});
		
		// 5 - 6
		Warp.initiate(new Activity() {
			
			@Override
			public void perform() {
				
				flightView = new FlightView(driver);
				flightView.clickReturnButton();
			}
		}).inspect(new Inspection() {
			
			private static final long serialVersionUID = 1L;
		});
		
		// 6 - 3 - 0
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
	
	public void initializeBeansState() {
		
		// redirect to homepage
		Warp.initiate(new Activity() {
			
			@Override
			public void perform() {
				
				String url = contextPath.toString() + "index.xhtml";
				driver.get(url);
			}
		}).inspect(new Inspection() {
			
			private static final long serialVersionUID = 1L;
			
			@Inject
			LoginController logController;
			
			@BeforeServlet
			public void login() {
				
				// insert admin credentials and login
				logController.setUsername("admin");
				logController.setPassword("pass");
				logController.loginAsAdmin();
			}
		});
	}
}