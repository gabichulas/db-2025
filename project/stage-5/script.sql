CREATE SCHEMA triplog;

CREATE TABLE triplog.USUARIO (
    id_usuario SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    email TEXT NOT NULL UNIQUE,
    contrasena TEXT NOT NULL UNIQUE
);

CREATE TABLE triplog.VIAJE (
    id_viaje SERIAL PRIMARY KEY,
    titulo VARCHAR(50) NOT NULL,
    destino VARCHAR(100) NOT NULL,
    fecha_inicio DATE,
    fecha_fin DATE,
    id_creador INTEGER REFERENCES triplog.USUARIO(id_usuario) ON DELETE CASCADE
);

CREATE TABLE triplog.PARTICIPA (
    id_viaje INTEGER REFERENCES triplog.VIAJE(id_viaje) ON DELETE CASCADE,
    id_usuario INTEGER REFERENCES triplog.USUARIO(id_usuario) ON DELETE CASCADE,
    es_organizador BOOLEAN NOT NULL,
    PRIMARY KEY (id_viaje, id_usuario)
);

CREATE TABLE triplog.LUGAR (
    id_lugar SERIAL PRIMARY KEY,
    nombre TEXT NOT NULL,
    direccion TEXT,
    fecha_visita DATE
);

CREATE TABLE triplog.UBICACION (
    id_viaje INTEGER REFERENCES triplog.VIAJE(id_viaje) ON DELETE CASCADE,
    id_lugar INTEGER REFERENCES triplog.LUGAR(id_lugar) ON DELETE CASCADE,
    PRIMARY KEY (id_viaje, id_lugar)
);

CREATE TABLE triplog.TAREA (
    id_tarea SERIAL PRIMARY KEY,
    descripcion VARCHAR(300) NOT NULL,
    completada BOOLEAN NOT NULL,
    fecha_limite DATE,
    id_viaje INTEGER REFERENCES triplog.VIAJE(id_viaje) ON DELETE CASCADE,
    id_usuario_asignado INTEGER REFERENCES triplog.USUARIO(id_usuario) ON DELETE SET NULL
);

CREATE TABLE triplog.GASTO (
    id_gasto SERIAL PRIMARY KEY,
    monto DECIMAL(10, 2) NOT NULL,
    descripcion VARCHAR(300) NOT NULL
);

CREATE TABLE triplog.GASTA (
    id_viaje INTEGER REFERENCES triplog.VIAJE(id_viaje) ON DELETE CASCADE,
    id_usuario INTEGER REFERENCES triplog.USUARIO(id_usuario) ON DELETE CASCADE,
    id_gasto INTEGER REFERENCES triplog.GASTO(id_gasto) ON DELETE CASCADE,
    PRIMARY KEY (id_usuario, id_gasto)
);

