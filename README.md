# 📝 Task manager project

This is a university project that has grown into a much more complex and powerful system than initially expected.

It is a **task manager** application that I am continuously improving by adding new features, implementing different algorithms, and applying advanced architectural decisions.

This project also serves as an **internship demo** to showcase my skills and passion for backend and frontend development.

Throughout development, I faced and solved many technical challenges, which helped me gain strong experience working with:

**Spring Security** (CSRF tokens, CORS policy, JWT Authentication)

**WebSockets** (real-time updates with STOMP over SockJS, JWT-secured connections)

**Hibernate & Spring Data JPA** (database interactions)

**Elasticsearch** (advanced searching capabilities)

## 📸 Screenshots of my application
![img.png](imageForReadme/img.png)
![img.png](imageForReadme/img_1.png)

## 🚀 Technologies
### Backend

- Java 17 / Spring Boot 3.4.4 / Spring Security
- Hibernate / Spring Data JPA
- PostgreSQL (database)
- WebSockets (real-time feature)
- STOMP / SockJS
- JWT tokens (authentication and authorization)
- Elasticsearch (live search functionality)
- Default WebSocket message broker

### Frontend 

- React 18 / Vite
- Bootstrap 5 (responsive UI)

## 🐳 Installation and setup with Docker

### Requirements
- [Docker](https://www.docker.com/get-started/) installed
- [Docker compose](https://docs.docker.com/compose/) (included in Docker Desktop)
- [Java JDK 21](https://www.oracle.com/cis/java/technologies/downloads/)

1. Clone the repository

```githubexpressionlanguage
git clone https://github.com/AlexPorokhnya/task-manager-project
cd task-manager-project
```

2. Build and run
```shell
#1. Up Postgres and Elastic Search and wait installation
docker compose up -d
#2. Check that containers already up
docker compose ps
#3. Start backend
./mvnw spring-boot:run -DskipTest 
#Or in cmd on Windows
.\mvnw.exe spring-boot:run -DskipTest
#Open new tab in terminal
#Go into frontend folder
cd React/my-app
#Install all dependencies
npm install
#Start frontend
npm run dev
```
The frontend will be available at http://localhost:5173

> Note: If you have port conflicts(for example port already in use or you start Postgres locally), you can change ports
> in `docker-compose.yml` and dont forget to change also in [application.properties](src/main/resources/application.properties)

> Also you can kill task on this port using terminal commands:
> ```shell
>   netstat -ano | findstr :<port>
>   taskkill /F /PID <pid> #last 4 digits from result of command above
>```

## 🔑 Environmental variables ([application.properties](src/main/resources/application.properties))
```properties
spring.application.name=TaskManagerProject
spring.jackson.date-format=java.text.SimpleDateFormat
spring.jackson.serialization.write-dates-as-timestamps=false
jwt.secret-key = uwbIMP2A2vSUmREmqTVR8FGRrEe//HDpTDuSEx0ylWvuuphX+atxLcPdebhBV6fiZvoleKuSLOR5VKt2zte8Zg==
spring.elasticsearch.uris=http://localhost:9200
spring.datasource.url=jdbc:postgresql://localhost:5444/taskManDB
spring.datasource.password=ahsas125
spring.datasource.username=postgres
spring.datasource.driver-class-name=org.postgresql.Driver
spring.flyway.enabled=true
spring.jpa.hibernate.ddl-auto=validate
```

## 📁 Project structure overview
```css
├── TaskManagerProject
│   ├── src/main/java/com/alex/project/taskmanagerproject
│   └── src/main/resources
├── React
│   ├── my-app/src
│   ├── my-app/public/
│   └── my-app/package.json
└── README.md
└── pom.xml
```

## 🧠 Key Features
- Real time task updates with WebSockets
- User authentication with JWT token
- Elasticsearch with live suggestions
- Creation, edition, deletion of task
- Tasks in markdown format
- Creation of projects 
- Responsive UI with Bootstrap
- Backend validation and error handling
- Ring buffer data structure

## 🧪 Test running
```shell
./mvnw test
```

## 🛠️ Future improvements
- Notification system(e.g email, web inbox)
- Email confirmation 
- Personal profile
- Addition settings
- Frontend tests
- Calendar for task reminding
- Dark/light mode toggle
- NoSql database for storing images and other big data
- Task grouping

## 📬 Contact
Telegram: [@alexporoh](https://t.me/alexporoh)

GitHub: https://github.com/AlexPorokhnya

LinkedIn: https://www.linkedin.com/in/oleksandr-porokhnia-0b20a5358/







