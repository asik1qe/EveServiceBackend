# EVE Online Backend

Backend service for the EVE Online project.  
This application collects data from the EVE Online API, processes it, stores it in a database, caches frequently requested results, and exposes convenient REST endpoints for the frontend.

---

## About the project

This repository contains the server-side part of the EVE Online project.

The backend is responsible for:

- requesting data from external EVE Online API sources,
- processing and aggregating market and LP Store related data,
- storing structured data in a database,
- caching expensive responses,
- providing REST endpoints for the frontend application.

The main goal of the project is to transform raw external data into a format that is fast, convenient, and practical for further analysis in the client application.

---

## Main Features

- Integration with external EVE Online API
- Data collection and transformation
- LP Store offer processing
- REST API for frontend clients
- PostgreSQL data storage
- Redis caching for heavy or frequently requested responses
- Concurrent request processing for better performance
- Error handling for unavailable external data
- Docker support for deployment

---

## Tech Stack

- **Java**
- **Spring Boot**
- **Spring Web**
- **Spring Data JPA**
- **PostgreSQL**
- **Redis**
- **Docker / Docker Compose**
- **Maven / Gradle** *(depending on your project setup)*
- **JUnit / Mockito** *(if tests are included)*

---

## Architecture Overview

The backend follows a typical layered architecture:

- **Controller layer** — accepts HTTP requests and returns API responses
- **Service layer** — contains business logic and data processing
- **Repository layer** — works with the database
- **Integration layer** — communicates with external EVE Online API
- **Cache layer** — stores precomputed or frequently requested data in Redis

This structure keeps the codebase easier to maintain, test, and extend.

---

## What the backend does

Typical data flow:

1. The service requests raw data from the EVE Online API
2. The received data is parsed and transformed into internal DTO/entity models
3. Important data is saved into PostgreSQL
4. Expensive computed responses can be cached in Redis
5. The frontend requests prepared data through REST endpoints
6. The backend returns already processed and convenient JSON responses