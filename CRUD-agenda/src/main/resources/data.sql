INSERT IGNORE INTO usuarios (activo, correo, documento, nombre_completo) VALUES 
(1, 'santiago@sena.edu.co', '1000111222', 'Santiago Pérez'),
(1, 'julia@sena.edu.co', '1000333444', 'Julián Flórez'),
(1, 'alejandro@sena.edu.co', '1000555666', 'Alejandro Patiño'),
(0, 'inactivo@sena.edu.co', '1000777888', 'Instructor Inactivo');

INSERT IGNORE INTO ambientes (activo, capacidad, nombre, tipo) VALUES 
(1, 35, 'Laboratorio de Software 302', 'LABORATORIO'),
(1, 40, 'Laboratorio de Software ADSO', 'LABORATORIO'),
(1, 150, 'Auditorio Central de Comercio', 'AUDITORIO'),
(0, 25, 'Sala de Redes Antigua (Inactiva)', 'SALA');