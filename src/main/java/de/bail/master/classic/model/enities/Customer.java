package de.bail.master.classic.model.enities;


import de.bail.master.classic.util.GenericEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "customers")
@NamedQueries({
        @NamedQuery(name = "Customer.count", query = "select count(f) from Customer f"),
        @NamedQuery(name = "Customer.getAll", query = "select f from Customer f order by f.id asc")
})
// TODO @NamedEntityGraph(name = "salesRepEmployee", attributeNodes = @NamedAttributeNode("salesRepEmployee"))
public class Customer extends GenericEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "customerNumber")
  private Integer id;

  @NotNull
  private String customerName;

  @NotNull
  private String contactLastName;

  @NotNull
  private String contactFirstName;

  private String phone;

  @NotNull
  private String addressLine1;

  private String addressLine2;

  @NotNull
  private String city;

  private String state;

  @NotNull
  private String postalCode;

  private String country;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "salesRepEmployeeNumber")
  private Employee salesRepEmployee;

  private Double creditLimit;

  @Override
  public Integer getId() {
    return id;
  }

  @Override
  public void setId(Integer customerNumber) {
    this.id = customerNumber;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public String getContactLastName() {
    return contactLastName;
  }

  public void setContactLastName(String contactLastName) {
    this.contactLastName = contactLastName;
  }

  public String getContactFirstName() {
    return contactFirstName;
  }

  public void setContactFirstName(String contactFirstName) {
    this.contactFirstName = contactFirstName;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getAddressLine1() {
    return addressLine1;
  }

  public void setAddressLine1(String addressLine1) {
    this.addressLine1 = addressLine1;
  }

  public String getAddressLine2() {
    return addressLine2;
  }

  public void setAddressLine2(String addressLine2) {
    this.addressLine2 = addressLine2;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public Employee getSalesRepEmployee() {
    return salesRepEmployee;
  }

  public void setSalesRepEmployee(Employee salesRepEmployeeNumber) {
    this.salesRepEmployee = salesRepEmployeeNumber;
  }

  public Double getCreditLimit() {
    return creditLimit;
  }

  public void setCreditLimit(Double creditLimit) {
    this.creditLimit = creditLimit;
  }

}
