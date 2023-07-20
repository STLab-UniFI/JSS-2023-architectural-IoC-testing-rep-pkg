# Admin View Flights Use Case

## Enriched Robustness Diagram

<p align="center">
  <img src="../../imgs/admin-view-flights-erd.png?raw=true" style="width:90%">
</p>

## Managed Components Data Flow Graph

<p align="center">
  <img src="../../imgs/admin-view-flights-mcdfg.png?raw=true" style="width:60%">
</p>

## Involved Components

- _AirportController_ _@ConversationScoped_
- _AirportDao_ _@RequestScoped_
- _FlightDao_ _@RequestScoped_

## Coverage Criteria

### All Nodes

1. [0, 1, 2, 3, 0, 1, 2, 4, 2, 5, 6]

### All Edges

1. [0, 1, 2, 3, 0, 1, 2, 4, 2, 5, 6, 5, 6, 4, 2, 5, 6, 3]

### All Defs

1.	[0, 1, 2, 4, 2]

### All Uses

1.	[0, 1, 2, 3, 0, 1, 2, 4, 2, 3, 0, 1, 2, 5, 6]

### All DU Paths

1.	[0, 1, 2, 3, 0, 1, 2, 5, 6, 3, 0, 1, 2, 4, 2, 3, 0, 1, 2, 5, 6, 4]

  
