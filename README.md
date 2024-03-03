# Kafka Event Project

This simple project demonstrates how to use Kafka within a Spring Boot application to implement event-driven communication. It consists of a producer that exposes a REST API to produce events and a consumer that consumes these events.

## Prerequisites
- Java Development Kit (JDK) 8 or higher
- Apache Maven
- Postgres for data persistence
- Docker (optional, if you want to run Kafka in a Docker container)

## Setup

1. Clone the repository.

2. Ensure Docker is installed and running on your machine.

3. Navigate to the project directory.

4. Start the Kafka cluster using Docker Compose: `docker compose up -d`.


## Running the Application

1. Build the application: `mvn clean package`.

2. Run the application.


### Producer

The producer exposes a REST API to produce events. 
You can send a POST request to the `api/v1/library-event` endpoint with the event data in the request body.
You can send a PUT request to the `api/v1/library-event` endpoint with the event data to update an existing data.

### Consumer

The consumer listens for events produced by the producer. When an event is received, it processes it accordingly into database.

## Configuration

- The Kafka broker and Zookeeper are configured using Docker Compose. You can modify the `docker-compose.yml` file to change the configuration if needed.
- Kafka producer and consumer configurations can be found in the `application.yaml` file.
- Configure your database locally changing the application.yaml file inside consumer package
