BEGIN TRANSACTION;

INSERT INTO tlrutinas(nombre, dias)
VALUES ('Rutina FullBody', 3);

INSERT INTO tlejercicios(nombre, dia, nom_dia, idrutinas, series, repes, peso, orden) VALUES
('Press inclinado', 1, 'Pecho', 1, 3, 10, 27.5, 1),
('Press plano', 1, 'Pecho', 1, 3, 10, 27.5, 2),
('Máquina pecho', 1, 'Pecho', 1, 3, 10, 27, 3),
('Poleas pecho arriba', 1, 'Pecho', 1, 3, 10, 12.5, 4),
('Poleas pecho abajo', 1, 'Pecho', 1, 3, 10, 12.5, 5),
('Super serie hombro', 1, 'Pecho', 1, 3, 10, 65, 6),
('Tríceps polea', 1, 'Pecho', 1, 3, 10, 30, 7),
('Máquina tríceps', 1, 'Pecho', 1, 3, 10, 65, 8);

INSERT INTO tlejercicios(nombre, dia, nom_dia, idrutinas, series, repes, peso, orden) VALUES
('Dominadas', 2, 'Espalda', 1, 3, 8, -1, 1),
('Jalón pecho', 2, 'Espalda', 1, 3, 10, 63.5, 2),
('Remo máquina', 2, 'Espalda', 1, 3, 10, 55, 3),
('Jalón remo', 2, 'Espalda', 1, 3, 10, 55, 4),
('Bíceps sentado', 2, 'Espalda', 1, 3, 10, 17, 5),
('Martillo', 2, 'Espalda', 1, 3, 10, 20, 6),
('Bíceps máquina', 2, 'Espalda', 1, 3, 10, 20, 7),
('Trapecio', 2, 'Espalda', 1, 3, 10, 32.5, 8);

INSERT INTO tlejercicios(nombre, dia, nom_dia, idrutinas, series, repes, peso, orden) VALUES
('Prensa', 3, 'Piernas', 1, 3, 10, 80, 1),
('Haka', 3, 'Piernas', 1, 3, 10, 110, 2),
('Extensión cuádriceps', 3, 'Piernas', 1, 4, 10, 60, 3),
('Femoral', 3, 'Piernas', 1, 3, 10, 45, 4),
('Glúteo', 3, 'Piernas', 1, 3, 10, 53, 5),
('Gemelo', 3, 'Piernas', 1, 3, 10, 90, 6),
('Abductores', 3, 'Piernas', 1, 3, 10, 75, 7);

INSERT INTO tlhistorial(idejercicio, repes, peso, date) VALUES
(1, 10, 27.5, '08/05/2025'),
(2, 10, 27.5, '08/05/2025'),
(3, 10, 27, '08/05/2025'),
(4, 10, 12.5, '08/05/2025'),
(5, 10, 12.5, '08/05/2025'),
(6, 10, 65, '08/05/2025'),
(7, 10, 30, '08/05/2025'),
(8, 10, 65, '08/05/2025'),
(10, 10, 63.5, '08/05/2025'),
(11, 10, 55, '08/05/2025'),
(12, 10, 55, '08/05/2025'),
(13, 10, 17, '08/05/2025'),
(14, 10, 20, '08/05/2025'),
(15, 10, 20, '08/05/2025'),
(16, 10, 32.5, '08/05/2025'),
(17, 10, 80, '08/05/2025'),
(18, 10, 110, '08/05/2025'),
(19, 10, 60, '08/05/2025'),
(20, 10, 45, '08/05/2025'),
(21, 10, 53, '08/05/2025'),
(22, 10, 90, '08/05/2025'),
(27, 10, 75, '08/05/2025');

COMMIT;