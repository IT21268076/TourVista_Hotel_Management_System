# Hotel Management System - TourVista

## Overview

This README provides instructions on how to set up and run the Hotel Management System, a comprehensive solution for managing hotel operations. The system is divided into two main components: the frontend, built with Angular 15, and the backend, built with Spring Boot 3.2. The database used is MySQL.

## Prerequisites

- Node.js and npm (for Angular)
- Java Development Kit (JDK) 11 or higher (for Spring Boot)
- MySQL Server
- An IDE or text editor (e.g., Visual Studio Code, IntelliJ IDEA)

## Frontend Setup

### Step 1: Clone the Frontend Repository

Open a terminal and navigate to the directory where you want to clone the frontend repository. Then, run:

```
bash
git clone https://github.com/IT21268076/TourVista_Hotel_Management_System.git
```

### Step 2: Install Dependencies

Navigate into the frontend project directory and install the necessary dependencies:

bash cd <frontend-project-directory> npm install


### Step 3: Configure Environment Variables

Edit 'environment.ts' file in the root of the frontend project directory and add the necessary environment variables. For example:

BACKEND_URL=http://localhost:8080/api


### Step 4: Run the Frontend

Start the Angular development server:

bash ng serve

By default, the application will be available at `http://localhost:4200/`.

## Backend Setup

### Step 1: Clone the Backend Repository

Open a terminal and navigate to the directory where you want to clone the backend repository. Then, run:

```
bash
git clone https://github.com/IT21268076/TourVista_Hotel_Management_System.git
```

### Step 2: Configure MySQL Database

Create a MySQL database and user for the application. Then, configure the database connection in the `application.properties` file located in the `src/main/resources` directory of the backend project. For example:

spring.datasource.url=jdbc:mysql://localhost:3306/hotel_management_db?useSSL=false&serverTimezone=UTC spring.datasource.username=<your-database-username> spring.datasource.password=<your-database-password>


### Step 3: Run the Backend

Navigate into the backend project directory and run the Spring Boot application:

By default, the backend will be available at `http://localhost:8080/`.

## Running the Application

1. Start the backend by following the steps in the "Backend Setup" section.
2. Start the frontend by following the steps in the "Frontend Setup" section.
3. Open a web browser and navigate to `http://localhost:4200/` to access the Hotel Management System.

