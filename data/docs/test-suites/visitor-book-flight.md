# Visitor Book Flight Use Case

## Enriched Robustness Diagram

<p align="center">
  <img src="../../imgs/visitor-book-flight-erd.png?raw=true" style="width:90%">
</p>

## Managed Components Data Flow Graph

<p align="center">
  <img src="../../imgs/visitor-book-flight-mcdfg.png?raw=true" style="width:60%">
</p>

## Involved Components

- _VisitorBookingController_		_@ConversationScoped_
- _FlightDao_						_@RequestScoped_
- _BillingComponent_				_@SessionScoped_
- _DiscounterComponent_				_@RequestScoped_
- _LoggedUserComponent_				_@SessionScoped_
- _TemporaryReservationComponent_	_@Dependent_
- _TemporaryReservationRepository_	_@ApplicationScoped_
- _BookingDao_						_@RequestScoped_
- _RouterComponent_					_@RequestScoped_

## Coverage Criteria

### All Nodes

1.  [0, 1, 2, 3, 6]
2.  [0, 1, 4, 5]
		
### All Edges

1.  [0, 1, 2, 3, 6]
2.  [0, 1, 4, 5]

### All Defs

1.  [0, 1, 2, 3, 6]

### All Uses
	
1.  [0, 1, 2, 3, 6]
2.  [0, 1, 4, 5]

### All DU Paths
	
1.  [0, 1, 2, 3, 6]
2.  [0, 1, 4, 5]
