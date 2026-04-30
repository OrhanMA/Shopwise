INSERT INTO shops (id, name, email, phone)
VALUES (1, 'Chez Marie', 'contact@chez-marie.example', '+33102030405');

INSERT INTO customers (id, shop_id, first_name, last_name, email, phone, loyalty_points)
VALUES
    (1, 1, 'Alice', 'Martin', 'alice.martin@example.com', '+33601020304', 100),
    (2, 1, 'Karim', 'Benali', 'karim.benali@example.com', '+33605060708', 0),
    (3, 1, 'Sophie', 'Durand', 'sophie.durand@example.com', '+33611121314', 200);

INSERT INTO services (id, shop_id, name, description, duration_minutes, points_reward, active)
VALUES
    (1, 1, 'Retrait commande', 'Rendez-vous de retrait de commande en boutique.', 15, 100, TRUE),
    (2, 1, 'Conseil produit', 'Conseil personnalisé sur les produits de l''épicerie.', 30, 100, TRUE),
    (3, 1, 'Panier découverte', 'Préparation et présentation d''un panier découverte.', 45, 100, TRUE);

INSERT INTO appointments (id, shop_id, customer_id, service_id, appointment_date, appointment_time, status, honored_at, notes)
VALUES
    (1, 1, 1, 1, CURRENT_DATE, TIME '10:00', 'HONORED', CURRENT_TIMESTAMP, 'Commande remise au client.'),
    (2, 1, 2, 2, CURRENT_DATE, TIME '14:30', 'SCHEDULED', NULL, 'Conseil autour des produits locaux.'),
    (3, 1, 3, 3, CURRENT_DATE + 1, TIME '09:30', 'SCHEDULED', NULL, 'Panier découverte à préparer.');

INSERT INTO loyalty_transactions (shop_id, customer_id, appointment_id, points, reason, transaction_type)
VALUES
    (1, 1, 1, 100, 'Rendez-vous honoré', 'EARNED'),
    (1, 3, NULL, 200, 'Solde initial de démonstration', 'ADJUSTED');

ALTER SEQUENCE shops_id_seq RESTART WITH 2;
ALTER SEQUENCE customers_id_seq RESTART WITH 4;
ALTER SEQUENCE services_id_seq RESTART WITH 4;
ALTER SEQUENCE appointments_id_seq RESTART WITH 4;
ALTER SEQUENCE loyalty_transactions_id_seq RESTART WITH 3;