-- 1. Insertar 15 usuarios con contrasenas hasheadas (bcrypt)
INSERT INTO triplog.USUARIO (nombre, email, contrasena) VALUES
('Juan Perez', 'juan.perez@triplog.com', '$2a$10$a./gqAxBDlDvikPHH8cK/OkP/AhliuLoC/xH6oNy2exwhtnPF1npG'),
('Maria Garcia', 'maria.garcia@triplog.com', '$2a$10$lBKI9gzKMUMXY0PPX7sN3.Becx2mMwjDuoKM5iM6zDdXntuxUmOFi'),
('Carlos Lopez', 'carlos.lopez@triplog.com', '$2a$10$yTMkBb3T/AlXH4JO8vIl/.OSnl8MC/IiBKteP1uE12gDQnnD91Sra'),
('Ana Martinez', 'ana.martinez@triplog.com', '$2a$10$U.1K.VLiXyakdFFInMZyf.kM.swye7MS2lK5FUfCNGHGA3Q1Hvr8y'),
('Pedro Sanchez', 'pedro.sanchez@triplog.com', '$2a$10$4A14LZNjdQ2i/ANhsx7dHOs6mlE0W5F4eI64Tecnx1oDttOE.nkIe'),
('Lucia Fernandez', 'lucia.fernandez@triplog.com', '$2a$10$0wDM.oiD82OJy8e6V9OaCe8JYYDtrTS4ghxid6no6XxJAykJ/6e76'),
('Diego Rodriguez', 'diego.rodriguez@triplog.com', '$2a$10$LdWkRae8/bemELezD1c3q.oAuih/9AxfDTgqOBZ9E.Cl3ygPsZVVS'),
('Sofia Gonzalez', 'sofia.gonzalez@triplog.com', '$2a$10$wHsR..RzG8VR6r298GF/8.e1ZhRQEbBrF0RQQ/kq0PLItgNSwYuPy'),
('Martin Diaz', 'martin.diaz@triplog.com', '$2a$10$JWLwKtpT8jHVo/1sLepS4eWze51pW1SmRDYrn0eM66kzdJ5ZVkYKK'),
('Valentina Morales', 'valentina.morales@triplog.com', '$2a$10$flr6g6qhP2NDamfBqsjNEuBUE3.ehVb2g7Wf8OTeVpMMpTuqxg9Ii'),
('Facundo Luna', 'facundo.luna@triplog.com', '$2a$10$L4vUDclirXcGox8EH7krP.gEtQowZ9RXNN6W8x97zywHHDpdqQbPG'),
('Camila Herrera', 'camila.herrera@triplog.com', '$2a$10$URaO..mltAkGXRQ65LUkvOLRjFpQAwUTnefM5Rlws68K.MKynSxbO'),
('Gonzalo Castro', 'gonzalo.castro@triplog.com', '$2a$10$.alXtENIMJGAlIYSAE8NHe0h8Wzqrxeu1..ljO4UaSv5o0MKVJNo.'),
('Florencia Rios', 'florencia.rios@triplog.com', '$2a$10$3fpIpPD0V0F58XW3PybVY.W0ZuB.uutlsgRxIK8h0VvRdjMzkhyK.'),
('Lautaro Mendez', 'lautaro.mendez@triplog.com', '$2a$10$bGbM.a2jiabbv3OfkRyII.RpzaW1W953/Jn6ZSttgnDRPYj7VR2JC');

-- 2. Insertar 10 viajes con diversos destinos y fechas
INSERT INTO triplog.VIAJE (titulo, destino, fecha_inicio, fecha_fin, id_creador) VALUES
('Aventura en Bariloche', 'San Carlos de Bariloche', '2023-12-15', '2023-12-22', 1),
('Fin de semana en Cordoba', 'Villa Carlos Paz', '2024-01-20', '2024-01-23', 2),
('Explorando Mendoza', 'Mendoza', '2024-02-10', '2024-02-17', 3),
('Playas en Mar del Plata', 'Mar del Plata', '2024-03-05', '2024-03-12', 1),
('Cultura en Buenos Aires', 'Buenos Aires', '2024-04-15', '2024-04-20', 4),
('Ecoturismo en Iguazu', 'Puerto Iguazu', '2024-05-10', '2024-05-17', 5),
('Aventura en Salta', 'Salta', '2024-06-01', '2024-06-10', 6),
('Nieve en Ushuaia', 'Ushuaia', '2024-07-15', '2024-07-25', 7),
('Ruta del vino en Cafayate', 'Cafayate', '2024-08-12', '2024-08-18', 8),
('Carnaval en Gualeguaychu', 'Gualeguaychu', '2025-02-10', '2025-02-15', 9),
('Turismo historico en Rosario', 'Rosario', '2024-09-05', '2024-09-08', 10),
('Relax en Termas de Rio Hondo', 'Termas de Rio Hondo', '2024-10-20', '2024-10-27', 11),
('Aventura en El Chalten', 'El Chalten', '2024-11-15', '2024-11-25', 12),
('Turismo rural en San Antonio de Areco', 'San Antonio de Areco', '2024-12-10', '2024-12-12', 13),
('Ano nuevo en Pinamar', 'Pinamar', '2024-12-28', '2025-01-05', 14);

