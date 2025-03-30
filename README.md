# car_dealership_management_system

Car dealership management system web application. Backend: Express.js, PostgreSQL. Frontend: React/Next.js.

This application provides a platform for managing the operations of a car dealership, including vehicle inventory, sales tracking, and potentially customer information.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

*   [Node.js](https://nodejs.org/) (LTS version recommended, e.g., >= 18.x)
*   [npm](https://www.npmjs.com/) or [yarn](https://yarnpkg.com/)
*   [Git](https://git-scm.com/)
*   [PostgreSQL](https://www.postgresql.org/) installed and running. You'll need to create a database for the application.

### Installation

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/RUnGEnRED/car_dealership_management_system.git
    cd car_dealership_management_system
    ```

2.  **Install Backend Dependencies:**
    ```bash
    cd backend
    npm install
    # or
    # yarn install
    ```

3.  **Install Frontend Dependencies:**
    ```bash
    cd ../frontend
    npm install
    # or
    # yarn install
    ```

### Running the Application

You need to run both the backend and frontend servers simultaneously.

1.  **Start the Backend Server:**
    ```bash
    # In the backend directory
    npm run dev
    ```
    The backend API should now be running (typically on `http://localhost:3001` or the port specified in your `.env`).

2.  **Start the Frontend Development Server:**
    ```bash
    # In the frontend directory
    npm run dev
    # or
    # yarn dev
    ```
    The frontend application should now be running and accessible in your browser (typically at `http://localhost:3000`).

## Usage

1.  Open your web browser and navigate to `http://localhost:3000` (or the port your frontend is running on).
2.  (If implemented) Log in using staff credentials.
3.  Navigate through the application sections:
    *   **Dashboard:** Overview of key metrics (if available).
    *   **Inventory:** View, search, add, edit, or delete vehicles.
    *   **Sales:** Track and manage vehicle sales (if available).
    *   **(Add other key sections/workflows)**

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details (if you have one).