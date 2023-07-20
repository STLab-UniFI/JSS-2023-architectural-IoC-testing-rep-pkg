# Visitor Manage Booking Use Case

## Enriched Robustness Diagram

<p align="center">
  <img src="../../imgs/visitor-manage-booking-erd.png?raw=true" style="width:90%">
</p>

## Managed Components Data Flow Graph

<p align="center">
  <img src="../../imgs/visitor-manage-booking-mcdfg.png?raw=true" style="width:60%">
</p>

## Involved Components

- _RouterComponent_ _@RequestScoped_
- _BookingViewController_ _@ConversationScoped_
- _BookingDao_ _@RequestScoped_
- _EditBookingController_ _@RequestScoped_
- _FlightDao_ _@RequestScoped_
- _LoggedUserComponent_ _@SessionScoped_


## Coverage Criteria

### All Nodes

1. [0, 1, 2, 1, 3, 4, 5, 11, 1, 3, 4, 6, 10, 11, 1, 3, 4, 7, 8, 9]

### All Edges

1. [0, 1, 2, 1, 3, 4, 5, 11, 1, 3, 4, 6, 10, 11, 1, 3, 4, 7, 8, 9, 7, 9, 6, 10, 11, 1, 3, 4, 7, 9, 5]

### All Defs

1.	[0, 1, 2, 1, 3, 7, 8, 9]
2.	[0, 1, 3, 4, 6]

### All Uses

1.	[0, 1, 2, 1, 3, 4, 5, 11, 1, 3, 4, 7, 8, 9]
2.	[0, 1, 3, 4, 6, 10]

### All DU Paths

1.	[0, 1, 2, 1, 3, 4, 5, 11, 1, 3, 4, 6, 10, 11, 1, 3, 4, 7, 8, 9, 5, 11, 1, 3, 4, 7, 9, 5]
2.	[0, 1, 3, 4, 6, 10]
3.	[0, 1, 3, 4, 7, 8, 9, 6, 10]
4.	[0, 1, 3, 4, 7, 9, 6, 10]

