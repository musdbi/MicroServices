# Microservices Travel Planning

> **Architecture microservices** pour la gestion de points d'intérêt touristiques et planification de voyages.

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue.svg)](https://www.docker.com/)

## Architecture

```
Gateway (8080) → City Service (8081) → PostgreSQL + JSON
                → Tourism Service (8082) → MongoDB Atlas  
                → Travel Service (8083) → Neo4j AuraDB
```

## Démarrage Rapide

```bash
# Clone & Start
git clone [URL_REPO]
cd MSA
docker-compose up --build

# Vérification
curl http://localhost:8080/api/health
```

## Services & APIs

### City Service (PostgreSQL + JSON)
```bash
# Créer une ville
POST /api/cities
# Villes proches (50km)
GET /api/cities/nearby?city=Paris&radius=50
# Distance entre villes
GET /api/cities/distance?city1=Paris&city2=Lyon
```

### Tourism Service (MongoDB)
```bash
# Points d'intérêt
GET /api/tourism/poi
# Activités saisonnières (ici : Avril, Mai, Juin)
GET /api/tourism/activities/months?months=4,5,6
# Hébergements par ville
GET /api/tourism/accommodations/city/Paris
```

### Travel Service (Neo4j)
```bash
# Créer un voyage
POST /api/travels
# Planifier une journée
POST /api/travels/1/days
# Villes intermédiaires
GET /api/travels/intermediate-cities?start=Paris&end=Lyon
```

### Gateway (Point d'entrée unique)
```bash
# Toutes les APIs via gateway
http://localhost:8080/api/*
# Routes disponibles
GET /api/routes
```

## Base de Données Polyglotte

| Service | DB | Usage |
|---------|----|----|
| City | PostgreSQL + JSON | Calculs géographiques |
| Tourism | MongoDB | Documents flexibles |
| Travel | Neo4j | Relations graphe |

## Tests

```bash
# Tests unitaires
mvn test

# Health checks
GET /api/health              # Gateway
GET /api/cities/health       # City Service
GET /api/tourism/poi/test    # Tourism Service  
GET /api/travels/health      # Travel Service
```

## Développement

```bash
# Build manuel
mvn clean install

# Démarrage manuel (4 terminaux)
cd city-service && mvn spring-boot:run      # 8081
cd tourism-service && mvn spring-boot:run   # 8082
cd travel-service && mvn spring-boot:run    # 8083  
cd gateway-service && mvn spring-boot:run   # 8080
```

## Technologies

- **Backend:** Spring Boot 3.4.4, Java 17, Maven
- **Databases:** PostgreSQL, MongoDB Atlas, Neo4j AuraDB
- **DevOps:** Docker, Docker Compose, Actuator
- **APIs:** REST, JSON, Jackson, Bean Validation