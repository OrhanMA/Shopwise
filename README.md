# ShopWise

ShopWise est une étude de cas de développement logiciel pour une plateforme SaaS destinée aux commerces de proximité.

Le périmètre implémenté couvre la gestion des clients, la gestion des rendez-vous et la fidélisation client par points pour le commerce de démonstration "Chez Marie".

## Stack Technique

- Backend : Java 21, Spring Boot, Gradle.
- Frontend : Angular, Angular Material et CSS natif.
- Base de données : PostgreSQL.
- Migrations : Flyway.
- Documentation API : OpenAPI/Swagger.
- Industrialisation : Docker, Docker Compose et GitHub Actions.

## Fonctionnalités

- Créer et mettre à jour une fiche client.
- Simuler une connexion client par email.
- Créer un rendez-vous pour un client.
- Filtrer les rendez-vous par date, statut ou client.
- Marquer un rendez-vous comme honoré ou annulé.
- Attribuer automatiquement 100 points lorsqu'un rendez-vous est honoré.
- Consulter le solde de points et l'historique de fidélité d'un client.

## Lancement Local

Le projet doit pouvoir être lancé localement en une seule commande :

```bash
docker compose up --build
```

Cette commande lancera, à terme, les services suivants :

- base PostgreSQL ;
- backend Spring Boot ;
- frontend Angular servi par Nginx.

## Workflow Git

Le workflow retenu est volontairement simple :

- `main` contient le code stable ;
- chaque fonctionnalité est développée dans une branche `feature/<numero-issue>-<sujet>` ;
- chaque correction est développée dans une branche `fix/<numero-issue>-<sujet>` ;
- les commits suivent le format Conventional Commits ;
- les branches de feature sont conservées après fusion.

Exemple de commit :

```text
feat(appointments): add appointment status update
```

## Suivi Projet

Le suivi des tâches se fait avec les Issues GitHub uniquement.

Chaque issue doit être reliée à une user story ou à un livrable attendu.

| Issue | Sujet | Type | Statut |
| --- | --- | --- | --- |
| `#1` | Créer et mettre à jour une fiche client | User story | Ouverte |
| `#3` | Créer un rendez-vous | User story | Ouverte |
| `#4` | Lister et filtrer les rendez-vous | User story | Ouverte |
| `#2` | Marquer un rendez-vous honoré ou annulé | User story | Ouverte |
| `#7` | Attribuer automatiquement les points de fidélité | User story | Ouverte |
| `#5` | Simuler la connexion client | User story | Ouverte |
| `#6` | Consulter le solde et l'historique de points | User story | Ouverte |
| `#10` | Concevoir la base de données | Livrable | Clôturée |
| `#9` | Ajouter les tests et rapports de couverture | Livrable | En cours |
| `#8` | Ajouter Docker et la CI/CD | Livrable | Ouverte |

## API

La documentation OpenAPI/Swagger sera disponible après lancement du backend :

- Swagger UI : `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON : `http://localhost:8080/v3/api-docs`

## Déploiement

La procédure de déploiement sera complétée avec l'industrialisation du projet.

Les images Docker prévues sont :

- `orhanma/shopwise-backend`
- `orhanma/shopwise-frontend`
