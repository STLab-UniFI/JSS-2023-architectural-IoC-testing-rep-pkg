# Admin Add Flight Use Case

## Enriched Robustness Diagram

<p align="center">
  <img src="../../imgs/admin-add-flight-erd.png?raw=true" style="width:90%">
</p>

## Managed Components Data Flow Graph

<p align="center">
  <img src="../../imgs/admin-add-flight-mcdfg.png?raw=true" style="width:60%">
</p>

## Involved Components

- _FlightController_ _@ConversationScoped_
- _AirportDao_ _@RequestScoped_
- _FlightDao_ _@RequestScoped_

## Coverage Criteria

### All Nodes

1. [0, 1, 2, 3, 0, 1, 2, 4, 5, 6, 4, 7]

### All Edges

1. [0, 1, 2, 3, 0, 1, 2, 4, 5, 6, 4, 5, 6, 3, 0, 1, 2, 4, 3, 0, 1, 2, 4, 7, 0]

### All Defs

1. [0, 1, 2, 4, 5, 6, 3, 0, 1, 2, 4, 7]

### All Uses

1. [0, 1, 2, 4, 5, 6, 3, 0, 1, 2, 4, 7]

### All DU Paths

1. [0, 1, 2, 3, 0, 1, 2, 4, 3, 0, 1, 2, 4, 5, 6, 4, 5, 6, 3, 0, 1, 2, 4, 7, 0, 1, 2, 4, 5, 6, 4, 7]

