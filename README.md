# car_dealership_management_system

Car dealership management system application.

**Backend**: Spring Boot (Java) with PostgreSQL
**Frontend**: Next.js (React)

This application provides a platform for managing the operations of a car dealership, including vehicle inventory, sales tracking, and potentially customer information.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

* Java JDK 21+
* Apache Maven 3.8+
* PostgreSQL 17+
* Next.js 17+ and npm/yarn
* [Git](https://git-scm.com/)

### Installation

1. **Clone the repository**:
    ```bash
    git clone https://github.com/RUnGEnRED/car_dealership_management_system.git
    cd car_dealership_management_system
    ```

2. **Database Setup**:
    - Create PostgreSQL database named `car_dealership_db`
    - Update `backend_app/src/main/resources/application.properties` with your credentials

3. **Build the project**:
    ```bash
    # Build backend
    cd backend_app
    mvn clean install
    
    # Build frontend 
    cd ../frontend_app
    npm install
    ```

### Running the Application

You need to run both the backend and frontend applications simultaneously.

1. **Start Backend**:
    ```bash
    # In the backend_app directory
    mvn spring-boot:run
    ```
    The backend API should now be running (typically on `http://localhost:3001`).

2. **Start Frontend**:
    ```bash
    # In the frontend_app directory
    npm run dev
    ```
    The frontend application should now be running and accessible.

## Usage

1.  Open your web browser and navigate to `http://localhost:3000` (or the port your backend is running on) for API access.
2.  Run the frontend application for the GUI.
3.  Navigate through the application sections.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.