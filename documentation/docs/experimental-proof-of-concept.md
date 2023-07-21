# Experimental Proof of Concept Documentation

This file describes how test suites are generated, based on both the _managed component Data Flow Graph_ (mcDFG) and the _Page Navigation Diagram_ (PND). Additionally, it provides the fault detection capabilities of the test suites obtained on the 32 faulty versions of the _Flight Manager_ web application.

## Test Case Generation Based on the managed component Data Flow Graph

<p align="center">
  <img src="../imgs/uc-diagram-user.png?raw=true" style="width:100%">
  <p align="center">
  <em>Fig. 1 Use Case Diagram for Regular Users </em>
    </p>
</p>

<p align="center">
  <img src="../imgs/uc-diagram-admin.png?raw=true" style="width:70%">
  <p align="center">
  <em>Fig. 2 Use Case Diagram for Administrator Users </em>
    </p>
</p>

For the sake of conciseness, documentation of each use case including _Enriched Robustness Diagrams_,  _managed components Data Flow Graph_ and corresponding test cases, is collected in separated pages listed below. 
- [Search Flight](test-suites/search-flight.md)
- [Visitor Book Flight](test-suites/visitor-book-flight.md)
- [Registered Book Flight](test-suites/registered-book-flight.md)
- [Visitor Search and Book Flight](test-suites/visitor-search-book-flight.md)
- [Registered Search and Book Flight](test-suites/registered-search-book-flight.md)
- [Visitor Manage Booking](test-suites/visitor-manage-booking.md)
- [Registered Manage Booking](test-suites/registered-manage-booking.md)
- [Admin Add Airport](test-suites/admin-add-airport.md)
- [Admin View Airports](test-suites/admin-view-airports.md)
- [Admin Add Flight](test-suites/admin-add-flight.md)
- [Admin View Flights](test-suites/admin-view-flights.md)

### Faulty Versions

This repository contains 32+1 versions of _Flight Manager_. The original version does not have any manually injected faults while the other 32 versions each have a single manually injected fault belonging to the fault model identified in the reference paper.

The following Table summarizes the type of fault injected in each branch following the terminology of the reference paper, elements in the first column can be clicked to obtain additional documentation about the specific faulty version and the fault detection capability of each test suite.  

| Faulty Version                   | Fault Type                               | Use Case              | Failure Mode                  |
|----------------------------------|------------------------------------------|-----------------------|-------------------------------|
| [1](faulty-versions/1.md)   | ErroneousEnclosingBoundary               | Search Flight         | Vanishing Component           |
| [2](faulty-versions/2.md)   | MissingStateClearance                    | Book Flight           | Zombie Component              |
| [3](faulty-versions/3.md)   | MissingStateClearance                    | Book Flight           | Zombie Component              |
| [4](faulty-versions/4.md)   | MissingStateClearance (WrongConformance) | Search Flight         | Unexpected Shared Component   |
| [5](faulty-versions/5.md)   | ErroneousDynamicInjection                | Book Flight           | Unexpected Injected Component |
| [6](faulty-versions/6.md)   | ShorterScope                             | Book Flight           | Vanishing Component           |
| [7](faulty-versions/7.md)   | WrongConformance                         | Book Flight           | Vanishing Component           |
| [8](faulty-versions/8.md)   | ErroneousEnclosingBoundary               | Search Flight         | Vanishing Component           |
| [9](faulty-versions/9.md)   | ErroneousEnclosingBoundary               | Search Flight         | Vanishing Component           |
| [10](faulty-versions/10.md) | ErroneousEnclosingBoundary               | Search Flight         | Zombie Component              |
| [11](faulty-versions/11.md) | ShorterScope                             | Book Flight           | Vanishing Component           |
| [12](faulty-versions/12.md) | LongerScope                              | Add Airport           | Zombie Component              |
| [13](faulty-versions/13.md) | ShorterScope                             | Add Airport           | Vanishing Component           |
| [14](faulty-versions/14.md) | ErroneousEnclosingBoundary               | Add Airport           | Vanishing Component           |
| [15](faulty-versions/15.md) | ErroneousEnclosingBoundary               | Add Airport           | Zombie Component              |
| [16](faulty-versions/16.md) | ErroneousEnclosingBoundary               | Add Airport           | Zombie Component              |
| [17](faulty-versions/17.md) | ErroneousEnclosingBoundary               | View Airports         | Vanishing Component           |
| [18](faulty-versions/18.md) | ShorterScope                             | View Airports         | Vanishing Component           |
| [19](faulty-versions/19.md) | ErroneousEnclosingBoundary               | View Flights          | Vanishing Component           |
| [20](faulty-versions/20.md) | ShorterScope                             | View Flights          | Vanishing Component           |
| [21](faulty-versions/21.md) | LongerScope                              | Add Flight            | Zombie Component              |
| [22](faulty-versions/22.md) | ShorterScope                             | Add Flight            | Vanishing Component           |
| [23](faulty-versions/23.md) | ErroneousEnclosingBoundary               | Add Flight            | Vanishing Component           |
| [24](faulty-versions/24.md) | ErroneousEnclosingBoundary               | Add Flight            | Zombie Component              |
| [25](faulty-versions/25.md) | ErroneousEnclosingBoundary               | Add Flight            | Zombie Component              |
| [26](faulty-versions/26.md) | LongerScope (MissingStateClearance)      | Book Flight           | Zombie Component              |
| [27](faulty-versions/27.md) | ErroneousEnclosingBoundary               | Book Flight           | Zombie Component              |
| [28](faulty-versions/28.md) | WrongConformance (MissingStateClearance) | Book Flight           | Zombie Component              |
| [29](faulty-versions/29.md) | MissingStateClearance                    | Book Flight           | Zombie Component              |
| [30](faulty-versions/30.md) | ErroneousEnclosingBoundary               | Book Flight           | Zombie Component              |
| [31](faulty-versions/31.md) | ShorterScope                             | Manage Booking        | Vanishing Component           |
| [32](faulty-versions/32.md) | ErroneousEnclosingBoundary               | Manage Booking        | Zombie Component              |

