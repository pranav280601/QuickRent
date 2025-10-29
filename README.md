# 🚗 Car Rental Application

A full-stack car rental platform built using **Spring Boot**, **Angular**, and **MySQL**, featuring secure authentication, booking management, dynamic pricing, and an AI-powered recommendation system.

---

## ⚙️ Tech Stack

**Backend:** Java, Spring Boot, Spring Data JPA, REST APIs
**Frontend:** Angular
**Database:** MySQL
**Containerization:** Docker & Docker Compose
**AI/ML:** Python (Flask microservice using scikit-learn / pandas)
**Tools:** Maven, Postman

---

## 🧩 Features

### Core Features

* **Car Management:** Admins can add, update, and delete cars.
* **Booking Lifecycle:** Customers can browse, book, and manage rentals.
* **Dynamic Pricing Engine:** Calculates total cost based on duration, distance, and peak hours.
* **Email Notifications:** Sends booking confirmation emails.
* **JWT-based Authentication:** Role-based access for customers and admins.
* **Dockerized Deployment:** Multi-container setup for backend, frontend, database, and ML service.

---

## 🤖 AI Recommendation System

### Overview

An **AI-driven recommendation system** provides personalized car suggestions based on:

* Past bookings and user preferences
* Similar users’ behavior
* Car category popularity and rental frequency

### Algorithm

Uses a **hybrid collaborative filtering** approach combining:

* **User-based similarity** (users with similar booking patterns)
* **Content-based filtering** (car type, fuel, and price range)
* Implements **cosine similarity** and **TF-IDF vectorization** with scikit-learn.

### Architecture

1. **Data Source:** Reads data directly from the MySQL database in Docker.
2. **Model Training:** Periodically or manually triggered via `/api/ml/retrain` endpoint.
3. **Integration:** Spring Boot calls Flask service (`/predict?userId=...`) using `WebClient`.
4. **Frontend Display:** Angular displays top car recommendations per user.

---

## 🐳 Docker Setup

### Services

* **car-rental-backend** – Spring Boot app
* **car-rental-db** – MySQL database
* **car-rental-frontend** – Angular app
* **ml-service** – Python Flask AI service

### Example Docker Compose

```yaml
services:
  car-rental-db:
    image: mysql:8
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: carrental
    ports:
      - "3306:3306"

  car-rental-backend:
    build: ./backend
    depends_on:
      - car-rental-db
    ports:
      - "8080:8080"

  car-rental-frontend:
    build: ./frontend
    ports:
      - "4200:4200"

  ml-service:
    build: ./ml-service
    ports:
      - "5000:5000"
    depends_on:
      - car-rental-db
```

---

## 🧠 How It Works

1. User logs in → dashboard loads → backend calls `/api/recommendations/{userId}`
2. Backend forwards request to Python ML microservice
3. ML service returns top car IDs and recommendation scores
4. Backend retrieves car details and sends JSON to frontend
5. Angular displays recommended cars dynamically

---

## 📂 Project Structure

```
com.shounoop.carrental
├── controller
├── service
│   ├── CarService.java
│   ├── BookingService.java
│   └── RecommendationService.java   ← New
├── ml
│   └── RecommendationController.java
├── config
└── repository
```

**ML Microservice**

```
ml-service/
├── app.py
├── model/
│   └── recommender.pkl
├── requirements.txt
└── Dockerfile
```

---

## 🚀 Run the Application

```bash
docker-compose up --build
```

---

## 🧾 API Endpoints

| Endpoint                        | Method | Description               |
| ------------------------------- | ------ | ------------------------- |
| `/api/cars`                     | GET    | List all cars             |
| `/api/bookings`                 | POST   | Create a new booking      |
| `/api/recommendations/{userId}` | GET    | Fetch car recommendations |
| `/api/ml/retrain`               | POST   | Trigger model retraining  |
