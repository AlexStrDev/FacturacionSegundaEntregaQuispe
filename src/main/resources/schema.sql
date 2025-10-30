CREATE DATABASE IF NOT EXISTS facturacion_db;
USE facturacion_db;

CREATE TABLE IF NOT EXISTS cliente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ruc VARCHAR(11) UNIQUE NOT NULL,
    razon_social VARCHAR(200) NOT NULL,
    direccion VARCHAR(300),
    telefono VARCHAR(20),
    email VARCHAR(100),
    activo BOOLEAN DEFAULT TRUE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS producto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(50) UNIQUE NOT NULL,
    nombre VARCHAR(200) NOT NULL,
    precio_unitario DECIMAL(10, 2) NOT NULL,
    stock INT DEFAULT 0,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS factura (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_factura VARCHAR(20) UNIQUE NOT NULL,
    cliente_id BIGINT NOT NULL,
    fecha_emision DATE NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    igv DECIMAL(10, 2) NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

CREATE TABLE IF NOT EXISTS detalle_factura (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    factura_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (factura_id) REFERENCES factura(id),
    FOREIGN KEY (producto_id) REFERENCES producto(id)
);

-- Datos de prueba
INSERT INTO cliente (ruc, razon_social, direccion, telefono, email) VALUES
('20123456789', 'Empresa Demo SAC', 'Av. Principal 123, Lima', '987654321', 'ventas@demo.com'),
('20987654321', 'Comercial Perú EIRL', 'Jr. Los Andes 456, Arequipa', '912345678', 'info@comercial.pe');

INSERT INTO producto (codigo, nombre, precio_unitario, stock) VALUES
('PROD001', 'Laptop HP', 2500.00, 10),
('PROD002', 'Mouse Inalámbrico', 35.00, 50),
('PROD003', 'Teclado Mecánico', 150.00, 25);