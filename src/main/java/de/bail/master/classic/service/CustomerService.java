package de.bail.master.classic.service;

import de.bail.master.classic.enities.Customer;
import de.bail.master.classic.enities.Employee;
import de.bail.master.classic.util.CrudService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CustomerService extends CrudService<Customer, Integer> {

    @Inject
    EmployeeService employeeService;

    protected CustomerService() {
        super(Customer.class);
    }

    @Override
    public Customer create(Customer customer) {
        // check for employee id
        if (customer.getSalesRepEmployee() != null && customer.getSalesRepEmployee().getId() != null) {
            Employee employee = employeeService.getEntityById(customer.getSalesRepEmployee().getId());
            if (employee != null) {
                System.out.println(employee.getFirstName());
                save(customer);
            }
        } else {
            save(customer);
        }
        return customer;
    }
}
