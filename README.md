# HireFlow — Job Portal Backend

A REST API for a job portal built with Spring Boot. Supports three roles — Admin, Recruiter, and Job Seeker — with JWT auth, resume uploads, and email notifications.

---

## Tech Stack

- **Java 21 + Spring Boot 4.1.0**
- **PostgreSQL** — primary database
- **Spring Security + JWT** — stateless authentication
- **Cloudinary** — resume and profile photo storage
- **Spring Mail** — email notifications via Gmail SMTP
- **Maven** — build tool
- **SpringDoc OpenAPI** — Swagger UI for API docs

---

## What it does

- Register/Login with role-based access (Admin, Recruiter, Seeker)
- Recruiters can create company profiles and post jobs
- Seekers can search jobs, upload resumes, apply, and track application status
- Application flow: `APPLIED → UNDER_REVIEW → SHORTLISTED → HIRED / REJECTED`
- Email sent on registration, application submit, and status updates
- Admin can manage users, jobs, and view platform stats
- Swagger UI available at `/swagger-ui.html`

---

## Project Structure

```
com.hireflow
├── auth/          
├── user/          
├── company/       
├── job/           
├── resume/        
├── application/   
├── savedjob/      
├── notification/  
├── admin/         
└── common/        
```

---

## Running Locally

**Requirements:** Java 21, PostgreSQL, Maven

```bash
git clone https://github.com/akashp369/hireflow-backend.git
cd hireflow-backend
```

Set up `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/hireflow
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password

app.jwt.secret=your_jwt_secret
app.jwt.expiration=86400000

cloudinary.cloud-name=your_cloud_name
cloudinary.api-key=your_api_key
cloudinary.api-secret=your_api_secret

spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
```

```bash
mvn spring-boot:run
```

---

## API Testing

Postman collection included in the repo root — `HireFlow.postman_collection.json`

Import it and you're good to go.

---

## Author

Built by [Akash](https://github.com/akashp369)
