INSERT INTO roles(name) VALUES('admin');
INSERT INTO roles(name) VALUES('super admin');
INSERT INTO roles(name) VALUES('super admin init');
INSERT INTO roles(name) VALUES('regular user');

-- Pera12345 pwd
INSERT INTO users(password, username, role_id, jwt)
VALUES
    ('$2a$12$/0.VQnx.7Ns0bd6Kd0Qpz.J11XIg8QKGS/TX4lRJZYtDXs/DVsXEK', 'nemanja.majstorovic3214@gmail.com', 4, NULL),
    ('$2a$12$/0.VQnx.7Ns0bd6Kd0Qpz.J11XIg8QKGS/TX4lRJZYtDXs/DVsXEK', 'nemanja.dutina@gmail.com', 4, NULL),
    ('$2a$12$/0.VQnx.7Ns0bd6Kd0Qpz.J11XIg8QKGS/TX4lRJZYtDXs/DVsXEK', 'milica.sladakovic@gmail.com', 4, NULL),
    ('$2a$12$/0.VQnx.7Ns0bd6Kd0Qpz.J11XIg8QKGS/TX4lRJZYtDXs/DVsXEK', 'thiaslsf@gmail.com', 4, NULL);

insert into regular_user(id, first_name, last_name, active) values ((SELECT id FROM users WHERE username = 'nemanja.majstorovic3214@gmail.com'), 'Nemanja', 'Majstorovic', true);
insert into regular_user(id, first_name, last_name, active) values ((SELECT id FROM users WHERE username = 'nemanja.dutina@gmail.com'), 'Nemanja', 'Dutina', true);
insert into regular_user(id, first_name, last_name, active) values ((SELECT id FROM users WHERE username = 'milica.sladakovic@gmail.com'), 'Milica', 'Sladakovic', true);
insert into regular_user(id, first_name, last_name, active) values ((SELECT id FROM users WHERE username = 'thiaslsf@gmail.com'), 'Mathias', 'Lessort', true);


-- Countries
INSERT INTO country(name)
VALUES
    ('BOSNIA AND HERZEGOVINA'),
    ('SERBIA'),
    ('CROATIA'),
    ('SLOVENIA'),
    ('NORTH MACEDONIA'),
    ('MONTENEGRO'),
    ('BULGARIA'),
    ('GREECE'),
    ('ROMANIA'),
    ('HUNGARY');