-- 3. Insertar participantes en los viajes (entre 3 y 8 participantes por viaje)
INSERT INTO triplog.PARTICIPA (id_viaje, id_usuario, es_organizador) VALUES
-- Viaje 1 (Bariloche)
(1, 1, TRUE), (1, 2, FALSE), (1, 3, FALSE), (1, 4, FALSE), (1, 5, FALSE),
-- Viaje 2 (Cordoba)
(2, 2, TRUE), (2, 4, FALSE), (2, 6, FALSE), (2, 8, FALSE),
-- Viaje 3 (Mendoza)
(3, 3, TRUE), (3, 1, FALSE), (3, 5, FALSE), (3, 7, FALSE), (3, 9, FALSE),
-- Viaje 4 (Mar del Plata)
(4, 1, TRUE), (4, 5, FALSE), (4, 10, FALSE),
-- Viaje 5 (Buenos Aires)
(5, 4, TRUE), (5, 2, FALSE), (5, 3, FALSE), (5, 6, FALSE), (5, 7, FALSE),
-- Viaje 6 (Iguazu)
(6, 5, TRUE), (6, 1, FALSE), (6, 3, FALSE), (6, 7, FALSE), (6, 9, FALSE), (6, 11, FALSE),
-- Viaje 7 (Salta)
(7, 6, TRUE), (7, 8, FALSE), (7, 10, FALSE), (7, 12, FALSE),
-- Viaje 8 (Ushuaia)
(8, 7, TRUE), (8, 9, FALSE), (8, 11, FALSE), (8, 13, FALSE), (8, 15, FALSE),
-- Viaje 9 (Cafayate)
(9, 8, TRUE), (9, 10, FALSE), (9, 12, FALSE), (9, 14, FALSE),
-- Viaje 10 (Gualeguaychu)
(10, 9, TRUE), (10, 11, FALSE), (10, 13, FALSE), (10, 15, FALSE),
-- Viaje 11 (Rosario)
(11, 10, TRUE), (11, 12, FALSE), (11, 14, FALSE),
-- Viaje 12 (Termas de Rio Hondo)
(12, 11, TRUE), (12, 13, FALSE), (12, 15, FALSE), (12, 2, FALSE), (12, 4, FALSE),
-- Viaje 13 (El Chalten)
(13, 12, TRUE), (13, 14, FALSE), (13, 1, FALSE), (13, 3, FALSE), (13, 5, FALSE),
-- Viaje 14 (San Antonio de Areco)
(14, 13, TRUE), (14, 15, FALSE), (14, 2, FALSE), (14, 4, FALSE),
-- Viaje 15 (Pinamar)
(15, 14, TRUE), (15, 1, FALSE), (15, 3, FALSE), (15, 5, FALSE), (15, 7, FALSE), (15, 9, FALSE), (15, 11, FALSE);

-- 4. Insertar lugares (5-8 lugares por viaje)
INSERT INTO triplog.LUGAR (nombre, direccion, fecha_visita) VALUES
-- Lugares para Bariloche (Viaje 1)
('Cerro Catedral', 'Av. Exequiel Bustillo 15000', '2023-12-16'),
('Lago Nahuel Huapi', '', '2023-12-17'),
('Colonia Suiza', 'Ruta 79 km 24', '2023-12-18'),
('Cerro Otto', 'Av. de los Pioneros km 5', '2023-12-19'),
('Circuito Chico', 'Ruta 77', '2023-12-20'),

-- Lugares para Cordoba (Viaje 2)
('Reloj Cucu', 'Av. Uruguay esq. Av. San Martin', '2024-01-21'),
('Dique San Roque', '', '2024-01-22'),
('Museo del Che Guevara', 'Av. Alta Gracia', '2024-01-22'),
('Cabana del Penon', '', '2024-01-23'),

