# Rapports de couverture ShopWise

Les rapports de couverture ont été générés le 30 avril 2026.

## Backend

- Commande : `./gradlew test jacocoTestReport --no-daemon`
- Rapport HTML : `couverture/backend/html/index.html`
- Rapport XML : `couverture/backend/jacocoTestReport.xml`
- Couverture instructions : 85,34 %
- Couverture branches : 92,86 %

## Frontend

- Commande : `npm run test:coverage -- --watch=false`
- Rapport HTML : `couverture/frontend/index.html`
- Rapport JSON : `couverture/frontend/coverage-final.json`
- Couverture instructions : 79,43 %
- Couverture branches : 69,87 %

Les deux rapports dépassent le seuil attendu de 60 % sur les instructions et les branches.
