# Travel Experts – Android Application

## 📌 Course Information
**Course:** Threaded Project for Object-Oriented Software Development  
**Institution:** SAIT (Southern Alberta Institute of Technology)  
**Program:** Fast Track Object-Oriented Software Development  
**Instructor:** Samuel Sofela  
**Project Due Date:** 04-17-2025  
**Group:** 1

## 📜 Table of Contents
- [Project Overview](#-project-overview)
- [Technologies Used](#-technologies-used)
- [Features](#-features)
- [Setup and Installation](#-setup-and-installation)

## 📌 Project Overview
This is an **Android application** developed for **Travel Experts agents** as part of **Project Workshop – CMPP264 (Android)** at SAIT. The app allows agents and managers to **view and manage travel packages, customers, suppliers, and products**, while also enabling **location search, user registration with photo capture, and activity logging**.

## 🔧 Technologies Used
- **Java** (Core Development)
- **Spring Boot** (Backend framework)
- **PostgreSQL** (Database)
- **REST API** (Server Communication)
- **GitHub** (Version Control)

## ✨ Features
- User Authentication 
- Travel Package Management (Add, Edit, Delete, View)
- Customer Management for Travel Agents
- Supplier and Product Management
- Secure API for Agent-Only Access
- Booking System for Customer Reservations
- Activity Logging for Tracking User Actions
- User Registration with Profile Photo Capture

## 🚀 Setup and Installation

### Backend (Spring Boot API)
1. **Clone the repository**  
   ```bash
   git clone https://github.com/your-repo/travel-expert-web-backend.git
   cd travel-expert-web-backend
   ```
2. **Set up PostgreSQL database**  
   - Create a PostgreSQL database: `travelexperts`  
   - Update database credentials in `application.properties`
  
3. **Build and run the application**  
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
  
4. **Verify API is running**  
   - Open `http://localhost:8080/api-docs` to access Swagger documentation.  

---
💡 *Developed as part of SAIT's Fast Track Object-Oriented Software Development Program.*