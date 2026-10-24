DROP DATABASE IF EXISTS biblioteca_escolar;
CREATE DATABASE biblioteca_escolar CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE biblioteca_escolar;

CREATE TABLE lector (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uq_lector_correo UNIQUE (correo)
) ENGINE = InnoDB;

CREATE TABLE libro (
    id BIGINT NOT NULL AUTO_INCREMENT,
    codigo VARCHAR(20) NOT NULL,
    titulo VARCHAR(150) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    ejemplares INT NOT NULL,
    ejemplares_disponibles INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uq_libro_codigo UNIQUE (codigo),
    CONSTRAINT ck_libro_ejemplares CHECK (ejemplares >= 1),
    CONSTRAINT ck_libro_disponibles CHECK (ejemplares_disponibles >= 0 AND ejemplares_disponibles <= ejemplares)
) ENGINE = InnoDB;

CREATE TABLE prestamo (
    id BIGINT NOT NULL AUTO_INCREMENT,
    libro_id BIGINT NOT NULL,
    lector_id BIGINT NOT NULL,
    fecha_prestamo DATE NOT NULL,
    fecha_limite DATE NOT NULL,
    fecha_devolucion DATE NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_prestamo_libro FOREIGN KEY (libro_id) REFERENCES libro (id),
    CONSTRAINT fk_prestamo_lector FOREIGN KEY (lector_id) REFERENCES lector (id),
    CONSTRAINT ck_prestamo_limite CHECK (fecha_limite >= fecha_prestamo),
    CONSTRAINT ck_prestamo_devolucion CHECK (fecha_devolucion IS NULL OR fecha_devolucion >= fecha_prestamo),
    INDEX idx_prestamo_pendientes (fecha_devolucion, fecha_limite)
) ENGINE = InnoDB;

INSERT INTO lector (nombre, correo) VALUES
    ('Ana Torres Díaz', 'ana.torres@ejemplo.com'),
    ('Luis Ramírez Soto', 'luis.ramirez@ejemplo.com'),
    ('María Quispe León', 'maria.quispe@ejemplo.com'),
    ('Carlos Vega Rojas', 'carlos.vega@ejemplo.com'),
    ('Rosa Huamán Paz', 'rosa.huaman@ejemplo.com');

INSERT INTO libro (codigo, titulo, autor, ejemplares, ejemplares_disponibles) VALUES
    ('LIB-001', 'Cien años de soledad', 'Gabriel García Márquez', 3, 1),
    ('LIB-002', 'El Principito', 'Antoine de Saint-Exupéry', 2, 1),
    ('LIB-003', 'Los ríos profundos', 'José María Arguedas', 2, 2),
    ('LIB-004', 'Conversación en La Catedral', 'Mario Vargas Llosa', 1, 0),
    ('LIB-005', 'Tradiciones peruanas', 'Ricardo Palma', 2, 2),
    ('LIB-006', 'Don Quijote de la Mancha', 'Miguel de Cervantes', 2, 1),
    ('LIB-007', 'La ciudad y los perros', 'Mario Vargas Llosa', 2, 2),
    ('LIB-008', 'Aves sin nido', 'Clorinda Matto de Turner', 1, 1);

INSERT INTO prestamo (libro_id, lector_id, fecha_prestamo, fecha_limite, fecha_devolucion) VALUES
    (1, 1, DATE_SUB(CURDATE(), INTERVAL 15 DAY), DATE_SUB(CURDATE(), INTERVAL 8 DAY), NULL),
    (2, 2, DATE_SUB(CURDATE(), INTERVAL 3 DAY), DATE_ADD(CURDATE(), INTERVAL 4 DAY), NULL),
    (4, 3, DATE_SUB(CURDATE(), INTERVAL 10 DAY), DATE_SUB(CURDATE(), INTERVAL 3 DAY), NULL),
    (1, 4, DATE_SUB(CURDATE(), INTERVAL 2 DAY), DATE_ADD(CURDATE(), INTERVAL 5 DAY), NULL),
    (3, 1, DATE_SUB(CURDATE(), INTERVAL 20 DAY), DATE_SUB(CURDATE(), INTERVAL 13 DAY), DATE_SUB(CURDATE(), INTERVAL 15 DAY)),
    (5, 5, DATE_SUB(CURDATE(), INTERVAL 12 DAY), DATE_SUB(CURDATE(), INTERVAL 5 DAY), DATE_SUB(CURDATE(), INTERVAL 6 DAY)),
    (6, 2, DATE_SUB(CURDATE(), INTERVAL 5 DAY), DATE_ADD(CURDATE(), INTERVAL 2 DAY), NULL);
