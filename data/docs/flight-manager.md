# FlightManager

## An Exemplary Stateful Web Application

_Flight Manager_ is a mid-size stateful web application, developed at the _Software Technologies Lab_ of the University of Florence and it provides an exemplary implementation of a sound and widely adopted combination of architectural patterns.

The application structure implements a 3-tier stateful architecture, outlined in the UML Deployment Diagram of _Fig. 1_. 

<p align="center">
  <img src="../imgs/architecture-overview-final.png?raw=true" style="width:65%">
  <p align="center">
  <em>Fig. 1 Flight Manager Architecture </em>
    </p>
</p>

The Data Source is implemented by a Relational Database Management System (RDBMS), connected to the Domain Model (represented in the class diagram of _Fig. 2_) by a Data Access Layer, featuring a Data Access Object (DAO) for each main Entity (for a total of 6 DAO classes), which exploits  services of an Object Relational Mapping (ORM) framework. 

<p align="center">
  <img src="../imgs/entities-class-diagram.png?raw=true" style="width:90%">
  <p align="center">
  <em>Fig. 2 Class Diagram </em>
    </p>
</p>

The Presentation Layer is organised in a User Interface made of XHTML pages (represented in the page navigation diagram of _Fig. 3_) and a Business Logic Layer that features Controllers implementing the navigation logic and other components maintaining information accumulated along the user interaction (roughly, 30 classes).

<p align="center">
  <img src="../imgs/page-navigation-diagram-enlarged.png?raw=true" style="width:100%">
  <p align="center">
  <em>Fig. 3 Page Navigation Diagram </em>
    </p>
</p>


_Flight Manager_ is implemented adopting specifications of the Java/Jakarta Enterprise Edition (JEE) ecosystem.
In particular, ORM relies on the _Java Persistence API_ (JPA, here provided by Hibernate), and interface XHTML pages are based on _Java Server Faces_ (JSF). 
DI and Automated Contexts Management for objects running in the Business Logic layer is implemented using _Contexts and Dependency Injection_ (CDI, here provided by the Weld reference implementation).

## Use Cases

_Flight Manager_ features functions in the context of an online flight booking system supporting registered and unregistered users (Use Case Diagram of _Fig 5_) and also administrator user (Use Case Diagram of _Fig 5_).
For each use case a different test suites fulfilling different coverage criteria was developed on top of the related _managed components Data Flow Graphs_ obtained through the methodology depicted in "_Model-Based Testing of Web Applications with automated lifecycle management of injected components_" paper.


<p align="center">
  <img src="../imgs/uc-diagram-user.png?raw=true" style="width:100%">
  <p align="center">
  <em>Fig. 4 Use Case Diagram for Regular Users </em>
    </p>
</p>

<p align="center">
  <img src="../imgs/uc-diagram-admin.png?raw=true" style="width:70%">
  <p align="center">
  <em>Fig. 5 Use Case Diagram for Administrator Users </em>
    </p>
</p>


