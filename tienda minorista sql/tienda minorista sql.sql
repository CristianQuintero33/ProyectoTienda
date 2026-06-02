DROP DATABASE IF EXISTS tienda_minorista;
CREATE DATABASE tienda_minorista;
USE tienda_minorista;

CREATE TABLE productos (
    codigo       VARCHAR(20)  PRIMARY KEY,
    nombre       VARCHAR(100) NOT NULL,
    categoria    VARCHAR(20)  NOT NULL,
    precioCompra DOUBLE       NOT NULL,
    precioVenta  DOUBLE       NOT NULL,
    stockActual  INT          DEFAULT 0,
    stockMinimo  INT          DEFAULT 0,
    stockMaximo  INT          DEFAULT 0,
    activo       BOOLEAN      DEFAULT TRUE
);

CREATE TABLE clientes (
    codigo               VARCHAR(20)  PRIMARY KEY,
    nombreCompleto       VARCHAR(100) NOT NULL,
    tipoIdentificacion   VARCHAR(10)  NOT NULL,
    numeroIdentificacion VARCHAR(20)  NOT NULL,
    direccion            VARCHAR(200),
    telefono             VARCHAR(20),
    tipoCliente          VARCHAR(20)  NOT NULL,
    activo               BOOLEAN      DEFAULT TRUE
);

CREATE TABLE proveedores (
    codigo    VARCHAR(20)  PRIMARY KEY,
    nombre    VARCHAR(100) NOT NULL,
    nit       VARCHAR(20)  NOT NULL,
    direccion VARCHAR(200),
    telefono  VARCHAR(20),
    correo    VARCHAR(100),
    activo    BOOLEAN      DEFAULT TRUE
);

CREATE TABLE ventas (
    numeroFactura VARCHAR(20)  PRIMARY KEY,
    fechaHora     DATETIME     NOT NULL,
    clienteCodigo VARCHAR(20)  NOT NULL,
    formaPago     VARCHAR(20)  NOT NULL,
    estado        VARCHAR(20)  DEFAULT 'ACTIVA',
    aplicaIva     BOOLEAN      DEFAULT FALSE,
    subtotal      DOUBLE       DEFAULT 0,
    iva           DOUBLE       DEFAULT 0,
    total         DOUBLE       DEFAULT 0,
    FOREIGN KEY (clienteCodigo) REFERENCES clientes(codigo)
);

CREATE TABLE detalle_ventas (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    numeroFactura  VARCHAR(20) NOT NULL,
    productoCodigo VARCHAR(20) NOT NULL,
    cantidad       INT         NOT NULL,
    precioUnitario DOUBLE      NOT NULL,
    subtotal       DOUBLE      NOT NULL,
    FOREIGN KEY (numeroFactura)  REFERENCES ventas(numeroFactura),
    FOREIGN KEY (productoCodigo) REFERENCES productos(codigo)
);

CREATE TABLE compras (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    numeroOrden     VARCHAR(20)  NOT NULL UNIQUE,
    fecha           DATETIME     NOT NULL,
    proveedorCodigo VARCHAR(20)  NOT NULL,
    total           DOUBLE       DEFAULT 0,
    estado          VARCHAR(20)  DEFAULT 'ACTIVA',
    FOREIGN KEY (proveedorCodigo) REFERENCES proveedores(codigo)
);

CREATE TABLE detalle_compras (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    compraId       INT         NOT NULL,
    productoCodigo VARCHAR(20) NOT NULL,
    cantidad       INT         NOT NULL,
    precioCompra   DOUBLE      NOT NULL,
    subtotal       DOUBLE      NOT NULL,
    FOREIGN KEY (compraId)       REFERENCES compras(id),
    FOREIGN KEY (productoCodigo) REFERENCES productos(codigo)
);

CREATE TABLE movimientos_contables (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    fecha       DATETIME     NOT NULL,
    tipo        VARCHAR(20)  NOT NULL,
    descripcion VARCHAR(255),
    debito      DOUBLE       DEFAULT 0,
    credito     DOUBLE       DEFAULT 0,
    referencia  VARCHAR(20)
);

INSERT INTO productos VALUES
('P001','Arroz 1kg','VIVERES',2500,3200,80,10,200,true),
('P002','Aceite 1L','VIVERES',8000,10500,45,8,100,true),
('P003','Jabón de manos','ASEO',3000,4200,60,15,150,true),
('P004','Cuaderno 100h','PAPELERIA',4500,6000,5,10,80,true);

INSERT INTO clientes VALUES
('C001','Juan Pérez','CC','10234567','Calle 1 #2-3','3001234567','MINORISTA',true),
('C002','Comercial López','NIT','900123456','Av Principal #10','3107654321','MAYORISTA',true);

INSERT INTO proveedores VALUES
('PR001','Distribuidora Nacional','800123456-1','Zona Industrial','3201234567','dist@correo.com',true),
('PR002','Suministros El Sur','900987654-2','Carrera 5 #10','3109876543','sur@correo.com',true);