-- Cities
INSERT INTO city(name, country_id)
VALUES
    ('SARAJEVO', (SELECT id FROM country WHERE name = 'BOSNIA AND HERZEGOVINA')),
    ('BANJA LUKA', (SELECT id FROM country WHERE name = 'BOSNIA AND HERZEGOVINA')),
    ('MOSTAR', (SELECT id FROM country WHERE name = 'BOSNIA AND HERZEGOVINA')),
    ('TUZLA', (SELECT id FROM country WHERE name = 'BOSNIA AND HERZEGOVINA')),
    ('ZENICA', (SELECT id FROM country WHERE name = 'BOSNIA AND HERZEGOVINA')),
    ('BELGRADE', (SELECT id FROM country WHERE name = 'SERBIA')),
    ('NOVI SAD', (SELECT id FROM country WHERE name = 'SERBIA')),
    ('NIS', (SELECT id FROM country WHERE name = 'SERBIA')),
    ('KRAGUJEVAC', (SELECT id FROM country WHERE name = 'SERBIA')),
    ('SUBOTICA', (SELECT id FROM country WHERE name = 'SERBIA')),
    ('LJUBLJANA', (SELECT id FROM country WHERE name = 'SLOVENIA')),
    ('MARIBOR', (SELECT id FROM country WHERE name = 'SLOVENIA')),
    ('CELJE', (SELECT id FROM country WHERE name = 'SLOVENIA')),
    ('KRANJ', (SELECT id FROM country WHERE name = 'SLOVENIA')),
    ('NOVO MESTO', (SELECT id FROM country WHERE name = 'SLOVENIA')),
    ('SKOPJE', (SELECT id FROM country WHERE name = 'NORTH MACEDONIA')),
    ('BITOLA', (SELECT id FROM country WHERE name = 'NORTH MACEDONIA')),
    ('OHRID', (SELECT id FROM country WHERE name = 'NORTH MACEDONIA')),
    ('PRILEP', (SELECT id FROM country WHERE name = 'NORTH MACEDONIA')),
    ('TETOVO', (SELECT id FROM country WHERE name = 'NORTH MACEDONIA')),
    ('PODGORICA', (SELECT id FROM country WHERE name = 'MONTENEGRO')),
    ('NIKŠIĆ', (SELECT id FROM country WHERE name = 'MONTENEGRO')),
    ('PLJEVLJA', (SELECT id FROM country WHERE name = 'MONTENEGRO')),
    ('HERCEG NOVI', (SELECT id FROM country WHERE name = 'MONTENEGRO')),
    ('BAR', (SELECT id FROM country WHERE name = 'MONTENEGRO')),
    ('SOFIA', (SELECT id FROM country WHERE name = 'BULGARIA')),
    ('PLOVDIV', (SELECT id FROM country WHERE name = 'BULGARIA')),
    ('VARNA', (SELECT id FROM country WHERE name = 'BULGARIA')),
    ('BURGAS', (SELECT id FROM country WHERE name = 'BULGARIA')),
    ('RUSE', (SELECT id FROM country WHERE name = 'BULGARIA')),
    ('ATHENS', (SELECT id FROM country WHERE name = 'GREECE')),
    ('THESSALONIKI', (SELECT id FROM country WHERE name = 'GREECE')),
    ('PATRAS', (SELECT id FROM country WHERE name = 'GREECE')),
    ('HERAKLION', (SELECT id FROM country WHERE name = 'GREECE')),
    ('LARISSA', (SELECT id FROM country WHERE name = 'GREECE')),
    ('BUCHAREST', (SELECT id FROM country WHERE name = 'ROMANIA')),
    ('CLUJ-NAPOCA', (SELECT id FROM country WHERE name = 'ROMANIA')),
    ('TIMIȘOARA', (SELECT id FROM country WHERE name = 'ROMANIA')),
    ('IAȘI', (SELECT id FROM country WHERE name = 'ROMANIA')),
    ('CONSTANȚA', (SELECT id FROM country WHERE name = 'ROMANIA')),
    ('BUDAPEST', (SELECT id FROM country WHERE name = 'HUNGARY')),
    ('DEBRECEN', (SELECT id FROM country WHERE name = 'HUNGARY')),
    ('SZEGED', (SELECT id FROM country WHERE name = 'HUNGARY')),
    ('MISKOLC', (SELECT id FROM country WHERE name = 'HUNGARY')),
    ('PÉCS', (SELECT id FROM country WHERE name = 'HUNGARY'));

INSERT INTO real_estate(id, name, type, size, number_of_floors, city_id, address, latitude, longitude)
VALUES
    (1, 'Kuca', 0, 25.3, 1, (SELECT id FROM city WHERE name = 'NOVI SAD'), 'Bulevar oslobodjenja 54', 54.1, 21.3),
    (2, 'Villa', 0, 350.0, 2, (SELECT id FROM city WHERE name = 'BURGAS'), '123 Main St', 45.2671, 19.8335),
    (3, 'Apartment', 1, 85.5, 3, (SELECT id FROM city WHERE name = 'HERCEG NOVI'), 'Setaliste 5 Danica 31', 53.1, 20.1),
    (4, 'Office Building', 1, 500.0, 10, (SELECT id FROM city WHERE name = 'SARAJEVO'), 'Glavna 1', 54.415, 21.468),
    (5, 'Cottage', 0, 120.8, 1, (SELECT id FROM city WHERE name = 'NOVI SAD'), '123 Main St', 52.2671, 20.8335),
    (6, 'Commercial Space', 0, 150.5, 4, (SELECT id FROM city WHERE name = 'BAR'), '478 Elmo St', 52.46, 20.15),
    (7, 'Duplex', 1, 120.0, 2, (SELECT id FROM city WHERE name = 'BELGRADE'), '49 Main St', 52.12, 21.61),
    (8, 'Shopping Mall', 0, 800.0, 6, (SELECT id FROM city WHERE name = 'SKOPJE'), '123 Oak St', 51.26, 17.65),
    (9, 'Townhouse', 1, 200.3, 3, (SELECT id FROM city WHERE name = 'SUBOTICA'), '15 Elm St', 47.2671, 21.8335),
    (10, 'Warehouse', 1, 450.7, 1, (SELECT id FROM city WHERE name = 'BUCHAREST'), '3 Main St', 49.2671, 20.8335);

