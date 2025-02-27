# Recipe Vault – Collaborative Cooking Platform  

## 🚀 Deployment  
The application is deployed at: http://3.107.98.238:3000/ 
> ⚠ Note: The deployment is powered by AWS EC2 Instance. The application might be slow due to deployment environement limitations.

---
## Running the Project Locally  

### 1️⃣ Clone the Repository  
```bash
git clone [GitHub Repository Link]
cd [Project Directory]
```
### 2️⃣ Update Base API URL
Modify the frontend's base API URL to point to the local backend:

In Vue.js frontend, update the API URL in the .env file or configuration file:
```
VITE_API_BASE_URL=http://localhost:8080
```
### 3️⃣ Run the Application Using Docker
Ensure Docker is installed on your system before proceeding.

Start the application 
```
docker-compose up --build
```
## 📂 Project Directory & Tech Stack
### Project Structure
```
/recipe-vault
 ├── frontend/   # Vue.js frontend (Vue 3, Pinia, Element Plus)
 ├── backend/    # Java Spring Boot backend (REST API, MySQL)
 ├── docker-compose.yml   # Docker configuration files
 ├── README.md   # Documentation
```
## Tech Stack

| Componenet    | Technology Used |
| ------------- | ------------- |
| Frontend      | Vue.js, Pinia, TypeScript, Element Plus, Vite  |
| Backend  | Java Spring Boot, Maven  |
| Database | MySQL |
| Deployment | Docker, AWS EC2 |


## 📌 Application Functionality
### 💡 Core Features
- Recipe Management:
    - Create, view, edit, and delete recipes.
    - Search recipes by title and filter by difficulty or ingredient count.
- Detailed Recipe View:
    - Display full instructions, ingredients, and creator details.
- Pagination:
    - Efficient navigation through recipes.

### 🔒 User Authentication & Authorization
- User Accounts:
    - Signup, login, and session-based authentication.
- User-Specific Operations:
    - Only recipe owners can edit or delete their own recipes.
### 📊 User Dashboard
- View account statistics:
    - Number of recipes created
    - Breakdown of Difficulty and Ingredient Count
