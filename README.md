# Food_Donation_Management_system_App

# 🍲 Food Donation Management System

![License](https://img.shields.io/badge/license-MIT-blue.svg)
![Java](https://img.shields.io/badge/Java-Spring%20Boot-green)
![React](https://img.shields.io/badge/React-Frontend-blue)


## 📖 Description

Food Donation Management System is a desktop application built with Java Swing that connects food donors with receivers (NGOs, shelters, individuals in need). The system enables efficient management of food donations, tracking, and distribution to reduce food waste and help communities.


## ✨ Features

- 🍽️ **Food Item Management**: Add, view, update, and delete food items with details
- 👥 **Donor Management**: Register and manage donor information
- 🏢 **Receiver Management**: Track NGOs and individuals receiving donations
- 📊 **Dashboard**: View donation statistics and status
- 🔍 **Search & Filter**: Search food items by category, status, or donor
- 🗄️ **JDBC Integration**: Direct MySQL database connectivity
- 🎨 **Java Swing GUI**: Desktop interface with intuitive forms and tables


## 🛠️ Tech Stack

- **Language**: Java
- **GUI Framework**: Java Swing
- **Database**: MySQL
- **Database Connectivity**: JDBC (Java Database Connectivity)
- **Build Tool**: Maven (optional) or plain Java compilation
- **IDE**: IntelliJ IDEA / Eclipse / NetBeans


## 🛠️ Prerequisites

Before running the application, ensure you have:

- **Java JDK** 8 or higher
- **MySQL** Server 8.0+
- **MySQL Connector/J** (JDBC Driver)
- **IDE**: IntelliJ IDEA, Eclipse, or NetBeans (optional)

## ⚙️ Installation & Setup

### 1. Clone the Repository
git clone https://github.com/yourusername/food-donation-system.git
cd food-donation-system


### 2. Setup MySQL Database

#### Create Database
CREATE DATABASE food_donation_db;
USE food_donation_db;


#### Create Tables
Run the SQL script provided in `/database/schema.sql` to create all necessary tables.

### 3. Configure Database Connection

Edit the database connection details in `src/config/DatabaseConfig.java`:

public class DatabaseConfig {
public static final String URL = "jdbc:mysql://localhost:3306/food_donation_db";
public static final String USER = "your_username";
public static final String PASSWORD = "your_password";
}


### 4. Add MySQL JDBC Driver

Download MySQL Connector/J from [MySQL website](https://dev.mysql.com/downloads/connector/j/) and add it to your project classpath.

### 5. Compile and Run

#### Using IDE
- Open the project in IntelliJ IDEA/Eclipse
- Run the `Main.java` file

#### Using Command Line
javac -cp .:mysql-connector-java-8.0.30.jar src/**/*.java
java -cp .:mysql-connector-java-8.0.30.jar src.Main

## 📁 Project Structure

food-donation-system/
├── src/
│ ├── com/
│ │ └── ayushcode/
│ │ └── food_donation/
│ │ ├── Main.java
│ │ ├── config/
│ │ │ └── DatabaseConfig.java
│ │ ├── dao/
│ │ │ ├── FoodItemDAO.java
│ │ │ ├── DonorDAO.java
│ │ │ └── ReceiverDAO.java
│ │ ├── model/
│ │ │ ├── FoodItem.java
│ │ │ ├── Donor.java
│ │ │ └── Receiver.java
│ │ └── ui/
│ │ ├── DashboardFrame.java
│ │ ├── FoodItemFrame.java
│ │ ├── DonorFrame.java
│ │ └── ReceiverFrame.java
├── database/
│ ├── schema.sql
│ └── sample_data.sql
├── lib/
│ └── mysql-connector-java-8.0.30.jar
├── screenshots/
└── README.md


## 🚀 Usage

1. Start MySQL server
2. Create and populate the database using provided SQL scripts
3. Configure database connection in `DatabaseConfig.java`
4. Run the application via `Main.java`
5. Use the GUI to:
   - Add/Edit/Delete food items
   - Manage donors and receivers
   - Search and filter donations
   - View statistics


## 🔑 Key Classes

### Model Classes
- `FoodItem.java` - Food item entity
- `Donor.java` - Donor entity
- `Receiver.java` - Receiver entity

### DAO (Data Access Object) Classes
- `FoodItemDAO.java` - CRUD operations for food items
- `DonorDAO.java` - CRUD operations for donors
- `ReceiverDAO.java` - CRUD operations for receivers

### UI Classes
- `Main.java` - Application entry point
- `DashboardFrame.java` - Main dashboard
- `FoodItemFrame.java` - Food items management
- `DonorFrame.java` - Donor management
- `ReceiverFrame.java` - Receiver management


## 🤝 How to Contribute

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/YourFeature`)
3. Commit changes (`git commit -m 'Add feature'`)
4. Push to branch (`git push origin feature/YourFeature`)
5. Open a Pull Request

## 👨‍💻 Authors

- **Your Name** - [@Not-Ayushs](https://github.com/Not-Ayushs)

