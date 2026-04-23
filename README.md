# ShopWise

ShopWise est une étude de cas de développement logiciel pour une plateforme SaaS destinée aux commerces de proximité.

Le projet vise à implémenter les modules suivants :

- gestion des clients ;
- gestion des rendez-vous ;
- fidélisation client par points.

## Stack technique

- Backend : Java 21, Spring Boot, Gradle.
- Frontend : Angular, Angular Material et CSS natif.
- Base de données : PostgreSQL.
- Migrations : Flyway.
- Documentation API : OpenAPI/Swagger.
- Industrialisation : Docker, Docker Compose et GitHub Actions.

## Workflow Git

Le workflow retenu est volontairement simple :

- `main` contient le code stable ;
- chaque fonctionnalité est développée dans une branche `feature/<numero-issue>-<sujet>` ;
- chaque correction est développée dans une branche `fix/<numero-issue>-<sujet>` ;
- les commits suivent le format Conventional Commits ;
- les branches de feature sont conservées après fusion.

## Suivi projet

Le suivi des tâches se fait avec les Issues GitHub uniquement.

Chaque issue doit être reliée à une user story ou à un livrable attendu.

## Déploiement

La procédure de déploiement sera complétée avec l'industrialisation du projet.
