# Admin Add Airport Use Case

## Enriched Robustness Diagram

<p align="center">
  <img src="../../imgs/admin-add-airport-erd.png?raw=true" style="width:90%">
</p>

## Managed Components Data Flow Graph

<p align="center">
  <img src="../../imgs/admin-add-airport-mcdfg.png?raw=true" style="width:60%">
</p>

## Involved Components

- _AirportController_ _@ConversationScoped_
- _AirportDao_ _@RequestScoped_

## Coverage Criteria

### All Nodes

1. [0, 1, 2, 3, 0, 1, 2, 4, 5, 2, 4, 6, 7, 2, 4, 8]

### All Edges

1. [0, 1, 2, 3, 0, 1, 2, 4, 5, 2, 4, 6, 7, 2, 4, 8, 0, 1, 2, 4, 3]

### All Defs

1. [0, 1, 2, 4, 6, 7, 2, 3]

### All Uses

1. [0, 1, 2, 3, 0, 1, 2, 4, 3, 0, 1, 2, 4, 5, 2, 3, 0, 1, 2, 4, 8, 0, 1, 2, 4, 6]

### All DU Paths

1. [0, 1, 2, 3, 0, 1, 2, 4, 6, 7, 2, 3, 0, 1, 2, 4, 3, 0, 1, 2, 4, 6, 7, 2, 4, 3, 0, 1, 2, 4, 5, 2, 4, 6, 7, 2, 4, 5, 2, 3, 0, 1, 2, 4, 8, 0, 1, 2, 4, 6, 7, 2, 4, 8]

