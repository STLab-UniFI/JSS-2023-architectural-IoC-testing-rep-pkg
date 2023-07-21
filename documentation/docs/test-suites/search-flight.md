# Search Flight Use Case

## Enriched Robustness Diagram

<p align="center">
  <img src="../../imgs/search-flight-erd.png?raw=true" style="width:90%">
</p>

## Managed Components Data Flow Graph

<p align="center">
  <img src="../../imgs/search-flight-mcdfg.png?raw=true" style="width:60%">
</p>

## Involved Components

- _TemporaryReservationRepository_ _@ApplicationScoped_
- _TemporaryReservationComponent_ _@SessionScoped_
- _SearchFlightsController_ _@SessionScoped_
- _LoggedUserComponent_ _@SessionScoped_
- _BillingComponent_ _@SessionScoped_
- _FlightManagerComponent_ _@ConversationScoped_
- _FlightDao_ _@RequestScoped_
- _CountryDao_ _@RequestScoped_

## Coverage Criteria

### All Nodes

1. [0, 1, 11, 2, 7, 9, 10, 11, 2, 7, 8, 2, 3, 4, 1, 11, 2, 5, 6]

### All Edges

1. [0, 1, 11, 2, 7, 9, 10, 11, 2, 7, 8, 2, 5, 6]
2. [0, 1, 11, 2, 3, 4, 1, 11, 2, 7, 5, 6]

### All Defs

1. [0, 1, 11, 2, 7, 9, 10, 11]

### All Uses

1. [0, 1, 11, 2, 3]
2. [0, 1, 11, 2, 5]
3. [0, 1, 11, 2, 7, 8]
4. [0, 1, 11, 2, 7, 9]

### All DU Paths

1. [0, 1, 11, 2, 3]
2. [0, 1, 11, 2, 5]
3. [0, 1, 11, 2, 7, 5]
4. [0, 1, 11, 2, 7, 8]
5. [0, 1, 11, 2, 7, 9, 10, 11, 2, 7]

