# Registered Book Flight Use Case

## Enriched Robustness Diagram

<p align="center">
  <img src="../../imgs/registered-book-flight-erd.png?raw=true" style="width:90%">
</p>

## Managed Components Data Flow Graph

<p align="center">
  <img src="../../imgs/registered-book-flight-mcdfg.png?raw=true" style="width:60%">
</p>

## Involved Components

- _TemporaryReservationRepository_ _@ApplicationScoped_
- _TemporaryReservationComponent_ _@SessionScoped_
- _RegisteredBookingController_ _@SessionScoped_
- _BillingComponent_ _@SessionScoped_
- _LoggedUserComponent_ _@SessionScoped_
- _FlightDao_ _@RequestScoped_
- _DiscounterComponent_ _@RequestScoped_
- _BookingDao_ _@RequestScoped_

## Coverage Criteria

### All Nodes

1. [0, 1, 3]
2. [0, 2]

### All Edges

1. [0, 1, 3]
2. [0, 2]

### All Defs

1. [0, 1]

### All Uses

1. [0, 1, 3]
2. [0, 2]

### All DU Paths

1. [0, 1, 3]
2. [0, 2]