-- Lugares para Mendoza (Viaje 3)
('Bodega Trapiche', 'Nueva Mayorga s/n, Coquimbito', '2024-02-12'),
('Cerro Aconcagua', 'Parque Provincial Aconcagua', '2024-02-14'),
('Parque General San Martin', 'Av. Emilio Civit 701', '2024-02-13'),
('Termas de Cacheuta', 'Ruta 82 km 38', '2024-02-15'),

-- Lugares para Mar del Plata (Viaje 4)
('Playa Grande', 'Av. Peralta Ramos 2500', '2024-03-07'),
('Puerto Mar del Plata', 'J. L. Borges 250', '2024-03-08'),
('Torre Tanque', 'Falucho 995', '2024-03-09'),
('Museo MAR', 'Av. Felix U. Camet 3510', '2024-03-10'),

-- Lugares para Buenos Aires (Viaje 5)
('Teatro Colon', 'Cerrito 628', '2024-04-16'),
('Caminito', 'Del Valle Iberlucea 800', '2024-04-17'),
('Recoleta', 'Junin 1760', '2024-04-18'),
('Tigre Delta', '', '2024-04-19'),

-- Lugares para Iguazu (Viaje 6)
('Cataratas del Iguazu', 'Parque Nacional Iguazu', '2024-05-11'),
('Garganta del Diablo', 'Parque Nacional Iguazu', '2024-05-12'),
('Hito Tres Fronteras', 'Av. Tres Fronteras 100', '2024-05-13'),
('Ruinas de San Ignacio', 'Ruta 12 km 57', '2024-05-14'),

-- Lugares para Salta (Viaje 7)
('Cerro San Bernardo', 'Av. San Martin 4400', '2024-06-02'),
('Tren a las Nubes', 'Estacion de Trenes', '2024-06-03'),
('Cafayate', 'Ruta 68', '2024-06-05'),
('Salinas Grandes', 'Ruta 52', '2024-06-07'),

-- Lugares para Ushuaia (Viaje 8)
('Glaciar Martial', 'Av. Luis Fernando Martial 3560', '2024-07-16'),
('Parque Nacional Tierra del Fuego', 'Ruta 3 km 3042', '2024-07-18'),
('Tren del Fin del Mundo', 'Estacion del Fin del Mundo', '2024-07-19'),
('Canal Beagle', 'Puerto Turistico', '2024-07-20'),

-- Lugares para Cafayate (Viaje 9)
('Bodega El Esteco', 'Calle San Martin 415', '2024-08-13'),
('Quebrada de las Conchas', 'Ruta 68', '2024-08-14'),
('Museo de la Vid y el Vino', 'Av. Guemes 1', '2024-08-15'),

-- Lugares para Gualeguaychu (Viaje 10)
('Corsodromo', 'Av. Parque 1500', '2025-02-11'),
('Termas de Gualeguaychu', 'Ruta 14 km 3', '2025-02-12'),
('Parque Unzue', 'Av. Parque s/n', '2025-02-13'),

-- Lugares para Rosario (Viaje 11)
('Monumento a la Bandera', 'Santa Fe 581', '2024-09-06'),
('Parque de la Independencia', 'Av. Pellegrini 1901', '2024-09-07'),
('La Florida', 'Av. Carrasco 3400', '2024-09-08'),

-- Lugares para Termas de Rio Hondo (Viaje 12)
('Termas de Rio Hondo', 'Av. Costanera s/n', '2024-10-21'),
('Autodromo', 'Ruta 9 km 795', '2024-10-22'),
('Dique Frontal', 'Av. Costanera', '2024-10-23'),

-- Lugares para El Chalten (Viaje 13)
('Cerro Fitz Roy', 'Parque Nacional Los Glaciares', '2024-11-16'),
('Laguna de los Tres', 'Sendero desde El Chalten', '2024-11-18'),
('Glaciar Viedma', 'Puerto Bahia Tunel', '2024-11-20'),

