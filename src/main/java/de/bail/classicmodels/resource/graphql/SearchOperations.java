package de.bail.classicmodels.resource.graphql;

import de.bail.classicmodels.model.enities.Contact;
import de.bail.classicmodels.service.CustomerService;
import de.bail.classicmodels.service.EmployeeService;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.opentracing.Traced;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Traced
@GraphQLApi
public class SearchOperations {

    @Inject
    public CustomerService customerService;

    @Inject
    public EmployeeService employeeService;

    @Query("searchContact")
    @Description("Search for customers and employees")
    public List<Contact> getAllCustomers(String term, int limit) {
        List<Contact> results = Stream.concat(
                customerService.search(term).stream(),
                employeeService.search(term).stream()
        ).collect(Collectors.toList());
        if (results.size() > limit) {
            return results.subList(0, limit);
        }
        return results;
    }

}
