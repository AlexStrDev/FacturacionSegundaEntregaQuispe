-- Datos de prueba para clientes
INSERT INTO cliente (ruc, razon_social, direccion, telefono, email, activo, fecha_registro) VALUES
('20123456789', 'Empresa Demo SAC', 'Av. Principal 123, Lima', '987654321', 'ventas@demo.com', true, CURRENT_TIMESTAMP),
('20987654321', 'Comercial Perú EIRL', 'Jr. Los Andes 456, Arequipa', '912345678', 'info@comercial.pe', true, CURRENT_TIMESTAMP),
('20456789123', 'Distribuidora Norte SAC', 'Calle Las Flores 789, Trujillo', '956789123', 'contacto@norte.pe', true, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Datos de prueba para productos
INSERT INTO producto (codigo, nombre, precio_unitario, stock, activo) VALUES
('PROD001', 'Laptop HP', 2500.00, 10, true),
('PROD002', 'Mouse Inalámbrico', 35.00, 50, true),
('PROD003', 'Teclado Mecánico', 150.00, 25, true),
('PROD004', 'Monitor 24 pulgadas', 450.00, 15, true),
('PROD005', 'Webcam HD', 120.00, 30, true),
('PROD006', 'Audífonos Bluetooth', 80.00, 40, true)
ON CONFLICT DO NOTHING;