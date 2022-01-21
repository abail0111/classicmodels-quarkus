package de.bail.master.classic.service;

import de.bail.master.classic.enities.Employee;
import de.bail.master.classic.util.CrudService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

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

}
