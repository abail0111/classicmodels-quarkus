package de.bail.master.classic.service;

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
public class EmployeeService extends CrudService<Employee, Integer> {

    @Inject
    OfficeService officeService;

    protected EmployeeService() {
        super(Employee.class);
    }

    @Override
    public Employee create(Employee entity) {
        // TODO create Employee
        save(entity);
        return null;
    }

    public List<Employee> search(String term) {
        if (term != null && !term.isEmpty() && !term.isBlank()) {
            List<Employee> employees = new ArrayList<>();
            String[] keywords = term.toLowerCase().split(" ");
            for (Employee employee : getAllEntities()) {
                boolean isMatch = false;
                for (String keyword : keywords) {
                    if (employee.getJobTitle().toLowerCase().contains(keyword) ||
                            employee.getFirstName().toLowerCase().contains(keyword) ||
                            employee.getLastName().toLowerCase().contains(keyword)) {
                        isMatch = true;
                    } else {
                        isMatch = false;
                    }
                }
                if (isMatch) {
                    employees.add(employee);
                }
            }
            return employees;
        }
        return Collections.emptyList();
    }

    public List<Employee> getAllEmployees(List<Integer> employees) {
        Query query = em.createNamedQuery("Employee.getAllByIDs");
        query.setParameter("employees", employees);
        return query.getResultList();
    }

    public List<Employee> getAllByOffice(List<String> office) {
        Query query = em.createNamedQuery("Employee.getAllByOffice");
        query.setParameter("office", office);
        return query.getResultList();
    }
}