-- Lugares para San Antonio de Areco (Viaje 14)
('Museo Gauchesco Ricardo Guiraldes', 'Camino Ricardo Guiraldes s/n', '2024-12-11'),
('Puente Viejo', 'Zarate y Arellano', '2024-12-11'),
('Parque Criollo', 'Ruta 31 km 114', '2024-12-12'),

-- Lugares para Pinamar (Viaje 15)
('Playa de Pinamar', 'Av. Bunge y el mar', '2024-12-29'),
('Bosque de Pinamar', 'Av. Bunge 1550', '2024-12-30'),
('Carilo', 'Ruta 11 km 328', '2025-01-02'),
('Medanos Grandes', 'Ruta 11 km 312', '2025-01-03');

-- 5. Relacionar lugares con viajes (UBICACION)
INSERT INTO triplog.UBICACION (id_viaje, id_lugar) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5),
(2, 6), (2, 7), (2, 8), (2, 9),
(3, 10), (3, 11), (3, 12), (3, 13),
(4, 14), (4, 15), (4, 16), (4, 17),
(5, 18), (5, 19), (5, 20), (5, 21),
(6, 22), (6, 23), (6, 24), (6, 25),
(7, 26), (7, 27), (7, 28), (7, 29),
(8, 30), (8, 31), (8, 32), (8, 33),
(9, 34), (9, 35), (9, 36),
(10, 37), (10, 38), (10, 39),
(11, 40), (11, 41), (11, 42),
(12, 43), (12, 44), (12, 45),
(13, 46), (13, 47), (13, 48),
(14, 49), (14, 50), (14, 51),
(15, 52), (15, 53), (15, 54), (15, 55);

-- 6. Insertar tareas (3-5 tareas por viaje)
INSERT INTO triplog.TAREA (descripcion, completada, fecha_limite, id_viaje, id_usuario_asignado) VALUES
-- Tareas para Bariloche (Viaje 1)
('Reservar hotel', TRUE, '2023-11-30', 1, 1),
('Comprar pasajes de avion', TRUE, '2023-11-15', 1, 2),
('Alquilar equipo de ski', FALSE, '2023-12-10', 1, 3),
('Contratar seguro de viaje', TRUE, '2023-11-20', 1, 4),
('Planificar itinerario diario', FALSE, '2023-12-05', 1, 5),

-- Tareas para Cordoba (Viaje 2)
('Reservar cabana', TRUE, '2024-01-05', 2, 2),
('Planificar ruta de viaje', FALSE, '2024-01-15', 2, 4),
('Comprar entradas para museos', TRUE, '2024-01-10', 2, 6),

-- Tareas para Mendoza (Viaje 3)
('Reservar tour en bodega', TRUE, '2024-01-25', 3, 3),
('Comprar entradas para parque', FALSE, '2024-02-05', 3, 1),
('Alquilar auto', TRUE, '2024-01-30', 3, 5),
('Reservar hotel en termas', FALSE, '2024-02-01', 3, 7),

-- Tareas para Mar del Plata (Viaje 4)
('Reservar departamento', TRUE, '2024-02-20', 4, 1),
('Hacer lista de cosas para playa', FALSE, NULL, 4, 5),
('Comprar protector solar', TRUE, '2024-03-01', 4, 10),

-- Tareas para Buenos Aires (Viaje 5)
('Comprar entradas para teatro', TRUE, '2024-03-20', 5, 4),
('Investigar restaurantes', FALSE, '2024-04-01', 5, 2),
('Reservar hotel centrico', TRUE, '2024-03-25', 5, 3),
('Planificar transporte urbano', FALSE, '2024-04-05', 5, 6),

-- Tareas para Iguazu (Viaje 6)
('Reservar hotel cerca del parque', TRUE, '2024-04-20', 6, 5),
('Comprar entradas para cataratas', TRUE, '2024-04-25', 6, 1),
('Contratar tour a las cataratas', FALSE, '2024-05-01', 6, 3),
('Investigar opciones de comida local', FALSE, NULL, 6, 7),

