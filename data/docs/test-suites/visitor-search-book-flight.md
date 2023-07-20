# Visitor Search and Book Flight Use Case

This use case represent the union of the _Search Flight_ use case with the _Visitor Book Flight_ use case.

## Enriched Robustness Diagram

<p align="center">
  <img src="../../imgs/visitor-search-book-flight-erd.png?raw=true" style="width:90%">
</p>

## Managed Components Data Flow Graph

The mcDFG can also be generated manually with a simple merge of the _Search Flight_ mcDFG with the _Visitor Book Flight_ mcDFG, under the assumption that all _@ApplicationScoped_ and _@SessionScoped_ components are defined at the starting node.

<p align="center">
  <img src="../../imgs/visitor-search-book-flight-mcdfg.png?raw=true" style="width:60%">
</p>

## Involved Components

- _TemporaryReservationRepository_ _@ApplicationScoped_
- _SearchFlightsController_ _@SessionScoped_
- _FlightManagerComponent_ _@ConversationScoped_
- _FlightDao_ _@RequestScoped_
- _TemporaryReservationComponent_ _@Dependent_
- _TemporaryReservationRepository_ _@ApplicationScoped_
- _LoggedUserComponent_ _@SessionScoped_
- _BillingComponent_ _@SessionScoped_
- _CountryDao_ _@RequestScoped_
- _VisitorBookingController_ _@ConversationScoped_
- _DiscounterComponent_ _@RequestScoped_
- _BookingDao_ _@RequestScoped_
- _RouterComponent_ _@RequestScoped_


## Coverage Criteria

### All Nodes

1. [0, 1, 16, 2, 3, 4, 1, 16, 2, 5, 6, 8, 9, 10, 15, 1, 16, 2, 5, 6, 8, 11, 17, 4, 1, 16, 2, 7, 12, 2, 7, 13, 14]

### All Edges

1. [0, 1, 16, 2, 3, 4, 1, 16, 2, 5, 6, 8, 9, 10, 15, 1, 16, 2, 5, 6, 8, 11, 17, 4, 1, 16, 2, 7, 12, 2, 7, 13, 14, 16, 2, 7, 5]

### All Defs
1. [0, 1, 16, 2, 7, 13, 14, 16, 2, 5, 6, 8, 9, 10, 15]  

### All Uses

1.	[0, 1, 16, 2, 3]  
2.	[0, 1, 16, 2, 5, 6, 8, 9, 10, 15]  
3.	[0, 1, 16, 2, 5, 6, 8, 11, 17]  
4.	[0, 1, 16, 2, 7, 12]  
5.	[0, 1, 16, 2, 7, 13]  

### All DU Paths

1.	[0, 1, 16, 2, 3]  
2.	[0, 1, 16, 2, 5, 6, 8, 9, 10, 15]  
3.	[0, 1, 16, 2, 5, 6, 8, 11, 17]  
4.	[0, 1, 16, 2, 7, 5, 6, 8, 9, 10]  
5.	[0, 1, 16, 2, 7, 5, 6, 8, 11, 17]  
6.	[0, 1, 16, 2, 7, 12]  
7.	[0, 1, 16, 2, 7, 13, 14, 16, 2, 7]  