INSERT INTO real_estate_permission(type, real_estate_id, user_id)
VALUES
    (0,1,(SELECT id FROM users WHERE username = 'nemanja.majstorovic3214@gmail.com')),
    (0,2,(SELECT id FROM users WHERE username = 'nemanja.majstorovic3214@gmail.com')),
    (0,3,(SELECT id FROM users WHERE username = 'nemanja.dutina@gmail.com')),
    (0,4,(SELECT id FROM users WHERE username = 'nemanja.dutina@gmail.com')),
    (0,5,(SELECT id FROM users WHERE username = 'thiaslsf@gmail.com')),
    (0,6,(SELECT id FROM users WHERE username = 'nemanja.majstorovic3214@gmail.com')),
    (0,7,(SELECT id FROM users WHERE username = 'milica.sladakovic@gmail.com')),
    (0,8,(SELECT id FROM users WHERE username = 'thiaslsf@gmail.com')),
    (0,9,(SELECT id FROM users WHERE username = 'milica.sladakovic@gmail.com')),
    (0,10,(SELECT id FROM users WHERE username = 'thiaslsf@gmail.com'));

UPDATE real_estate set owner_real_estate_id = 1, owner_user_id = (SELECT id FROM users WHERE username = 'nemanja.majstorovic3214@gmail.com') where id = 1;
UPDATE real_estate set owner_real_estate_id = 2, owner_user_id = (SELECT id FROM users WHERE username = 'nemanja.majstorovic3214@gmail.com') where id = 2;
UPDATE real_estate set owner_real_estate_id = 3, owner_user_id = (SELECT id FROM users WHERE username = 'nemanja.dutina@gmail.com') where id = 3;
UPDATE real_estate set owner_real_estate_id = 4, owner_user_id = (SELECT id FROM users WHERE username = 'nemanja.dutina@gmail.com') where id = 4;
UPDATE real_estate set owner_real_estate_id = 5, owner_user_id = (SELECT id FROM users WHERE username = 'thiaslsf@gmail.com') where id = 5;
UPDATE real_estate set owner_real_estate_id = 6, owner_user_id = (SELECT id FROM users WHERE username = 'nemanja.majstorovic3214@gmail.com') where id = 6;
UPDATE real_estate set owner_real_estate_id = 7, owner_user_id = (SELECT id FROM users WHERE username = 'milica.sladakovic@gmail.com') where id = 7;
UPDATE real_estate set owner_real_estate_id = 8, owner_user_id = (SELECT id FROM users WHERE username = 'thiaslsf@gmail.com') where id = 8;
UPDATE real_estate set owner_real_estate_id = 9, owner_user_id = (SELECT id FROM users WHERE username = 'milica.sladakovic@gmail.com') where id = 9;
UPDATE real_estate set owner_real_estate_id = 10, owner_user_id = (SELECT id FROM users WHERE username = 'thiaslsf@gmail.com') where id = 10;

INSERT INTO real_estate_request(approved, declined, id, real_estate_id, comment)
VALUES
    (true, false, 1, 1, ''),
    (false, false, 2, 2, ''),
    (true, false, 3, 3, ''),
    (false, true, 4, 4, 'Ne svidja mi se'),
    (true, false, 5, 5, ''),
    (false, true, 6, 6, 'Imas previse nekretnina'),
    (false, false, 7, 7, ''),
    (true, false, 8, 8, ''),
    (true, false, 9, 9, ''),
    (false, false, 10, 10, '');

UPDATE real_estate set request_id = 1 where id = 1;
UPDATE real_estate set request_id = 2 where id = 2;
UPDATE real_estate set request_id = 3 where id = 3;
UPDATE real_estate set request_id = 4 where id = 4;
UPDATE real_estate set request_id = 5 where id = 5;
UPDATE real_estate set request_id = 6 where id = 6;
UPDATE real_estate set request_id = 7 where id = 7;
UPDATE real_estate set request_id = 8 where id = 8;
UPDATE real_estate set request_id = 9 where id = 9;
UPDATE real_estate set request_id = 10 where id = 10;

