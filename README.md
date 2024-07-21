# New Relic Coding Assessment

## Overview
Hello Team @ New Relic,

This project it implements a Java server named "Application" using Spring Boot and Tomcat that accepts connections from up 
to 5 concurrent clients on TCP/IP port 4000. Clients can send 9-digit numbers to the server, which are then de-duplicated 
and written to a log file. The application includes robust error handling, ensures data resilience, and provides specific
scheduled reports on the received numbers.

### Features/Requirements
1. #### Client Connections: 
Accepts input from at most 5 concurrent clients on TCP/IP port 4000.
2. #### Persistent Connections:
Client connections are kept open unless explicitly required to close.
3. #### Input Validation:
Input lines must consist of exactly nine decimal digits followed by a server-native newline sequence or the termination 
command detailed in requirement 10.
4. #### Leading Zeros: 
Numbers must include leading zeros to ensure they are nine digits in length.
5. #### Log File Creation:
The log file, named `numbers.log`, is created anew or cleared when the application starts.
6. #### Logging Numbers:
Only numbers are written to the log file, each followed by a server-native newline sequence.
7. #### De-duplication:
No duplicate numbers are written to the log file.
8. #### Invalid input handling 
Any non-conforming input is discarded, and the client connection is closed immediately and without comment.
9. #### Periodic Reporting:
Every 10 seconds, the application closes all client connections and performs a clean shutdown as quickly as possible.
10. #### Termination Command:
If a client sends a single line with the word `terminate` followed by a server-native newline sequence, the application
closes all client connections and performs a clean shutdown as quickly as possible.
11. #### Comprehensive Testing:
Provide comprehensive unit and integration tests to verify that all primary considerations and requirements are met.

## Assumptions
1. The log file `numbers.log` is created anew or cleared when the application starts.
2. Leading zeros are retained in the logged numbers.
3. The application aims to handle a large volume of numbers efficiently, optimized for maximum throughput.

## Core Components
1. #### NumberController
Handles WebSocket connections and processes incoming number requests. It ensures that messages are properly received and
passed to the service layer for processing. This controller manages the client connections, processes valid numbers, and
handles the termination command to close all connections and shut down the server.
2. #### DuplicatedListNumberService
Processes and logs the received numbers, ensuring the de-duplication and error handling. This service validates the
incoming numbers, processes them asynchronously using an `ExecutorService`, and writes them to the log file if they are unique.
It also handles termination commands and invalid input by closing client connections. This service is responsible for creating
and clearing the log file at startup, and ensuring no duplicate numbers are written to the log file.
3. #### ScheduledStatisticsService
Generates periodic reports on the received numbers. This service extends `AbstractStatisticsService` and uses the `@Scheduled`
annotation to run the `reportStatistics` method every 10 seconds, printing the count of new unique numbers, 
duplicate numbers, and the total unique numbers received.
4. #### SocketGlobalExceptionHandler
Handles global exceptions for WebSocket connections. This class ensures that any exceptions thrown during the processing
of WebSocket messages are properly handled and appropriate error responses are sent back to the clients. It also manages
the closure of client connections when invalid input is detected.
5. #### WebSessionStorageManager
Manager WebSocket sessions. This component is responsible for storing active WebSocket sessions and providing functionality to close
sessions and providing functionality to close sessions when required. It ensures that client connections are properly tracked
and managed, allowing the application to enforce the limit of 5 concurrent clients.
6. #### WebSocketConfig
Configures the WebSocket settings for the application. This configuration class sets up the message broker, registers WebSocket endpoints,
and ensures that SockJS is enabled for fallback options. It also configures inbound and outbound channels to manage the WebSocket connections
and apply necessary interceptors.

## Testing
### Unit Tests
1. #### DuplicatedListNumberServiceTest
Tests the core logic of the `DuplicatedListNumberService` for handling valid and invalid numbers.
2. #### ScheduledStatisticsServiceTest 
Tests the core logic of the `ScheduledStatisticsService` for handling the appropriate operations on valid and invalid, 
repeated numbers. The tests ensure that the numbers are categorized appropriately according given app requirements. These tests ensure that valid numbers are processed and logged correctly, while invalid numbers are discarded, and appropriate error handling is performed.
The tests also verify the asynchronous execution of tasks using a mocked `ExecutorService`.

### Integration Tests
1. #### DuplicatedListNumberServiceIntegrationTest
This integration test ensures the correct functionality of the `DuplicatedListNumberService` by replicating its functionality without the use of mocks.
2. #### ScheduledStatisticsServiceIntegrationTest
Tests the scheduling behavior of the `ScheduledStatisticsService`. This integration test verifies that the `reportStatistics`
is called at the specified fixed rate, ensuring that periodic reports are generated as expected. The test uses `@SpyBean`
to spy on the service and `Awaitility` to wait for the method invocation.
3. #### WebSocketIntegrationTest
Tests the WebSocket communication and validates the handling of the number submissions, including valid, invalid, and termination commands.
It ensures that the `NumberController` and related services process WebSocket messages correctly and that connections are managed as specified.
4. #### GlobalExceptionHandlerIntegrationTest
Tests the global exception handling for WebSocket connections. It verifies that exceptions thrown during the processing 
of WebSocket messages are properly handled by `SocketGlobalExceptionHandler` and appropriate error responses are sent back to the clients.
5. #### WebSocketTestUtils
Provides utility methods for testing WebSocket connections. It includes methods for sending number requests and handling WebSocket sessions during tests.


## Requirements:
- JDK 17: Ensure you have JDK 17 Installed.
- Apache Maven: Ensure you have Maven installed for building and running the application.
- Spring Boot: This application uses Spring Boot to simplify setup and deployment.
- Tomcat: Embedded Tomcat server provided by Spring Boot for running the application.

## Dependencies
### 1.  spring-boot-starter-web 
    `<dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>`
### 2.  spring-boot-starter-websocket 
    `<dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-websocket</artifactId>
    </dependency>`
### 3.  spring-boot-starter-actuator 
    `<dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>`
### 4.  spring-boot-starter-test 
    `<dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>`
### 5.  spring-boot-starter-validation
    `<dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>`
### 6.  mockito-core
    `<dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <version>5.10.0</version>
        <scope>test</scope>
    </dependency>`
### 7.  jackson-databind
    `<dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.14.0</version>
    </dependency>`
### 8.  jackson-core
    `<dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-core</artifactId>
        <version>2.14.0</version>
    </dependency>`
### 9.  jackson-annotations
    `<dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-annotations</artifactId>
        <version>2.14.0</version>
    </dependency>`

## Running the application

### 1. Build the application.
#### `mvn clean install`

### 2. Run the application:
#### `mvn spring-boot:run`

### 3. Run the tests:
#### `mvn test`

## Conclusion
This project demonstrates a robust and optimized solution for handling concurrent client connections, processing and 
logging 9-digit numbers, and generating periodic reports. The application is designed to be resilient, easy to read, and maintainable,
ensuring high throughput and data integrity.