-- Tareas para Salta (Viaje 7)
('Reservar hotel en Salta ciudad', TRUE, '2024-05-15', 7, 6),
('Comprar pasajes de bus a Cafayate', TRUE, '2024-05-20', 7, 8),
('Reservar excursion a Salinas Grandes', FALSE, '2024-05-25', 7, 10),
('Investigar clima y ropa necesaria', FALSE, '2024-05-28', 7, 12),

-- Tareas para Ushuaia (Viaje 8)
('Reservar hotel con vista al canal', TRUE, '2024-06-20', 8, 7),
('Comprar pasajes aereos', TRUE, '2024-06-15', 8, 9),
('Reservar excursion al parque nacional', FALSE, '2024-07-01', 8, 11),
('Alquilar ropa termica', FALSE, '2024-07-05', 8, 13),
('Comprar seguro de viaje especial', TRUE, '2024-06-25', 8, 15),

-- Tareas para Cafayate (Viaje 9)
('Reservar hotel en Cafayate', TRUE, '2024-07-25', 9, 8),
('Reservar tours en bodegas', TRUE, '2024-08-01', 9, 10),
('Alquilar auto para recorrer quebradas', FALSE, '2024-08-05', 9, 12),

-- Tareas para Gualeguaychu (Viaje 10)
('Comprar entradas para el carnaval', TRUE, '2025-01-20', 10, 9),
('Reservar hotel cerca del corsodromo', FALSE, '2025-01-25', 10, 11),
('Planificar transporte desde Buenos Aires', FALSE, '2025-02-01', 10, 13),

-- Tareas para Rosario (Viaje 11)
('Reservar hotel cerca del rio', TRUE, '2024-08-20', 11, 10),
('Investigar museos y actividades', FALSE, '2024-08-25', 11, 12),
('Planificar recorrido del monumento', FALSE, '2024-09-01', 11, 14),

-- Tareas para Termas de Rio Hondo (Viaje 12)
('Reservar hotel con spa termal', TRUE, '2024-09-25', 12, 11),
('Comprar accesorios para piscina', FALSE, '2024-10-10', 12, 13),
('Investigar tratamientos termales', FALSE, NULL, 12, 15),

-- Tareas para El Chalten (Viaje 13)
('Reservar hosteria en El Chalten', TRUE, '2024-10-20', 13, 12),
('Alquilar equipo de trekking', FALSE, '2024-11-01', 13, 14),
('Contratar guia para el Fitz Roy', FALSE, '2024-11-05', 13, 1),
('Preparar botiquin de primeros auxilios', TRUE, '2024-11-10', 13, 3),

-- Tareas para San Antonio de Areco (Viaje 14)
('Reservar estancia para alojamiento', TRUE, '2024-11-25', 14, 13),
('Comprar entradas para museo gauchesco', FALSE, '2024-12-01', 14, 15),
('Planificar visita a talleres artesanales', FALSE, NULL, 14, 2),

-- Tareas para Pinamar (Viaje 15)
('Reservar casa para ano nuevo', TRUE, '2024-11-15', 15, 14),
('Comprar comida para la estadia', FALSE, '2024-12-20', 15, 1),
('Organizar transporte desde Buenos Aires', TRUE, '2024-12-15', 15, 3),
('Planificar actividades para ninos', FALSE, '2024-12-22', 15, 5),
('Reservar restaurante para ano nuevo', TRUE, '2024-12-10', 15, 7);

-- 7. Insertar gastos (3-5 gastos por viaje)
INSERT INTO triplog.GASTO (monto, descripcion) VALUES
-- Gastos para Bariloche (Viaje 1)
(120000.00, 'Hotel 7 noches'),
(85000.50, 'Pasajes aereos'),
(15000.75, 'Alquiler de equipo de ski'),
(5000.00, 'Seguro de viaje'),
(20000.00, 'Excursiones varias'),

