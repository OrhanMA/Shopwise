# Schéma de Base de Données

Ce fichier source décrit le modèle relationnel des modules Clients, Rendez-vous et Fidélisation.

Le livrable final attendu par l'étude de cas est `bdd/schema base de données.pdf`. Ce fichier Markdown sert de source textuelle versionnée pour générer ou vérifier ce PDF.

```mermaid
erDiagram
    SHOPS ||--o{ CUSTOMERS : owns
    SHOPS ||--o{ SERVICES : offers
    SHOPS ||--o{ APPOINTMENTS : schedules
    SHOPS ||--o{ LOYALTY_TRANSACTIONS : records
    CUSTOMERS ||--o{ APPOINTMENTS : books
    SERVICES ||--o{ APPOINTMENTS : concerns
    CUSTOMERS ||--o{ LOYALTY_TRANSACTIONS : receives
    APPOINTMENTS ||--o| LOYALTY_TRANSACTIONS : generates

    SHOPS {
        bigint id PK
        varchar name
        varchar email
        varchar phone
        timestamp created_at
        timestamp updated_at
    }

    CUSTOMERS {
        bigint id PK
        bigint shop_id FK
        varchar first_name
        varchar last_name
        varchar email
        varchar phone
        integer loyalty_points
        timestamp created_at
        timestamp updated_at
    }

    SERVICES {
        bigint id PK
        bigint shop_id FK
        varchar name
        text description
        integer duration_minutes
        integer points_reward
        boolean active
        timestamp created_at
        timestamp updated_at
    }

    APPOINTMENTS {
        bigint id PK
        bigint shop_id FK
        bigint customer_id FK
        bigint service_id FK
        date appointment_date
        time appointment_time
        varchar status
        text notes
        timestamp honored_at
        timestamp cancelled_at
        timestamp created_at
        timestamp updated_at
    }

    LOYALTY_TRANSACTIONS {
        bigint id PK
        bigint shop_id FK
        bigint customer_id FK
        bigint appointment_id FK
        integer points
        varchar reason
        varchar transaction_type
        timestamp created_at
    }
```

## Règles Métier

- Le commerce de démonstration est "Chez Marie".
- Un client appartient au commerce "Chez Marie".
- Un email client est unique pour un commerce.
- Un rendez-vous concerne un client, un service, une date et une heure.
- Les statuts de rendez-vous sont `SCHEDULED`, `HONORED` et `CANCELLED`.
- Lorsqu'un rendez-vous passe à `HONORED`, une transaction de fidélité `EARNED` de 100 points est créée.
- Le solde du client est stocké dans `customers.loyalty_points` et l'historique est stocké dans `loyalty_transactions`.
- Les données de test sont anonymisées et utilisent des adresses email d'exemple.
