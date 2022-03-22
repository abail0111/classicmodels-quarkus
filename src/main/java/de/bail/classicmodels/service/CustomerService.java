package de.bail.classicmodels.service;

import de.bail.classicmodels.model.enities.Customer;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Customer Service
 */
@Traced
@ApplicationScoped
public class CustomerService extends CrudService<Customer, Integer> {

    @Inject
    EmployeeService employeeService;

    /**
     * Call constructor of abstract crud service
     * The type of Entity is needed to secure the correct implementation of the JPA access methods
     */
    protected CustomerService() {
        super(Customer.class);
    }

    /**
     * Save a new customer to the database.
     * The employee must already exist and will not be created.
     * @param customer Valid customer object
     * @return persisted customer object
     */
    @Override
    public Customer create(Customer customer) {
        if (customer != null && customer.getSalesRepEmployee() != null && customer.getSalesRepEmployee().getId() != null) {
            if (!employeeService.hasEntity(customer.getSalesRepEmployee().getId())) {
                throw employeeService.notFoundException(customer.getSalesRepEmployee().getId());
            }
        }
        save(customer);
        return customer;
    }

    /**
     * Search for customer
     * @param term Search term will be used for 'customerName', 'firstName' and 'lastName'
     * @return List of customer with matching names
     */
    public List<Customer> search(String term) {
        if (term != null && !term.isEmpty() && !term.isBlank()) {
            List<Customer> customers = new ArrayList<>();
            String[] keywords = term.toLowerCase().split(" ");
            for (Customer customer : getAllEntities()) {
                boolean isMatch = false;
                for (String keyword : keywords) {
                    isMatch = customer.getCustomerName().toLowerCase().contains(keyword) ||
                            customer.getFirstName().toLowerCase().contains(keyword) ||
                            customer.getLastName().toLowerCase().contains(keyword);
                }
                if (isMatch) {
                    customers.add(customer);
                }
            }
            return customers;
        }
        return Collections.emptyList();
    }

    /**
     * Get all customer by employee ids
     * @param employeesId List of employee ids
     * @return List of matching customer
     */
    public List<Customer> getAllCustomerByEmployees(List<Integer> employeesId) {
        Query query = em.createNamedQuery("Customer.getAllByEmployees");
        query.setParameter("employees", employeesId);
        return query.getResultList();
    }

}