-- Gastos para Cordoba (Viaje 2)
(45000.00, 'Cabana 3 noches'),
(8000.00, 'Combustible'),
(5000.00, 'Entradas a museos'),
(12000.00, 'Comidas'),

-- Gastos para Mendoza (Viaje 3)
(25000.00, 'Tour en bodega'),
(12000.00, 'Entradas parque Aconcagua'),
(30000.00, 'Alquiler de auto'),
(18000.00, 'Hotel en termas'),

-- Gastos para Mar del Plata (Viaje 4)
(68000.00, 'Departamento 7 noches'),
(5000.00, 'Sombrilla y reposeras'),
(3000.00, 'Protector solar'),
(15000.00, 'Comidas y helados'),

-- Gastos para Buenos Aires (Viaje 5)
(15000.00, 'Entradas Teatro Colon'),
(20000.00, 'Cena en restaurante'),
(40000.00, 'Hotel 5 noches'),
(8000.00, 'Transporte urbano'),

-- Gastos para Iguazu (Viaje 6)
(75000.00, 'Hotel 7 noches'),
(12000.00, 'Entradas al parque'),
(18000.00, 'Tour a las cataratas'),
(15000.00, 'Comidas regionales'),

-- Gastos para Salta (Viaje 7)
(55000.00, 'Hotel 9 noches'),
(10000.00, 'Pasajes a Cafayate'),
(15000.00, 'Excursion a Salinas'),
(20000.00, 'Comidas tipicas'),

-- Gastos para Ushuaia (Viaje 8)
(90000.00, 'Hotel 10 noches'),
(120000.00, 'Pasajes aereos'),
(25000.00, 'Excursion al parque'),
(15000.00, 'Alquiler ropa termica'),
(8000.00, 'Seguro especial'),

-- Gastos para Cafayate (Viaje 9)
(40000.00, 'Hotel 6 noches'),
(12000.00, 'Tours en bodegas'),
(25000.00, 'Alquiler de auto'),
(10000.00, 'Degustaciones'),

-- Gastos para Gualeguaychu (Viaje 10)
(30000.00, 'Entradas carnaval'),
(40000.00, 'Hotel 5 noches'),
(8000.00, 'Transporte'),

-- Gastos para Rosario (Viaje 11)
(35000.00, 'Hotel 3 noches'),
(5000.00, 'Entradas a museos'),
(10000.00, 'Comidas'),

-- Gastos para Termas de Rio Hondo (Viaje 12)
(60000.00, 'Hotel con spa 7 noches'),
(5000.00, 'Accesorios piscina'),
(15000.00, 'Tratamientos termales'),

-- Gastos para El Chalten (Viaje 13)
(50000.00, 'Hosteria 10 noches'),
(15000.00, 'Alquiler equipo trekking'),
(20000.00, 'Guia para excursion'),
(5000.00, 'Botiquin'),

-- Gastos para San Antonio de Areco (Viaje 14)
(45000.00, 'Estancia 2 noches'),
(3000.00, 'Entradas museo'),
(10000.00, 'Talleres artesanales'),

-- Gastos para Pinamar (Viaje 15)
(120000.00, 'Casa 8 noches'),
(30000.00, 'Comida para estadia'),
(15000.00, 'Transporte'),
(25000.00, 'Cena ano nuevo'),
(20000.00, 'Actividades ninos');

-- 8. Relacionar gastos con viajes y usuarios (GASTA)
INSERT INTO triplog.GASTA (id_viaje, id_usuario, id_gasto) VALUES
-- Bariloche (Viaje 1)
-- Hotel compartido entre 3 usuarios (gasto 1)
(1, 1, 1), (1, 2, 1), (1, 3, 1),
-- Pasajes individuales (gasto 2)
(1, 2, 2),
-- Alquiler equipo compartido 2 usuarios (gasto 3)
(1, 3, 3), (1, 4, 3),
-- Seguro individual (gasto 4)
(1, 5, 4),
-- Excursiones grupo completo (gasto 5)
(1, 1, 5), (1, 2, 5), (1, 3, 5), (1, 4, 5), (1, 5, 5),

