package de.bail.master.classic.model.enities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "offices")
@NamedQueries({
        @NamedQuery(name = "Office.count", query = "select count(f) from Office f"),
        @NamedQuery(name = "Office.getAll", query = "select f from Office f")
})
public class Office implements GenericEntity, Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "officeCode")
  private Integer id;

  @NotNull
  private String city;

  @NotNull
  private String phone;

  @NotNull
  private String addressLine1;

  private String addressLine2;

  private String state;

  @NotNull
  private String country;

  @NotNull
  private String postalCode;

  @NotNull
  private String territory;

  public Integer getId() {
    return id;
  }

  public void setId(Integer officeCode) {
    this.id = officeCode;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
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

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getTerritory() {
    return territory;
  }

  public void setTerritory(String territory) {
    this.territory = territory;
  }

  @Override
  public String idToString() {
    return id.toString();
  }
}
