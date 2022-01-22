package de.bail.master.classic.model.enities;

import de.bail.master.classic.util.GenericEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "payments")
@NamedQueries({
        @NamedQuery(name = "Payment.count", query = "select count(f) from Payment f"),
        @NamedQuery(name = "Payment.getAll", query = "select f from Payment f order by f.customer.id asc")
})
public class Payment extends GenericEntity implements Serializable {

  @Id
  @ManyToOne
  @JoinColumn(name = "customerNumber", nullable= false)
  private Customer customer;

  @NotNull
  private String checkNumber;

  @NotNull
  private Timestamp paymentDate;

  @NotNull
  private Double amount;

  @Override
  public Integer getId() {
    return customer.getId();
  }

  @Override
  public void setId(Integer id) {
    customer.setId(id);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customerNumber) {
    this.customer = customerNumber;
  }

  public String getCheckNumber() {
    return checkNumber;
  }

  public void setCheckNumber(String checkNumber) {
    this.checkNumber = checkNumber;
  }

  public Timestamp getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(Timestamp paymentDate) {
    this.paymentDate = paymentDate;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }
}
