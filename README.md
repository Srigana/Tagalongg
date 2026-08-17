# TagAlong (Spring Boot port)

Java/Spring Boot rebuild of TagAlong's backend, keeping the same architecture:
Kafka for events, Redis for search caching, PostgreSQL as source of truth,
Stripe for held/captured payments, and STOMP-over-WebSocket for chat.

## Tech stack

- **Spring Web (MVC)** — REST API
- **Spring Data JPA** — PostgreSQL persistence
- **Spring Kafka** — post-created / request-made / status-updated events
- **Spring Data Redis** — `@Cacheable` search-by-destination, evicted on writes
- **Spring Security + JWT** — stateless auth
- **Spring WebSocket (STOMP)** — real-time poster/requester chat
- **Stripe Java SDK** — manual-capture PaymentIntents (hold on request, capture on accept)

## Running locally

```bash
docker-compose up -d        # postgres, redis, kafka, zookeeper, and the app itself
```

Or run the app outside Docker against the infra containers:

```bash
docker-compose up -d postgres redis zookeeper kafka
mvn spring-boot:run
```

Set real values for `JWT_SECRET` and `STRIPE_SECRET_KEY` via environment
variables (see `application.yml`) before deploying anywhere real.

## API sketch

- `POST /api/auth/register`, `POST /api/auth/login` → JWT
- `POST /api/posts` (auth) — create a trip post
- `GET /api/posts/search?destination=...` — cached search
- `POST /api/requests` (auth) — request to join a post, holds payment
- `POST /api/requests/{id}/accept` / `/reject` — captures or releases payment,
  publishes a status-updated event that a Kafka consumer uses to decrement
  post slots
- WebSocket: connect to `/ws`, send to `/app/chat/{requestId}`, subscribe to
  `/topic/chat/{requestId}`

## What's stubbed / left for you

- Rating submission endpoint (entity + repository exist; wire up a controller)
- Global exception handling (`@ControllerAdvice`) for cleaner error responses
- Refresh tokens / token revocation
- Integration tests (Testcontainers works well here for Postgres/Kafka/Redis)
