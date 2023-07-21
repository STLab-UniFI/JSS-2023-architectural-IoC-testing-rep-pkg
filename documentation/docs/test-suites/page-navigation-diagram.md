# Test Suites based on the Page Navigation Diagram 

## Page Navigation Diagram
<p align="center">
  <img src="../../imgs/page-navigation-diagram-enlarged.png?raw=true" style="width:90%">
</p>

## Coverage Criteria

The navigation coverage criteria are shown below. For readability purposes, each first occurrence of the affected elements is highlighted in bold.

### All Pages

1. **[Home]** → search → **[FlightsResult]** → view details → **[FlightDetails]** → confirm → **[BookingDetails]** → confirm → **[Confirmation]** → back → [Home] → my booking → **[BookingLogin]** → enter → **[BookingView]** → edit passengers → **[BookingEdit]** → update → **[UpdatePassengerSuccess]** → return → [BookingView] → delete → **[DeleteBookingSuccess]** → return → [Home] → login → **[Login]** → login as user → [Home] → logged my bookings → **[BookingList]**
2. [Home] → login → [Login] → login as admin → **[AdministratorPage]** → view airports → **[AirportList]** → view details → **[AirportView]** → return → [AirportList] → back → [AdministratorPage] → add airport → **[AddAirportsMain]** → next → **[AddAirportsDetails]** → save → [AdministratorPage] → add flight → **[AddFlights]** → next → **[InsertFlightDetails]** → save → [AdministratorPage] → view flights → **[FlightsList]** → view details → **[FlightView]** → return → [FlightsList] → delete flight → **[DeleteFlightSuccess]** → back to show flights → [FlightsList] → back → [AdministratorPage] → view countries → **[CountryList]** → back → [AdministratorPage] → add country → **[AddCountry]**

### All Navigations

1. [Home] → **search** → [FlightsResult] → **cancel** → [Home] → search → [FlightsResult] → **select flight** → [BookingDetails] → **cancel** → [Home] → search → [FlightsResult] → **view details** → [FlightDetails] → **back** → [FlightsResult] → view details → [FlightDetails] → **new search** → [FlightsResult] → view details → [FlightDetails] → **confirm** → [BookingDetails] → **confirm** → [Confirmation] → **back** → [Home]

2. [Home] → **my booking** → [BookingLogin] → **back to home** → [Home] → my booking → [BookingLogin] → **enter** → [BookingView] → **back to home** → [Home] → my booking → [BookingLogin] → enter → [BookingView] → **edit passengers** → [BookingEdit] → **cancel** → [BookingView] → edit passengers → [BookingEdit] → **update** → [updatePassengersSuccess] → **return** → [BookingView] → **delete** → [DeleteBookingSuccess] → **return** → [Home] → **login** → [Login] → **login as user** → [Home] → **logged my bookings** → [BookingList] → **details** → [BookingView] → **my bookings** → [BookingList] → details → [BookingView] → delete → [DeleteBookingSuccess] → **return** → [BookingList] → **back to home** → [Home]

3. [Home] → login → [Login] → **login as admin** → [AdministratorPage] → **add country** → [AddCountry] → **cancel** → [AdministratorPage] → add country → [AddCountry] → **save** → [AdministratorPage] → **view countries** → [CountryList] → **back** → [AdministratorPage] → **add airport** → [AddAirportsMain] → **next** → [AddAirportsDetails] → **back** → [AddAirportsMain] → **cancel** → [AdministratorPage] → add airport → [AddAirportsMain] → next → [AddAirportsDetails] → **cancel** → [AdministratorPage] → add airport → [AddAirportsMain] → next → [AddAirportsDetails] → **save and add new** → [AddAirportsMain] → next → [AddAirportsDetails] → **save** → [AdministratorPage] → **view airports** → [AirportList] → **view details** → [AirportView] → **return** → [AirportList] → **back** → [AdministratorPage] → **add flight** → [AddFlights] → **cancel** → [AdministratorPage] → add flight → [AddFlights] → **next** → [InsertFlightsDetails] → **cancel** → [AdministratorPage] → add flight → [AddFlights] → next → [InsertFlightsDetails] → **save and add new** → [AddFlights] → next → [InsertFlightsDetails] → **save** → [AdministratorPage] → **view flights** → [FlightsList] → **search** → [FlightsList] → **view details** → [FlightView] → **return** → [FlightsList] → search → [FlightsList] → **delete flight** → [DeleteFlightSuccess] → **back to show flights** → [FlightsList] → **back** → [AdministratorPage]

## Fault Detection Capability

| Faulty Version                     | All Pages             | All Navigations         |
| ---------------------------------- | --------------------- | ----------------------- |
| [1](../faulty-versions/1.md)       | :x:                   | :heavy_check_mark:      |
| [2](../faulty-versions/2.md)       | :x:                   | :x:                     |
| [3](../faulty-versions/3.md)       | :x:                   | :x:                     |
| [4](../faulty-versions/4.md)       | :x:                   | :x:                     |
| [5](../faulty-versions/5.md)       | :x:                   | :x:                     |
| [6](../faulty-versions/6.md)       | :x:                   | :x:                     |
| [7](../faulty-versions/7.md)       | :x:                   | :x:                     |
| [8](../faulty-versions/8.md)       | :heavy_check_mark:    | :heavy_check_mark:      |
| [9](../faulty-versions/9.md)       | :x:                   | :heavy_check_mark:      |
| [10](../faulty-versions/10.md)     | :x:                   | :heavy_check_mark:      |
| [11](../faulty-versions/11.md)     | :x:                   | :x:                     |
| [12](../faulty-versions/12.md)     | :x:                   | :heavy_check_mark:      |
| [13](../faulty-versions/13.md)     | :heavy_check_mark:    | :heavy_check_mark:      |
| [14](../faulty-versions/14.md)     | :heavy_check_mark:    | :heavy_check_mark:      |
| [15](../faulty-versions/15.md)     | :x:                   | :heavy_check_mark:      |
| [16](../faulty-versions/16.md)     | :x:                   | :x:                     |
| [17](../faulty-versions/17.md)     | :heavy_check_mark:    | :heavy_check_mark:      |
| [18](../faulty-versions/18.md)     | :heavy_check_mark:    | :heavy_check_mark:      |
| [19](../faulty-versions/19.md)     | :heavy_check_mark:    | :heavy_check_mark:      |
| [20](../faulty-versions/20.md)     | :heavy_check_mark:    | :heavy_check_mark:      |
| [21](../faulty-versions/21.md)     | :x:                   | :heavy_check_mark:      |
| [22](../faulty-versions/22.md)     | :heavy_check_mark:    | :heavy_check_mark:      |
| [23](../faulty-versions/23.md)     | :heavy_check_mark:    | :heavy_check_mark:      |
| [24](../faulty-versions/24.md)     | :x:                   | :heavy_check_mark:      |
| [25](../faulty-versions/25.md)     | :x:                   | :x:                     |
| [26](../faulty-versions/26.md)     | :x:                   | :x:                     |
| [27](../faulty-versions/27.md)     | :x:                   | :x:                     |
| [28](../faulty-versions/28.md)     | :x:                   | :x:                     |
| [29](../faulty-versions/29.md)     | :x:                   | :x:                     |
| [30](../faulty-versions/30.md)     | :x:                   | :x:                     |
| [31](../faulty-versions/31.md)     | :x:                   | :x:                     |
| [32](../faulty-versions/32.md)     | :x:                   | :x:                     |
| **Total**                          | **9**                 | **16**                  |
| **Percentage**                     | **28.12%**            | **50.0%**               |