-- Cordoba (Viaje 2)
-- Cabana compartida 2 usuarios (gasto 6)
(2, 2, 6), (2, 4, 6),
-- Combustible dividido 4 (gasto 7)
(2, 2, 7), (2, 4, 7), (2, 6, 7), (2, 8, 7),
-- Entradas individuales (gasto 8)
(2, 6, 8),
-- Cena grupal dividida 4 (gasto 9)
(2, 2, 9), (2, 4, 9), (2, 6, 9), (2, 8, 9),

-- Mendoza (Viaje 3)
-- Tour bodega grupo completo (gasto 10)
(3, 3, 10), (3, 1, 10), (3, 5, 10), (3, 7, 10), (3, 9, 10),
-- Entradas parque compartido 2 (gasto 11)
(3, 1, 11), (3, 5, 11),
-- Alquiler auto dividido 3 (gasto 12)
(3, 3, 12), (3, 5, 12), (3, 7, 12),
-- Hotel termas compartido 2 (gasto 13)
(3, 7, 13), (3, 9, 13),

-- Mar del Plata (Viaje 4)
(4, 1, 14), (4, 5, 15), (4, 10, 16), (4, 1, 17),
-- Buenos Aires (Viaje 5)
(5, 4, 18), (5, 2, 19), (5, 3, 20), (5, 6, 21),
-- Iguazu (Viaje 6)
(6, 5, 22), (6, 1, 23), (6, 3, 24), (6, 7, 25),
-- Salta (Viaje 7)
(7, 6, 26), (7, 8, 27), (7, 10, 28), (7, 12, 29),

-- Ushuaia (Viaje 8)
-- Hotel dividido 5 (gasto 30)
(8, 7, 30), (8, 9, 30), (8, 11, 30), (8, 13, 30), (8, 15, 30),
-- Pasajes individuales (gasto 31)
(8, 7, 31), (8, 9, 31), (8, 11, 31), (8, 13, 31), (8, 15, 31),
-- Excursión parque grupo completo (gasto 32)
(8, 7, 32), (8, 9, 32), (8, 11, 32), (8, 13, 32), (8, 15, 32),
-- Alquiler ropa compartido 2 (gasto 33)
(8, 11, 33), (8, 13, 33),
-- Seguro individual (gasto 34)
(8, 15, 34),

-- Cafayate (Viaje 9)
(9, 8, 35), (9, 10, 36), (9, 12, 37), (9, 14, 38),
-- Gualeguaychú (Viaje 10)
(10, 9, 39), (10, 11, 40), (10, 13, 41),
-- Rosario (Viaje 11)
(11, 10, 42), (11, 12, 43), (11, 14, 44),
-- Termas de Río Hondo (Viaje 12)
(12, 11, 45), (12, 13, 46), (12, 15, 47),
-- El Chaltén (Viaje 13)
(13, 12, 48), (13, 14, 49), (13, 1, 50), (13, 3, 51),
-- San Antonio de Areco (Viaje 14)
(14, 13, 52), (14, 15, 53), (14, 2, 54),

-- Pinamar (Viaje 15)
-- Casa dividida 6 (gasto 55)
(15, 14, 55), (15, 1, 55), (15, 3, 55), (15, 5, 55), (15, 7, 55), (15, 9, 55),
-- Comida grupo completo (gasto 56)
(15, 14, 56), (15, 1, 56), (15, 3, 56), (15, 5, 56), (15, 7, 56), (15, 9, 56), (15, 11, 56),
-- Transporte dividido 3 (gasto 57)
(15, 1, 57), (15, 3, 57), (15, 5, 57),
-- Cena año nuevo dividido 4 (gasto 58)
(15, 14, 58), (15, 3, 58), (15, 7, 58), (15, 9, 58),
-- Actividades niños compartido 2 (gasto 59)
(15, 5, 59), (15, 7, 59);