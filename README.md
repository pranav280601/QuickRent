# 🚗 QuickRent - A Car Rental Web Application

A full-stack **Car Rental Management System** built with **Java Spring Boot**, **Angular**, and **MySQL**, designed to allow users to browse, book, and manage cars easily.  
This project includes a **dynamic pricing engine**, **email notifications**, and **Dockerized deployment** setup for seamless local and production environments.

---

## 🧱 Tech Stack

### **Backend**
- Java 17  
- Spring Boot 3+  
- Spring Data JPA  
- Spring Mail  
- MySQL  
- Docker  
- Maven  

### **Frontend**
- Angular 17+  
- TypeScript  
- Bootstrap / Material UI  

---

## 🚀 Features

### 🔑 **Authentication & Authorization**
- Secure login & registration using JWT.
- Role-based access control (Customer / Admin).

### 🚘 **Car Management**
- Admins can add, update, or delete cars.
- Customers can view available cars and book them.

### 🕓 **Booking System**
- Real-time booking validation (based on car availability).
- Automatic total price calculation.

### 💰 **Dynamic Pricing Engine**
- Calculates price based on:
  - **Distance (KM)** covered.
  - **Peak hours multiplier** (1.5x between 6–9 AM, 5–8 PM).


### 📧 **Email Notifications**
- Sends a confirmation email after every successful booking.
- Uses Spring Boot Mail + SMTP (Gmail recommended).

### 🐳 **Dockerized Deployment**
- Full setup using `docker-compose`:
- MySQL database container.
- Spring Boot backend container.
- Simple one-command launch: `docker-compose up --build`.

### 📊 **Admin Dashboard**
- Admin can view all bookings, users, and car availability.
- Angular UI built with responsive layout.

---


  - **Peak hours multiplier** (1.5x between 6–9 AM, 5–8 PM).
- Example formula:
