package de.bail.master.classic.model.enities;

import de.bail.master.classic.util.GenericEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "employees")
@NamedQueries({
        @NamedQuery(name = "Employee.count", query = "select count(f) from Employee f"),
        @NamedQuery(name = "Employee.getAll", query = "select f from Employee f order by f.id asc")
})
public class Employee extends GenericEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "employeeNumber")
  private Integer id;

  @NotNull
  private String lastName;

  @NotNull
  private String firstName;

  @NotNull
  private String extension;

  @NotNull
  private String email;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "officeCode", nullable = false)
  private Office office;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reportsTo", nullable = false)
  private Employee reportsTo;

  @NotNull
  private String jobTitle;

  @Override
  public Integer getId() {
    return id;
  }

  @Override
  public void setId(Integer id) {
    this.id = id;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getExtension() {
    return extension;
  }

  public void setExtension(String extension) {
    this.extension = extension;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Office getOffice() {
    return office;
  }

  public void setOffice(Office office) {
    this.office = office;
  }

  public Employee getReportsTo() {
    return reportsTo;
  }

  public void setReportsTo(Employee reportsTo) {
    this.reportsTo = reportsTo;
  }

  public String getJobTitle() {
    return jobTitle;
  }

  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
  }
}
