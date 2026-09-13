## **🎯College Portal APIs With Real-Time Notification Service**
This project demonstrates a real-time notification service using Spring Boot for the backend and React for the frontend. The service allows for real-time updates via WebSockets, ensuring that notifications are pushed to the client instantly.

## **`📦Prerequisites`**
 - ![Static Badge](https://img.shields.io/badge/Java-17%2B-%23165BFF?logo=openjdk)
 - ![Static Badge](https://img.shields.io/badge/Maven-4.0.0-%236D00CC?logo=apachemaven)
 - ![Static Badge](https://img.shields.io/badge/PostgreSQL-13-%234169E1?logo=postgresql)
 - ![Static Badge](https://img.shields.io/badge/Json%20Web%20Token-0.11.5-%23FD3456?logo=jsonwebtokens&logoColor=auto)
 - ![Static Badge](https://img.shields.io/badge/Open%20API-2.8.6-%2319216C?logo=openapiinitiative&logoColor=auto)
 - ![Static Badge](https://img.shields.io/badge/Apache%20Kafka-4.3.0-%23FF6300?logo=apachekafka&logoColor=auto)

## **`⛓Project Structure`**
     clgportal_api/
     ├── src/
     │   ├── main/
     │   │   ├── java/com/aj/clgportal/
     │   │   │   ├── ClgportalApplication.java          # Main entry point
     │   │   │   ├── controller/                        # HTTP endpoints
     │   │   │   │   ├── AdminController.java
     │   │   │   │   ├── AuthController.java
     │   │   │   │   ├── StudentController.java
     │   │   │   │   ├── TeacherController.java
     │   │   │   │   ├── NoticeController.java
     │   │   │   │   └── ... (other controllers)
     │   │   │   ├── service/                           # Service interfaces
     │   │   │   │   ├── AdminService.java
     │   │   │   │   ├── AuthService.java
     │   │   │   │   └── ... (other interfaces)
     │   │   │   ├── impl/                              # Service implementations
     │   │   │   │   ├── AdminServiceImpl.java
     │   │   │   │   ├── AuthServiceImpl.java
     │   │   │   │   └── ... (other implementations)
     │   │   │   ├── repository/                        # JPA repositories
     │   │   │   │   ├── AdminRepository.java
     │   │   │   │   ├── StudentRepository.java
     │   │   │   │   └── ... (other repositories)
     │   │   │   ├── entity/                            # JPA entities
     │   │   │   │   ├── Admin.java
     │   │   │   │   ├── Student.java
     │   │   │   │   ├── Notice.java
     │   │   │   │   └── ... (other entities)
     │   │   │   ├── dto/                               # Data Transfer Objects
     │   │   │   │   ├── AdminDto.java
     │   │   │   │   ├── StudentDto.java
     │   │   │   │   ├── NoticeDto.java
     │   │   │   │   ├── ApiResponse.java
     │   │   │   │   └── ... (other DTOs)
     │   │   │   ├── security/                          # Security & JWT
     │   │   │   │   ├── JwtTokenProvider.java
     │   │   │   │   ├── JwtAuthenticationFilter.java
     │   │   │   │   ├── JwtAuthenticationEntryPoint.java
     │   │   │   │   └── CustomAdminDetailsService.java
     │   │   │   ├── config/                            # Spring configurations
     │   │   │   │   ├── SpringSecurityConfig.java
     │   │   │   │   ├── WebSocketConfig.java
     │   │   │   │   ├── WebConfig.java
     │   │   │   │   └── OpenApiConfig.java
     │   │   │   ├── exception/                         # Custom exceptions
     │   │   │   │   ├── ResourceNotFoundException.java
     │   │   │   │   └── ... (other exceptions)
     │   │   │   └── util/                              # Utility classes
     │   │   │       └── ... (utility methods)
     │   │   └── resources/
     │   │       ├── application.properties              # Default config
     │   │       ├── application-dev.properties         # Dev environment
     │   │       ├── application-prod.properties        # Prod environment
     │   │       ├── banner.txt                         # Banner
     │   │       ├── static/                            # Static files (future)
     │   │       └── templates/                         # Templates (future)
     │   └── test/
     │       ├── java/com/aj/clgportal/
     │       │   ├── controller/
     │       │   │   └── AdminControllerTest.java
     │       │   ├── service/
     │       │   │   └── AdminServiceImplTest.java
     │       │   ├── repository/
     │       │   │   └── AdminRepositoryTest.java
     │       │   └── conftest.py                        # Test fixtures (if using)
     │       └── resources/
     │           ├── application-test.properties        # Test config
     │           └── test-data.sql                      # Test data
     ├── pom.xml                                        # Maven config
     ├── Dockerfile                                     # Container config
     ├── docker-compose.yml                             # Local dev setup
     ├── deploymentservice.yaml                         # K8s deployment
     ├── Jenkinsfile                                    # CI/CD pipeline
     └── README.md                                      # Documentation

## **`⚙️Setup`**
**1. Clone the Repository**
```bash
git clone https://github.com/theajdev/clgportal_api.git
cd clgportal_api
```

## **`💻Frontend`**
**1. Clone the Repository**
```bash
git clone https://github.com/theajdev/clgportal_ui.git
cd clgportal_ui
```

