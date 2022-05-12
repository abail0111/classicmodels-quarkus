
DROP TABLE Customers;
DROP TABLE Employees;
DROP TABLE Offices;
DROP TABLE OrderDetails;
DROP TABLE Orders;
DROP TABLE Payments;
DROP TABLE Products;
DROP TABLE ProductLines;
DROP SEQUENCE product_seq;

CREATE TABLE Customers (
  customerNumber SERIAL PRIMARY KEY,
  customerName VARCHAR(50) NOT NULL,
  contactLastName VARCHAR(50) NOT NULL,
  contactFirstName VARCHAR(50) NOT NULL,
  phone VARCHAR(50) NOT NULL,
  addressLine1 VARCHAR(50) NOT NULL,
  addressLine2 VARCHAR(50) NULL,
  city VARCHAR(50) NOT NULL,
  state VARCHAR(50) NULL,
  postalCode VARCHAR(15) NULL,
  country VARCHAR(50) NOT NULL,
  salesRepEmployeeNumber INTEGER NULL,
  creditLimit DOUBLE PRECISION NULL
);

CREATE TABLE Employees (
  employeeNumber SERIAL PRIMARY KEY,
  lastName VARCHAR(50) NOT NULL,
  firstName VARCHAR(50) NOT NULL,
  extension VARCHAR(10) NOT NULL,
  email VARCHAR(100) NOT NULL,
  officeCode INTEGER NOT NULL,
  reportsTo INTEGER NULL,
  jobTitle VARCHAR(50) NOT NULL
);

CREATE TABLE Offices (
  officeCode SERIAL PRIMARY KEY,
  city VARCHAR(50) NOT NULL,
  phone VARCHAR(50) NOT NULL,
  addressLine1 VARCHAR(50) NOT NULL,
  addressLine2 VARCHAR(50) NULL,
  state VARCHAR(50) NULL,
  country VARCHAR(50) NOT NULL,
  postalCode VARCHAR(15) NOT NULL,
  territory VARCHAR(10) NOT NULL
);

CREATE TABLE OrderDetails (
  orderNumber INTEGER NOT NULL,
  productCode VARCHAR(15) NOT NULL,
  quantityOrdered INTEGER NOT NULL,
  priceEach DOUBLE PRECISION NOT NULL,
  orderLineNumber SMALLINT NOT NULL,
  PRIMARY KEY (orderNumber, productCode)
);

CREATE TABLE Orders (
  orderNumber SERIAL PRIMARY KEY,
  orderDate TIMESTAMP(0) NOT NULL,
  requiredDate TIMESTAMP(0) NOT NULL,
  shippedDate TIMESTAMP(0) NULL,
  status VARCHAR(15) NOT NULL,
  comments TEXT NULL,
  customerNumber INTEGER NOT NULL
);

CREATE TABLE Payments (
  customerNumber INTEGER NOT NULL,  
  checkNumber VARCHAR(50) NOT NULL,
  paymentDate TIMESTAMP(0) NOT NULL,
  amount DOUBLE PRECISION NOT NULL,
  PRIMARY KEY (customerNumber, checkNumber)
);

CREATE SEQUENCE product_seq;

CREATE TABLE Products (
  productCode VARCHAR(15) NOT NULL DEFAULT '0',
  productName VARCHAR(70) NOT NULL,
  productLine VARCHAR(50) NOT NULL,
  productScale VARCHAR(10) NOT NULL,
  productVendor VARCHAR(50) NOT NULL,
  productDescription TEXT NOT NULL,
  quantityInStock SMALLINT NOT NULL,
  buyPrice DOUBLE PRECISION NOT NULL,
  MSRP DOUBLE PRECISION NOT NULL,
  PRIMARY KEY (productCode)
);

CREATE TABLE ProductLines(
  productLine VARCHAR(50) NOT NULL,
  textDescription VARCHAR(4000) NULL,
  htmlDescription TEXT NULL,
  image bytea NULL,
  PRIMARY KEY (productLine)
);


COPY Customers FROM 'F:/mysqlsampledatabase/postgresql/datafiles/Customers.txt' (format csv, null "null", DELIMITER ',');

COPY Employees FROM 'F:/mysqlsampledatabase/postgresql/datafiles/Employees.txt' (format csv, null "null", DELIMITER ',');

COPY Offices FROM 'F:/mysqlsampledatabase/postgresql/datafiles/Offices.txt' (format csv, null "null", DELIMITER ',');

COPY OrderDetails FROM 'F:/mysqlsampledatabase/postgresql/datafiles/OrderDetails.txt' (format csv, null "null", DELIMITER ',');

COPY Orders FROM 'F:/mysqlsampledatabase/postgresql/datafiles/Orders.txt' (format csv, null "null", DELIMITER ',');

COPY Payments FROM 'F:/mysqlsampledatabase/postgresql/datafiles/Payments.txt' (format csv, null "null", DELIMITER ',');

COPY ProductLines FROM 'F:/mysqlsampledatabase/postgresql/datafiles/ProductLines.txt' (format csv, null "null", DELIMITER ',');

COPY Products FROM 'F:/mysqlsampledatabase/postgresql/datafiles/Products.txt' (format csv, null "null", DELIMITER ',');

