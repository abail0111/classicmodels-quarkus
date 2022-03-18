package de.bail.master.classic.service;

import de.bail.master.classic.model.enities.Customer;
import de.bail.master.classic.model.enities.Employee;
import de.bail.master.classic.util.CrudService;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Traced
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

    public List<Customer> search(String term) {
        if (term != null && !term.isEmpty() && !term.isBlank()) {
            List<Customer> customers = new ArrayList<>();
            String[] keywords = term.toLowerCase().split(" ");
            for (Customer customer : getAllEntities()) {
                boolean isMatch = false;
                for (String keyword : keywords) {
                    if (customer.getCustomerName().toLowerCase().contains(keyword) ||
                            customer.getFirstName().toLowerCase().contains(keyword) ||
                            customer.getLastName().toLowerCase().contains(keyword)) {
                        isMatch = true;
                    } else {
                        isMatch = false;
                    }
                }
                if (isMatch) {
                    customers.add(customer);
                }
            }
            return customers;
        }
        return Collections.emptyList();
    }

    public List<Customer> getAllCustomerByEmployees(List<Integer> employeesId) {
        Query query = em.createNamedQuery("Customer.getAllByEmployees");
        query.setParameter("employees", employeesId);
        return query.getResultList();
    }

    public String greet(Integer id){
        return "hello " + id;
    }

}
