# IP & Subnet Calculator

A Java 17 desktop utility for calculating IPv4 subnet details from an address in CIDR notation.

## Features
- Validates IPv4 addresses and CIDR prefixes (`/0` to `/32`)
- Calculates subnet mask and wildcard mask
- Displays network and broadcast addresses
- Displays first and last usable host addresses where applicable
- Calculates total addresses and usable host count
- Handles `/31` point-to-point and `/32` single-host prefixes
- Copies the calculation report to the clipboard

## Requirements
- Java 17 or later
- Apache Maven

## Run
```bash
mvn clean package
mvn exec:java
```

## Notes
For conventional subnets, the network and broadcast addresses are excluded from the usable-host count. A `/31` is treated as a point-to-point subnet with two usable addresses, while a `/32` represents one individual address.

## Technology
Java 17, Swing, Maven.