-- Ambient sensors
INSERT INTO device(id, name, real_estate_id, user_id, energy_consumption, power_supply_type, status)
VALUES
    (1, 'Senzor 1', 1, (SELECT id FROM users WHERE username = 'nemanja.majstorovic3214@gmail.com'), 1.5, 0, 0),
    (2, 'Senzor 2', 2, (SELECT id FROM users WHERE username = 'nemanja.majstorovic3214@gmail.com'), 1.0, 0, 0),
    (3, 'Senzor 3', 6, (SELECT id FROM users WHERE username = 'nemanja.majstorovic3214@gmail.com'), 0.5, 1, 0),
    (4, 'Senzor 4', 1, (SELECT id FROM users WHERE username = 'nemanja.majstorovic3214@gmail.com'), 0.7, 1, 0),
    (5, 'Senzor 5', 5, (SELECT id FROM users WHERE username = 'thiaslsf@gmail.com'), 0.8, 0, 0),
    (6, 'Senzor 6', 7, (SELECT id FROM users WHERE username = 'milica.sladakovic@gmail.com'), 1.3, 1, 0),
    (7, 'Senzor 7', 10, (SELECT id FROM users WHERE username = 'thiaslsf@gmail.com'), 1.2, 0, 0);

INSERT INTO ambient_sensor(id)
VALUES
    (1),
    (2),
    (3),
    (4),
    (5),
    (6),
    (7);

-- AC
INSERT INTO device(id, name, real_estate_id, user_id, energy_consumption, power_supply_type, status)
VALUES
    (11, 'Klima 1', 1, (SELECT id FROM users WHERE username = 'nemanja.majstorovic3214@gmail.com'), 3.1, 0, 0),
    (12, 'Klima 2', 9, (SELECT id FROM users WHERE username = 'milica.sladakovic@gmail.com'), 2.5, 0, 0),
    (13, 'Klima 3', 2, (SELECT id FROM users WHERE username = 'nemanja.majstorovic3214@gmail.com'), 3.7, 1, 0),
    (14, 'Klima 4', 7, (SELECT id FROM users WHERE username = 'milica.sladakovic@gmail.com'), 2.9, 1, 0),
    (15, 'Klima 5', 5, (SELECT id FROM users WHERE username = 'thiaslsf@gmail.com'), 3.8, 0, 0),
    (16, 'Klima 6', 4, (SELECT id FROM users WHERE username = 'nemanja.dutina@gmail.com'), 2.8, 1, 0),
    (17, 'Klima 7', 10, (SELECT id FROM users WHERE username = 'thiaslsf@gmail.com'), 3.4, 0, 0);

INSERT INTO air_conditioning(id, min_temperature, max_temperature, supported_modes)
VALUES
    (11, 0, 30, ARRAY [0,1]),
    (12, 10, 25, ARRAY [1,2]),
    (13, 5, 27, ARRAY [0,1,2]),
    (14, 14, 25, ARRAY [0,2]),
    (15, 16, 28, ARRAY [0,3]),
    (16, 12, 26, ARRAY [1,3]),
    (17, 19, 29, ARRAY [1,2,3]);

-- WASHING MACHINE
INSERT INTO device(id, name, real_estate_id, user_id, energy_consumption, power_supply_type, status)
VALUES
    (21, 'Ves masina 1', 3, (SELECT id FROM users WHERE username = 'nemanja.dutina@gmail.com'), 2.1, 0, 0),
    (22, 'Ves masina 2', 4, (SELECT id FROM users WHERE username = 'nemanja.dutina@gmail.com'), 2.5, 0, 0),
    (23, 'Ves masina 3', 5, (SELECT id FROM users WHERE username = 'thiaslsf@gmail.com'), 2.7, 1, 0),
    (24, 'Ves masina 4', 9, (SELECT id FROM users WHERE username = 'milica.sladakovic@gmail.com'), 1.9, 1, 0),
    (25, 'Ves masina 5', 1, (SELECT id FROM users WHERE username = 'nemanja.majstorovic3214@gmail.com'), 2.8, 0, 0),
    (26, 'Ves masina 6', 6, (SELECT id FROM users WHERE username = 'nemanja.majstorovic3214@gmail.com'), 1.9, 1, 0),
    (27, 'Ves masina 7', 7, (SELECT id FROM users WHERE username = 'milica.sladakovic@gmail.com'), 2.4, 0, 0);

INSERT INTO washing_machine(id, supported_mods)
VALUES
    (21, ARRAY [0,1]),
    (22, ARRAY [1]),
    (23, ARRAY [0]),
    (24, ARRAY [0,1]),
    (25, ARRAY [0,1]),
    (26, ARRAY [1]),
    (27, ARRAY [1]);

