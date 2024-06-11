# Flight-Planner 

## **Project Description** 📝

The Flight Planner is a comprehensive application designed to assist users in planning and managing their flights. 

It provides functionalities for adding flights, checking flight availability, and ensuring no duplicate flight entries are recorded.

The application aims to streamline the flight management process for both administrators and users, offering an intuitive interface and efficient backend operations.

**Project Objectives**: 📌
 - *Flight Management*
 - *Flight Search*
 - *Duplicate Check*
 - *Data Persistence*

### **Key Features** 📜
1. **Add Flight**:
   - Input flight details including departure and arrival airports, carrier, departure, and arrival times.
   - Automatically check for and prevent duplicate flights.
   - Store flight information in the local H2 database.

2. **Search Flights**:
   - Search for flights based on criteria such as departure and arrival locations, dates, and carriers.
   - Display a list of matching flights with relevant details.

3. **Airport Management**:
   - Find existing airports or create new ones if they don't exist during flight creation.
   - Maintain a list of all airports with unique codes.

4. **Duplicate Flight Detection**:
   - Implement logic to detect and prevent duplicate flights by checking all relevant fields.
   - Provide appropriate feedback to administrators when a duplicate flight is detected.
     

## **How to run Flight-Planner** 
### **Prerequisites:**
- Ensure you have Java JDK installed on your system.
- Install IntelliJ IDEA, which supports running Spring Boot applications.
- Clone the repository from GitHub.

## **Steps**

### Clone the Repository

1. **Open IntelliJ IDEA.**
2. Navigate to `File` -> `New` -> `Project from Version Control` -> `Git`.
3. Enter `https://github.com/JackDamniel/flight-planner.git` as the Git repository URL.
4. Click `Clone`.

### Open and Build the Project

1. **IntelliJ IDEA Setup:**
   - IntelliJ IDEA will automatically detect the Gradle project.
   - Wait for IntelliJ IDEA to sync and build the project.

### Run the Application

1. **Start the Application:**
   - Navigate to the main class (e.g., `FlightPlannerApplication`).
   - Right-click on the main class and select `Run 'FlightPlannerApplication.main()'`.

### Verify the Application

1. **Confirm Startup:**
   - Once the application is running, check the console for startup messages indicating that the application has started successfully.

### **Interacting with the Application:**
- Since the application doesn't have a UI, you can interact with it using tools like **Postman** or **curl** to make HTTP requests.

### **Interacting with the Application using Postman**

- **Clear Flights:**
  - Method: POST
  - URL: `http://localhost:8080/testing-api/clear`
  - Body: None

- **Add Flight:**
  - Method: PUT
  - URL: `http://localhost:8080/admin-api/flights`
  - Body: 
    ```json
    {
        "from": "JFK",
        "to": "LAX",
        "carrier": "Delta",
        "departureTime": "2024-07-15T06:00:00",
        "arrivalTime": "2024-07-15T09:00:00"
    }
    ```
  - Headers: `Content-Type: application/json`

- **Delete Flight:**
  - Method: DELETE
  - URL: `http://localhost:8080/admin-api/flights/{id}`
  - Replace `{id}` with the actual flight ID.
  - Body: None

- **Fetch Flight:**
  - Method: GET
  - URL: `http://localhost:8080/admin-api/flights/{id}`
  - Replace `{id}` with the actual flight ID.
  - Body: None

- **Search Airports:**
  - Method: GET
  - URL: `http://localhost:8080/api/airports?search=JFK`
  - Replace `JFK` with the search term.
  - Body: None

- **Search Flights:**
  - Method: POST
  - URL: `http://localhost:8080/api/flights/search`
  - Body:
    ```json
    {
        "from": "JFK",
        "to": "LAX",
        "departureDate": "2024-07-15"
    }
    ```
  - Headers: `Content-Type: application/json`

## **Technologies Used** 💡
- Java
- SpringBoot
- SpringBoot security
- H2 Database (Uses SQL)
- Gradle
- Git


