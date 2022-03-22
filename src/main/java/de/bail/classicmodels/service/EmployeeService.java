package de.bail.classicmodels.service;

import de.bail.classicmodels.model.enities.Employee;
import org.eclipse.microprofile.opentracing.Traced;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Employee service
 */
@Traced
@ApplicationScoped
public class EmployeeService extends CrudService<Employee, Integer> {

    @Inject
    OfficeService officeService;

    /**
     * Call constructor of abstract crud service
     * The type of Entity is needed to secure the correct implementation of the JPA access methods
     */
    protected EmployeeService() {
        super(Employee.class);
    }

    /**
     * Save a new employee to the database.
     * The office must already exist and will not be created.
     * @param employee Valid employee object
     * @return persisted employee object
     */
    @Override
    public Employee create(Employee employee) {
        if (employee != null && employee.getOffice() != null) {
            if (!officeService.hasEntity(employee.getOffice().getId())) {
                throw officeService.notFoundException(employee.getOffice().getId());
            }
            save(employee);
        }
        return employee;
    }

    /**
     * Search for employee
     * @param term Search term will be used for 'jobTitle', 'firstName' and 'lastName'
     * @return List of employee with matching names
     */
    public List<Employee> search(String term) {
        if (term != null && !term.isEmpty() && !term.isBlank()) {
            List<Employee> employees = new ArrayList<>();
            String[] keywords = term.toLowerCase().split(" ");
            for (Employee employee : getAllEntities()) {
                boolean isMatch = false;
                for (String keyword : keywords) {
                    isMatch = employee.getJobTitle().toLowerCase().contains(keyword) ||
                            employee.getFirstName().toLowerCase().contains(keyword) ||
                            employee.getLastName().toLowerCase().contains(keyword);
                }
                if (isMatch) {
                    employees.add(employee);
                }
            }
            return employees;
        }
        return Collections.emptyList();
    }

    /**
     * Get all employees by id list
     * @param employees List of employee ids
     * @return List of employees with matching ids
     */
    public List<Employee> getAllEmployees(List<Integer> employees) {
        Query query = em.createNamedQuery("Employee.getAllByIDs");
        query.setParameter("employees", employees);
        return query.getResultList();
    }

    /**
     * Get all employees with matching office id
     * @param office list of office ids
     * @return List of employees with matching office id
     */
    public List<Employee> getAllByOffice(List<Integer> office) {
        Query query = em.createNamedQuery("Employee.getAllByOffice");
        query.setParameter("office", office);
        return query.getResultList();
    }
}