-- LAMP
INSERT INTO device(id, name, real_estate_id, user_id, energy_consumption, power_supply_type, status)
VALUES
    (31, 'Lampa 1', 2, (SELECT id FROM users WHERE username = 'nemanja.majstorovic3214@gmail.com'), 0.5, 0, 0),
    (32, 'Lampa 2', 3, (SELECT id FROM users WHERE username = 'nemanja.dutina@gmail.com'), 0.2, 0, 0),
    (33, 'Lampa 3', 4, (SELECT id FROM users WHERE username = 'nemanja.dutina@gmail.com'), 0.4, 1, 0),
    (34, 'Lampa 4', 1, (SELECT id FROM users WHERE username = 'nemanja.majstorovic3214@gmail.com'), 0.7, 1, 0),
    (35, 'Lampa 5', 10, (SELECT id FROM users WHERE username = 'thiaslsf@gmail.com'), 0.3, 0, 0),
    (36, 'Lampa 6', 9, (SELECT id FROM users WHERE username = 'milica.sladakovic@gmail.com'), 0.3, 1, 0),
    (37, 'Lampa 7', 8, (SELECT id FROM users WHERE username = 'thiaslsf@gmail.com'), 0.2, 0, 0);

INSERT INTO lamp(id)
VALUES
    (31),
    (32),
    (33),
    (34),
    (35),
    (36),
    (37);

-- SPRINKLER SYSTEM
INSERT INTO device(id, name, real_estate_id, user_id, energy_consumption, power_supply_type, status)
VALUES
    (41, 'Prskalice 1', 2, (SELECT id FROM users WHERE username = 'nemanja.majstorovic3214@gmail.com'), 2.5, 0, 0),
    (42, 'Prskalice 2', 7, (SELECT id FROM users WHERE username = 'milica.sladakovic@gmail.com'), 2.2, 0, 0),
    (43, 'Prskalice 3', 4, (SELECT id FROM users WHERE username = 'nemanja.dutina@gmail.com'), 2.4, 1, 0),
    (44, 'Prskalice 4', 6, (SELECT id FROM users WHERE username = 'nemanja.majstorovic3214@gmail.com'), 2.7, 1, 0),
    (45, 'Prskalice 5', 5, (SELECT id FROM users WHERE username = 'thiaslsf@gmail.com'), 2.3, 0, 0),
    (46, 'Prskalice 6', 9, (SELECT id FROM users WHERE username = 'milica.sladakovic@gmail.com'), 2.3, 1, 0),
    (47, 'Prskalice 7', 10, (SELECT id FROM users WHERE username = 'thiaslsf@gmail.com'), 2.2, 0, 0);

INSERT INTO sprinkler_system(id)
VALUES
    (41),
    (42),
    (43),
    (44),
    (45),
    (46),
    (47);

-- VEHICLE GATE
INSERT INTO device(id, name, real_estate_id, user_id, energy_consumption, power_supply_type, status)
VALUES
    (51, 'Vrata za vozila 1', 10, (SELECT id FROM users WHERE username = 'thiaslsf@gmail.com'), 4.1, 0, 0),
    (52, 'Vrata za vozila 2', 4, (SELECT id FROM users WHERE username = 'nemanja.dutina@gmail.com'), 4.5, 0, 0),
    (53, 'Vrata za vozila 3', 8, (SELECT id FROM users WHERE username = 'thiaslsf@gmail.com'), 4.7, 1, 0),
    (54, 'Vrata za vozila 4', 7, (SELECT id FROM users WHERE username = 'milica.sladakovic@gmail.com'), 3.9, 1, 0),
    (55, 'Vrata za vozila 5', 1, (SELECT id FROM users WHERE username = 'nemanja.majstorovic3214@gmail.com'), 3.8, 0, 0),
    (56, 'Vrata za vozila 6', 2, (SELECT id FROM users WHERE username = 'nemanja.majstorovic3214@gmail.com'), 3.9, 1, 0),
    (57, 'Vrata za vozila 7', 9, (SELECT id FROM users WHERE username = 'milica.sladakovic@gmail.com'), 4.4, 0, 0);

INSERT INTO vehicle_gate(id, current_mode, allowed_vehicles)
VALUES
    (51, 0, ARRAY ['AS546SA','HT486DS']),
    (52, 1, ARRAY ['AG954SS']),
    (53, 0, ARRAY ['UK589FR','ON458PO','YV486DR']),
    (54, 0, ARRAY ['WF489JB','BG168RH']),
    (55, 0, ARRAY ['AS115AS','UW853AF']),
    (56, 1, ARRAY ['BR363DV']),
    (57, 0, ARRAY ['AV998AF', 'EP840MU']);

