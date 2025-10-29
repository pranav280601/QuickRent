# 🚗 Car Rental Application

A full-stack car rental platform built using **Spring Boot**, **Angular**, and **MySQL**, featuring secure authentication, booking management, dynamic pricing, and AI-driven recommendations.

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
* **Email Notifications:** Confirmation emails sent post-booking.
* **JWT-based Authentication:** Role-based access for customers and admins.
* **Dockerized Deployment:** Full setup for multi-container environment (Spring Boot, MySQL, Angular, Python ML service).

---

## 🤖 AI Recommendation System

### Overview

An **AI-powered car recommendation system** suggests cars to users based on:

* Their past bookings
* Similar users’ preferences
* Popular car categories and rental patterns

### Algorithm Used

A **hybrid collaborative filtering model** combining:

* **User-based similarity** (users who rented similar cars)
* **Content-based filtering** (based on car type, price range, and fuel type)
* Uses **cosine similarity** and **TF-IDF vectorization** under the hood.

### Architecture

1. **Data Fetching:**
   The Flask ML service reads car and booking data directly from the existing MySQL (using Docker Compose network).

2. **Model Training:**

   * Runs periodically or on admin trigger (`/api/ml/retrain` endpoint).
   * Generates similarity matrices and stores them as serialized `.pkl` models.

3. **Integration with Backend (Spring Boot):**

   * Spring Boot uses `WebClient` to call ML API (`/predict?userId=...`).
   * The ML API returns a list of recommended car IDs and scores.
   * Backend fetches full car details and sends to the Angular UI.

4. **Frontend (Angular):**

   * A new **Recommendations** component displays top suggestions to users.
   * Optional **Admin** component to trigger retraining.

---

## 🐳 Docker Setup

### Services

* **car-rental-backend** – Spring Boot app
* **car-rental-db** – MySQL database
* **car-rental-frontend** – Angular app
* **ml-service** – Python Flask AI model

### Example Docker Compose (excerpt)

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

1. User logs in → Dashboard loads → Backend calls `/api/recommendations/{userId}`
2. Backend forwards request to Python ML service via REST
3. ML service processes user history and returns top 5 car IDs
4. Backend fetches car details and sends JSON to frontend
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

## 🧾 API Examples

| Endpoint                        | Method | Description               |
| ------------------------------- | ------ | ------------------------- |
| `/api/cars`                     | GET    | List all cars             |
| `/api/bookings`                 | POST   | Create booking            |
| `/api/recommendations/{userId}` | GET    | Fetch car recommendations |
| `/api/ml/retrain`               | POST   | Trigger model retraining  |

---
**Shounoop** – Full-Stack Java Developer
Built with ❤️ using Spring Boot, Angular & Machine Learning.