## Test Case Generation Based on the Page Navigation Diagram

<p align="center">
  <img src="../imgs/page-navigation-diagram-enlarged.png?raw=true" style="width:100%">
  <p align="center">
  <em>Fig. 3 Page Navigation Diagram </em>
    </p>
</p>

To further evaluate the proposed methodology, we compared the fault detection capability obtained with the _mcDFG_ against coverage criteria that can be implemented in end-to-end functional testing directly based on the page navigation diagram (_PND_, see Fig. 3).

Specifically, we considered _All Pages_ coverage, which requires that each  reachable page is visited at least once, and _All Navigation_ coverage, which verifies that each navigation (i.e., each edge of the page navigation diagram) is traversed at least once.

You can find test suites and fault detection capabilities at [this page](test-suites/page-navigation-diagram.md).

## Collection of Issues Related to IoC containers Configurations

The following collection represents a subset of the available issues encountered by users using frameworks for dependency injection and automatic contexts management. The collection does not aim to contain all existing cases of the problem but to demonstrate the wide range of possible pitfalls that a programmer might face.

## Collection of Issues Related to IoC containers Configurations

The following collection represents a subset of the available issues encountered by users using frameworks for dependency injection and automatic contexts management. The collection does not aim to contain all existing cases of the problem but to demonstrate the wide range of possible pitfalls that a programmer might face.

Queries used for StackOverflow: `[spring] or [cdi] or [bean] title:bean title:conversation/title:session answers:3.. score:+1..`

* https://stackoverflow.com/questions/23510857/how-end-one-cdi-conversation-and-completely-destroy-all-variable-of-cdi-bean
* https://stackoverflow.com/questions/9572418/new-cdi-conversation
* http://stackoverflow.com/questions/29280296/cdi-conversationscope-end-and-begin-with-one-request
* https://stackoverflow.com/questions/10300603/error-in-jsf-cdi-conversation-scope-when-begin-is-called-two-times/37879053
* https://stackoverflow.com/questions/13906167/cdi-conversation-id-is-always-1-and-nonexistentconversationexception-caught-when
* https://stackoverflow.com/questions/36609316/why-my-spring-session-scoped-bean-is-shared-across-sessions
* https://stackoverflow.com/questions/38661685/sessionscoped-cdi-bean-is-a-different-instance-when-injected
* https://stackoverflow.com/questions/11368662/injecting-a-sessionscoped-stateful-bean-in-entitylistener
* https://stackoverflow.com/questions/41511511/unsatisfieddependencyexception-error-creating-bean-with-name
* https://stackoverflow.com/questions/47529691/error-while-creating-bean-even-though-the-dependency-bean-is-there
* https://stackoverflow.com/questions/28042426/spring-boot-error-creating-bean-with-name-datasource-defined-in-class-path-r
* https://stackoverflow.com/questions/40058001/error-creating-bean-with-name-entitymanagerfactory-defined-in-class-path-resou
* https://github.com/xpoft/spring-vaadin/issues/10
* https://github.com/vaadin/flow/issues/5229
* https://github.com/vaadin/spring/issues/288