-- SOLAR PANEL SYSTEM
INSERT INTO device(id, name, real_estate_id, user_id, energy_consumption, power_supply_type, status)
VALUES
    (61, 'Paneli 1', 2, (SELECT id FROM users WHERE username = 'nemanja.majstorovic3214@gmail.com'), 0.1, 0, 0),
    (62, 'Paneli 2', 9, (SELECT id FROM users WHERE username = 'milica.sladakovic@gmail.com'), 0.2, 0, 0),
    (63, 'Paneli 3', 6, (SELECT id FROM users WHERE username = 'nemanja.majstorovic3214@gmail.com'), 0.1, 1, 0),
    (64, 'Paneli 4', 7, (SELECT id FROM users WHERE username = 'milica.sladakovic@gmail.com'), 0.1, 1, 0),
    (65, 'Paneli 5', 10, (SELECT id FROM users WHERE username = 'thiaslsf@gmail.com'), 0.2, 0, 0),
    (66, 'Paneli 6', 3, (SELECT id FROM users WHERE username = 'nemanja.dutina@gmail.com'), 0.1, 1, 0),
    (67, 'Paneli 7', 8, (SELECT id FROM users WHERE username = 'thiaslsf@gmail.com'), 0.2, 0, 0);

INSERT INTO solar_panel_system(id, size, efficiency)
VALUES
    (61, 15, 60),
    (62, 10, 65),
    (63, 17, 29),
    (64, 13, 40),
    (65, 19, 41),
    (66, 25, 35),
    (67, 33, 49);

-- HOUSE BATTERY
INSERT INTO device(id, name, real_estate_id, user_id, energy_consumption, power_supply_type, status)
VALUES
    (71, 'Baterija 1', 1, (SELECT id FROM users WHERE username = 'nemanja.majstorovic3214@gmail.com'), 0.0, 0, 0),
    (72, 'Baterija 2', 3, (SELECT id FROM users WHERE username = 'nemanja.dutina@gmail.com'), 0.0, 0, 0),
    (73, 'Baterija 3', 8, (SELECT id FROM users WHERE username = 'thiaslsf@gmail.com'), 0.0, 1, 0),
    (74, 'Baterija 4', 7, (SELECT id FROM users WHERE username = 'milica.sladakovic@gmail.com'), 0.0, 1, 0),
    (75, 'Baterija 5', 10, (SELECT id FROM users WHERE username = 'thiaslsf@gmail.com'), 0.0, 0, 0),
    (76, 'Baterija 6', 4, (SELECT id FROM users WHERE username = 'nemanja.dutina@gmail.com'), 0.0, 1, 0),
    (77, 'Baterija 7', 5, (SELECT id FROM users WHERE username = 'thiaslsf@gmail.com'), 0.0, 0, 0);

INSERT INTO house_battery(id, size)
VALUES
    (71, 60),
    (72, 65),
    (73, 49),
    (74, 40),
    (75, 41),
    (76, 55),
    (77, 69);

-- VEHICLE CHARGER
INSERT INTO device(id, name, real_estate_id, user_id, energy_consumption, power_supply_type, status)
VALUES
    (71, 'Punjac 1', 2, (SELECT id FROM users WHERE username = 'nemanja.majstorovic3214@gmail.com'), 4.0, 0, 0),
    (72, 'Punjac 2', 5, (SELECT id FROM users WHERE username = 'thiaslsf@gmail.com'), 4.6, 0, 0),
    (73, 'Punjac 3', 6, (SELECT id FROM users WHERE username = 'nemanja.majstorovic3214@gmail.com'), 4.1, 1, 0),
    (74, 'Punjac 4', 7, (SELECT id FROM users WHERE username = 'milica.sladakovic@gmail.com'), 4.9, 1, 0),
    (75, 'Punjac 5', 10, (SELECT id FROM users WHERE username = 'thiaslsf@gmail.com'), 5.2, 0, 0),
    (76, 'Punjac 6', 1, (SELECT id FROM users WHERE username = 'nemanja.majstorovic3214@gmail.com'), 6.1, 1, 0),
    (77, 'Punjac 7', 8, (SELECT id FROM users WHERE username = 'thiaslsf@gmail.com'), 4.2, 0, 0);

INSERT INTO electric_vehicle_charger(id, num_of_slots, charge_power)
VALUES
    (71, 5, 60),
    (72, 1, 65),
    (73, 2, 29),
    (74, 3, 40),
    (75, 1, 41),
    (76, 2, 35),
    (77, 3, 49);