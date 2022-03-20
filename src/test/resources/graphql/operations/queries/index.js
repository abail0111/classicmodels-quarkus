const fs = require('fs');
const path = require('path');

module.exports.customer = fs.readFileSync(path.join(__dirname, 'customer.gql'), 'utf8');
module.exports.customers = fs.readFileSync(path.join(__dirname, 'customers.gql'), 'utf8');
module.exports.employee = fs.readFileSync(path.join(__dirname, 'employee.gql'), 'utf8');
module.exports.employees = fs.readFileSync(path.join(__dirname, 'employees.gql'), 'utf8');
module.exports.office = fs.readFileSync(path.join(__dirname, 'office.gql'), 'utf8');
module.exports.offices = fs.readFileSync(path.join(__dirname, 'offices.gql'), 'utf8');
module.exports.order = fs.readFileSync(path.join(__dirname, 'order.gql'), 'utf8');
module.exports.orders = fs.readFileSync(path.join(__dirname, 'orders.gql'), 'utf8');
module.exports.payment = fs.readFileSync(path.join(__dirname, 'payment.gql'), 'utf8');
module.exports.payments = fs.readFileSync(path.join(__dirname, 'payments.gql'), 'utf8');
module.exports.product = fs.readFileSync(path.join(__dirname, 'product.gql'), 'utf8');
module.exports.productLine = fs.readFileSync(path.join(__dirname, 'productLine.gql'), 'utf8');
module.exports.productLines = fs.readFileSync(path.join(__dirname, 'productLines.gql'), 'utf8');
module.exports.products = fs.readFileSync(path.join(__dirname, 'products.gql'), 'utf8');
module.exports.searchContact = fs.readFileSync(path.join(__dirname, 'searchContact.gql'), 'utf8');
