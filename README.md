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

## Contraintes de l'Étude de Cas

- API ouverte sans authentification avancée : la connexion client est volontairement simulée par email.
- RGPD : seules les données nécessaires au périmètre sont stockées pour les clients (`nom`, `email`, `téléphone`) et les données de démonstration utilisent des adresses d'exemple.
- Accessibilité et responsive design : l'interface Angular Material est utilisable sur desktop et mobile, avec navigation adaptée aux petits écrans.
- Scalabilité fonctionnelle : le modèle conserve une table `shops` et des clés étrangères par commerce, même si la démonstration est centrée sur "Chez Marie".
- Lancement local reproductible : l'application complète démarre avec une seule commande Docker Compose.

## Lancement Local

Prérequis :

- Docker ;
- Docker Compose v2.

Le projet se lance localement en une seule commande depuis la racine du repository :

```bash
docker compose up --build
```

Cette commande lance les services suivants :

- base PostgreSQL ;
- backend Spring Boot ;
- frontend Angular servi par Nginx.

Une fois les conteneurs démarrés :

- Frontend : `http://localhost:4200`
- Backend : `http://localhost:8080`
- Swagger UI : `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON : `http://localhost:8080/v3/api-docs`

Les données de démonstration sont injectées par Flyway au démarrage du backend.

Pour arrêter l'application :

```bash
docker compose down
```

Pour réinitialiser totalement la base locale Docker :

```bash
docker compose down -v
docker compose up --build
```

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
| `#1` | Créer et mettre à jour une fiche client | User story | Clôturée |
| `#3` | Créer un rendez-vous | User story | Clôturée |
| `#4` | Lister et filtrer les rendez-vous | User story | Clôturée |
| `#2` | Marquer un rendez-vous honoré ou annulé | User story | Clôturée |
| `#7` | Attribuer automatiquement les points de fidélité | User story | Clôturée |
| `#5` | Simuler la connexion client | User story | Clôturée |
| `#6` | Consulter le solde et l'historique de points | User story | Clôturée |
| `#11` | Initialiser le frontend Angular responsive | Livrable | Clôturée |
| `#10` | Concevoir la base de données | Livrable | Clôturée |
| `#9` | Ajouter les tests et rapports de couverture | Livrable | Clôturée |
| `#8` | Ajouter Docker et la CI/CD | Livrable | Clôturée |
| `#12` | Audit final de conformité de l'étude de cas | Qualité | Clôturée |
| `#13` | Corriger le style des chips de statut | Correction | Clôturée |

## Traçabilité GitHub

Les branches de fonctionnalité et de correction sont conservées après fusion, conformément au workflow retenu.

| Périmètre | Issues | Branche principale | Commit ou merge de référence |
| --- | --- | --- | --- |
| Organisation projet | `#1` à `#10` initiales | `feature/1-project-organization` | `94f94c7`, `cbcc554` |
| Conception BDD | `#10` | `feature/q2-database-design` | `78e4982`, `5413c74` |
| Backend API et règles métier | `#1` à `#7` | `feature/backend-core-api` | `0bda760`, `5ac9e9b`, `2efcfb0` |
| Frontend responsive | `#1` à `#7`, `#11` | `feature/11-frontend-shell` | `04c37df`, `6759666` |
| Tests, couverture, Docker et CI/CD | `#8`, `#9` | `feature/8-local-docker-run` | `fa95f34`, `00cf7d1` |
| Correction UI des statuts | `#13` | `fix/status-chip-outline` | `274d05d`, `6ef94e9` |
| Audit final | `#12` | `fix/12-compliance-audit` | commit de finalisation |

## API

La documentation OpenAPI/Swagger sera disponible après lancement du backend :

- Swagger UI : `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON : `http://localhost:8080/v3/api-docs`

## Déploiement

La procédure locale reproductible repose sur Docker Compose :

1. Cloner le repository.
2. Vérifier que Docker est démarré.
3. Lancer `docker compose up --build` depuis la racine.
4. Accéder au frontend sur `http://localhost:4200`.

Le frontend Nginx proxyfie les appels `/api` vers le backend Spring Boot. Le backend se connecte à PostgreSQL avec les variables d'environnement déclarées dans `docker-compose.yml`.

Les images Docker prévues sont :

- `orhanma/shopwise-backend`
- `orhanma/shopwise-frontend`

## Configuration

Les variables d'environnement utiles au lancement Docker sont déclarées dans `docker-compose.yml` :

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SERVER_PORT`

Un éventuel fichier `.env` local est ignoré par Git pour éviter de versionner des secrets ou des paramètres personnels.

## Tests et Couverture

Backend :

```bash
cd backend
./gradlew test jacocoTestReport --no-daemon
```

Frontend :

```bash
cd frontend
npm run test:coverage -- --watch=false
```

Les rapports déposés dans le repository sont disponibles dans :

- `couverture/backend/html/index.html`
- `couverture/backend/jacocoTestReport.xml`
- `couverture/frontend/index.html`
- `couverture/frontend/coverage-final.json`

Couverture actuelle :

- Backend : 85,34 % instructions, 92,86 % branches.
- Frontend : 79,43 % instructions, 69,87 % branches.

## CI/CD

La pipeline GitHub Actions est définie dans `.github/workflows/ci-cd.yml`.

Elle exécute à chaque push sur `main`, `feature/**`, `fix/**` et à chaque pull request vers `main` :

- le build et les tests backend avec rapport JaCoCo ;
- le build et les tests frontend avec rapport de couverture ;
- la mise à disposition des rapports de couverture en artefacts téléchargeables.

Sur chaque push vers `main`, la pipeline construit et publie les images Docker suivantes sur DockerHub si les secrets sont configurés :

- `orhanma/shopwise-backend:latest`
- `orhanma/shopwise-backend:<sha>`
- `orhanma/shopwise-frontend:latest`
- `orhanma/shopwise-frontend:<sha>`

Secrets GitHub nécessaires :

- `DOCKERHUB_USERNAME` : `orhanma`
- `DOCKERHUB_TOKEN` : token d'accès DockerHub

La publication DockerHub a été validée sur le workflow GitHub Actions du commit `274d05d`.

## Auto-Évaluation Finale

| Partie | État | Preuves |
| --- | --- | --- |
| Q1 Organisation | Validé | Issues `#1` à `#13`, labels, workflow Git et tableau de suivi dans ce README |
| Q2 Conception | Validé | `bdd/schema base de données.pdf`, `bdd/schema-base-donnees.md`, `bdd/script.sql`, migrations Flyway |
| Q3 Développement | Validé | Backend Spring Boot, frontend Angular, endpoints et écrans couvrant les user stories |
| Q4 Tests | Validé | Tests backend/frontend, rapports dans `couverture/`, seuils de 60 % dépassés |
| Q5 Industrialisation | Validé | Dockerfiles, `docker-compose.yml`, GitHub Actions, artefacts de couverture, images DockerHub |
