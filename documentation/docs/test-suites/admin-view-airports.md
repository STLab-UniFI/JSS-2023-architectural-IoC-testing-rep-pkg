# Admin View Airports Use Case

## Enriched Robustness Diagram

<p align="center">
  <img src="../../imgs/admin-view-airports-erd.png?raw=true" style="width:90%">
</p>

## Managed Components Data Flow Graph

<p align="center">
  <img src="../../imgs/admin-view-airports-mcdfg.png?raw=true" style="width:60%">
</p>

## Involved Components

- _AirportController_ _@ConversationScoped_
- _AirportDao_ _@RequestScoped_

## Coverage Criteria

### All Nodes

1. [0, 1, 2, 3, 0, 1, 2, 4, 5]

### All Edges

1. [0, 1, 2, 3, 0, 1, 2, 4, 5, 4, 5, 3]

### All Defs

1.	[0, 1, 2]

### All Uses

1.	[0, 1, 2, 3, 0, 1, 2, 4, 5]

### All DU Paths

1.	[0, 1, 2, 3, 0, 1, 2, 4, 5, 3]

