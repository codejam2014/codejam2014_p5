package jam.controllers;

import hello.Greeting;
import jam.beans.Driver;
import jam.beans.Location;
import jam.beans.LocationInstance;
import jam.beans.Route;
import jam.beans.Shuttle;
import jam.beans.Status;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShuttleController implements ErrorController{

  private static final String template = "Hello, %s!";
  private final AtomicLong counter = new AtomicLong();
  private static HashMap<String, Shuttle> sbShuttles = new HashMap<String, Shuttle>();

  // private static List<LocationInstance> latestLocation = new ArrayList<LocationInstance>();

  @PostConstruct
  private void loadData() {
    Location l1_5simmonds = new Location(-26.211042, 28.039661);
    Location l2_commisioner = new Location(-26.205892, 28.036615);
    Location l3_rosebank = new Location(-26.147374, 28.043929);
    Location l4_station = new Location(-26.194911, 28.036137);

    Route r1 = new Route("1", "No.5 to Gautrain", l1_5simmonds, l4_station);
    Route r2= new Route("2", "No.5 to Commissioner", l1_5simmonds, l2_commisioner);
    Route r3 = new Route("3", "No.5 to Rosebank", l1_5simmonds, l3_rosebank);

    Driver d1 = new Driver("1", "Naruto", "Usumaki");
    Driver d2 = new Driver("2", "Ichigo", "Korusaki");
    Driver d3 = new Driver("3", "Luffy", "Monkey");
    Driver d4 = new Driver("4", "Grey", "Ice");
    Driver d5 = new Driver("4", "Bruce", "Banner");

    Shuttle s1 = new Shuttle("1", "Captain America", 30);
    Shuttle s2 = new Shuttle("2", "Hulk", 30);
    Shuttle s3 = new Shuttle("3", "Scarlett MMMmmm", 20);
    Shuttle s4 = new Shuttle("4", "Ironman", 20);
    Shuttle s5 = new Shuttle("5", "Agent Simmons", 50);

    // String driverId, Date time, Location location, String shuttleId, String routeId
    LocationInstance sp1 = new LocationInstance(d1.getId(), new Date(), l1_5simmonds, s1.getId(), r1.getId());
    LocationInstance sp2 = new LocationInstance(d2.getId(), new Date(), l1_5simmonds, s2.getId(), r2.getId());
    LocationInstance sp3 = new LocationInstance(d3.getId(), new Date(), l1_5simmonds, s3.getId(), r3.getId());
    LocationInstance sp4 = new LocationInstance(d4.getId(), new Date(), l1_5simmonds, s4.getId(), r1.getId());
    LocationInstance sp5 = new LocationInstance(d5.getId(), new Date(), l1_5simmonds, s5.getId(), r2.getId());

    s1.setLatestLocation(sp1);
    s2.setLatestLocation(sp2);
    s3.setLatestLocation(sp3);
    s4.setLatestLocation(sp4);
    s5.setLatestLocation(sp5);

    sbShuttles.put(s1.getId(), s1);
    sbShuttles.put(s2.getId(), s2);
    sbShuttles.put(s3.getId(), s3);
    sbShuttles.put(s4.getId(), s4);
    sbShuttles.put(s5.getId(), s5);

  }

  @RequestMapping("/greeting")
  public Greeting greeting(
      @RequestParam(value = "name", defaultValue = "World") String name) {
    return new Greeting(counter.incrementAndGet(), String.format(template,
        name));
  }

  @RequestMapping(value = "/locationUpdate", method = RequestMethod.POST)
  public Status updateLocation(LocationInstance locationInstance) {
    storeLocation(locationInstance);
    return new Status("You have 5min to reach No.5 - why are u at Nandoes?");
  }

  private void storeLocation(LocationInstance locationInstance) {
    Shuttle shuttle = sbShuttles.get(locationInstance.getShuttleId());
    shuttle.setLatestLocation(locationInstance);
    // also store it in DB - for historical tracking.
  }

  @RequestMapping(value = "/locations")
  public List<Shuttle> retrieveLocation(@RequestParam(value = "routeId", defaultValue="1") String routeId) {

    List<Shuttle> shuttles = new ArrayList<Shuttle>();
    for (Shuttle shuttle : sbShuttles.values()) {
      if (shuttle.getLatestLocation().getRouteId().equals(routeId)) {
        shuttles.add(shuttle);
      }
    }
    return shuttles;
  }
  
  @RequestMapping("/test")
  public Greeting test(
      @RequestParam(value = "name", defaultValue = "World") String name) {
    return new Greeting(counter.incrementAndGet(), String.format(template,
        name));
  }
  

  @Override
  public String getErrorPath() {
      return "/error";
  }
  
  
  
  
  
  

}
