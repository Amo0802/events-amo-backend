# NeoEvents Backend API

Hi! 😊 This is the backend for my full-stack events app, NeoEvents. I originally built it to help people in my local area find and create events nearby — since there wasn’t anything like it around here. The app lets users browse upcoming local events, save ones they’re interested in, and get notifications.

I planned to publish and market it properly, but due to lack of time and motivation, it’s now mainly a portfolio project showcasing what I can do.

---

## What It Does

- Users can browse events, filter by city or category, and search.
- Users can submit event requests. To avoid spam, these are just requests — sent to me by email through SMTP.
- I (or anyone I give admin access) review these requests and create official events.
- Admin users get extra UI buttons that regular users don’t see, like creating or editing events.
- If a user isn’t logged in and tries to access some features, they get redirected to the login page.
- It’s an MVP but has solid features and could easily go live as is.
  
### Note:
Instead of local city names, the city filter uses an enum of European countries (like Spain, France, Germany) to make it more familiar for anyone reviewing my portfolio.

---

## Try It Out

- **Live frontend app:** [https://app.arminramusovic.com](https://app.arminramusovic.com)  
- **Frontend repo:** [https://github.com/Amo0802/events-amo](https://github.com/Amo0802/events-amo)  
- **API:** [https://api.arminramusovic.com](https://api.arminramusovic.com/actuator/health)

---

## Tech & Deployment

- Built with Spring Boot (Java 21), MySQL, and JWT-based security.
- Event images upload support, email notifications via SMTP, and caching with Caffeine for performance.
- Deployed on a VPS using Docker containers.
- CI/CD pipeline set up with GitHub Actions for automated testing, building, and zero-downtime deployments.

---

## API Endpoints

### Authentication

- `POST /auth/register` - User registration  
- `POST /auth/authenticate` - User login  

### Events

- `GET /events` - Get paginated events  
- `GET /event/main` - Get official/main events  
- `GET /event/promoted` - Get promoted events  
- `GET /event/filter` - Filter events by city/category  
- `GET /event/search` - Search events  
- `POST /event` - Create event (Admin only)  
- `PUT /event/{id}` - Update event (Admin only)  
- `DELETE /event/{id}` - Delete event (Admin only)  

### User Features

- `GET /user/current` - Get current user info  
- `POST /user/save-event/{id}` - Save an event  
- `POST /user/attend-event/{id}` - Mark attendance  
- `POST /user/submit-event` - Submit event proposal  

---

## Security Features

- JWT-based stateless authentication  
- Role-based access control (Admin/User)  
- Password encryption with BCrypt  
- SQL injection prevention through JPA  
- CORS configuration for cross-origin requests  
- Input validation and sanitization
  
---

## Local Setup

If you want to clone and run the backend locally, here are some essentials:

- Configure your application.properties with these variables:

```ini
# Dev Database
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/dev_db
SPRING_DATASOURCE_USERNAME=your_db_username
SPRING_DATASOURCE_PASSWORD=your_db_password

# Mail (Gmail)
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_email_password

# JWT Config
JWT_SECRET=your_dev_jwt_secret_key
```

- Flyway will handle database migrations on startup.
  
- To make the first registered user an admin, change the admin flag to true in the registration service before the first user signs up.

---

## Docker Compose Notes

If you want to build with Docker Compose:

- You’ll need Docker Desktop installed.

- The provided `docker-compose.yml` includes Traefik as a reverse proxy for my domain setup. 

- If you want to run the app locally **without Traefik**, you must remove or comment out the `traefik` service and its related network from your `docker-compose.yml`.

- When running without Traefik, make sure your app service exposes ports directly (e.g., `ports: - "8080:8080"`) to access the backend.

```ini
docker-compose -f docker-compose.prod.yml up --build
```

---

## Feedback & Contributions

This is mainly a portfolio piece, but if you want to suggest improvements or report bugs, feel free to open issues or PRs!

---

Email: arminramusovic11@gmail.com

