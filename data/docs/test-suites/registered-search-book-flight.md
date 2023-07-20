# Registered Search and Book Flight Use Case

This use case represent the union of the _Search Flight_ use case with the _Registered Book Flight_ use case.

## Enriched Robustness Diagram

<p align="center">
  <img src="../../imgs/registered-search-book-flight-erd.png?raw=true" style="width:90%">
</p>

## Managed Components Data Flow Graph

The mcDFG can also be generated manually with a simple merge of the _Search Flight_ mcDFG with the _Book Flight_ mcDFG, under the assumption that all _@ApplicationScoped_ and _@SessionScoped_ components are defined at the starting node.

<p align="center">
  <img src="../../imgs/registered-search-book-flight-mcdfg.png?raw=true" style="width:60%">
</p>

## Involved Components

- _TemporaryReservationRepository_ _@ApplicationScoped_
- _TemporaryReservationComponent_ _@Dependent_
- _SearchFlightController_ _@SessionScoped_
- _RegisteredBookingController_ _@SessionScoped_
- _LoggedUserComponent_ _@SessionScoped_
- _BillingComponent_ _@SessionScoped_
- _FlightManagerComponent_ _@ConversationScoped_
- _FlightDao_ _@RequestScoped_
- _CountryDao_ _@RequestScoped_
- _DiscounterComponent_ _@RequestScoped_
- _BookingDao_ _@RequestScoped_

## Coverage Criteria

### All Nodes

1. [0, 1, 14, 2, 3, 4, 1, 14, 2, 5, 6, 8, 13, 1, 14, 2, 5, 6, 9, 1, 14, 2, 7, 10, 2, 7, 11, 12]

### All Edges

1. [0, 1, 14, 2, 3, 4, 1, 14, 2, 5, 6, 8, 13, 1, 14, 2, 5, 6, 9, 1, 14, 2, 7, 10, 2, 7, 11, 12, 14, 2, 7, 5]

### All Defs

1. [0, 1, 14, 2, 5, 6, 8]
2. [0, 1, 14, 2, 7, 11, 12, 14]

### All Uses

1. [0, 1, 14, 2, 3]
2. [0, 1, 14, 2, 5, 6, 8, 13]
3. [0, 1, 14, 2, 5, 6, 9]
4. [0, 1, 14, 2, 7, 10]
5. [0, 1, 14, 2, 7, 11, 12, 14]

### All DU Paths

1. [0, 1, 14, 2, 3]
2. [0, 1, 14, 2, 5, 6, 8, 13]
3. [0, 1, 14, 2, 7, 5, 6, 8, 13]
4. [0, 1, 14, 2, 5, 6, 9]
5. [0, 1, 14, 2, 7, 5, 6, 9]
6. [0, 1, 14, 2, 7, 10]
7. [0, 1, 14, 2, 7, 11, 12, 14, 2, 7]
