BEGIN TRANSACTION;

ALTER TABLE tlejercicios RENAME TO tlejercicios_old;

CREATE TABLE tlejercicios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT,
    dia INTEGER,
    idrutinas INTEGER,
    series INTEGER,
    repes INTEGER,
    peso REAL
);

INSERT INTO tlejercicios (id, nombre, dia, idrutinas, series, repes, peso)
SELECT id, nombre, dia, idrutinas, series, repes, peso FROM tlejercicios_old;

DROP TABLE tlejercicios_old;

COMMIT;
