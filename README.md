# TagAlong

TagAlong is a peer-to-peer ride-sharing backend built with **Java and Spring Boot**.

Users can create rides, search for rides, request seats, chat in real time, and handle payments.

## Features

- JWT authentication
- Create and search rides
- Redis caching
- Ride request management
- Stripe payments
- Kafka events
- WebSocket chat
- PostgreSQL database
- Docker support

## Tech Stack

- Java 21
- Spring Boot
- PostgreSQL
- Redis
- Apache Kafka
- Stripe
- WebSocket / STOMP
- Docker
- Maven

## Run Locally

```bash
git clone https://github.com/Srigana/Tagalongg.git
cd Tagalongg
docker compose up -d
mvn spring-boot:run
```

The application runs on:

```text
http://localhost:8081
```

## Main APIs

```text
POST /api/auth/register
POST /api/auth/login

POST /api/posts
GET  /api/posts/search

POST /api/requests
POST /api/requests/{requestId}/accept
POST /api/requests/{requestId}/reject
```

## Project Structure

```text
controller/
service/
repository/
entity/
security/
kafka/
config/
```

## Future Improvements

- Add more tests
- Add Swagger documentation
- Add better ride search filters
- Add CI/CD deployment
