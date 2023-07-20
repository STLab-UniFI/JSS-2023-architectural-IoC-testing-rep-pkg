# Registered Manage Booking Use Case

## Enriched Robustness Diagram

<p align="center">
  <img src="../../imgs/registered-manage-booking-erd.png?raw=true" style="width:90%">
</p>

## Managed Components Data Flow Graph

<p align="center">
  <img src="../../imgs/registered-manage-booking-mcdfg.png?raw=true" style="width:60%">
</p>

## Involved Components

- _LoggedUserComponent_ _@SessionScoped_
- _BookingListComponent_ _@SessionScoped_
- _BookingViewController_ _@ConversationScoped_
- _BookingDao_ _@RequestScoped_
- _FlightDao_ _@RequestScoped_
- _EditBookingController_ _@RequestScoped_
- _RouterComponent_ _@RequestScoped_

## Coverage Criteria

### All Nodes

1. [0, 1, 2, 3, 4, 5, 1, 2, 3, 6, 2, 3, 7, 11, 2, 3, 8, 9, 10]

### All Edges

1. [0, 1, 2, 3, 4, 5, 1, 2, 3, 6, 2, 3, 7, 11, 2, 3, 8, 9, 10, 8, 10, 7, 11, 2, 3, 8, 10, 6, 2, 3, 8, 10, 4, 5, 1, 2, 4]

### All Defs

1.	[0, 1, 2, 3, 7, 11, 2, 3, 8, 9, 10, 6]

### All Uses

1.	[0, 1, 2, 3, 7, 11, 2, 4, 5, 1, 2, 3, 4, 5, 1, 2, 4, 5, 1, 2, 3, 8, 9, 10, 6]

### All DU Paths

1.	[0, 1, 2, 3, 4, 5, 1, 2, 3, 8, 9, 10, 4, 5, 1, 2, 3, 8, 10, 4, 5, 1, 2, 4, 5, 1, 2, 3, 6]
2.	[0, 1, 2, 3, 7, 11]
2.	[0, 1, 2, 3, 8, 9, 10, 7, 11]
3.	[0, 1, 2, 3, 8, 10, 7, 11]
