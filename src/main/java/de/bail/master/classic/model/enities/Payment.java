package de.bail.master.classic.model.enities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "payments")
@NamedQueries({
        @NamedQuery(name = "Payment.count", query = "select count(f) from Payment f"),
        @NamedQuery(name = "Payment.getAll", query = "select f from Payment f order by f.customerNumber asc")
})
@IdClass(Payment.PaymentId.class)
public class Payment implements GenericEntity, Serializable {

  @Id
  private Integer customerNumber;

  @Id
  private String checkNumber;

  @NotNull
  private Timestamp paymentDate;

  @NotNull
  private Double amount;

  public PaymentId getId() {
    return new PaymentId(
            customerNumber,
            checkNumber
    );
  }

  public void setId(PaymentId id) {
    this.customerNumber = id.getCustomerNumber();
    this.checkNumber = id.getCheckNumber();
  }


  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  public Integer getCustomerNumber() {
    return customerNumber;
  }

  public void setCustomerNumber(Integer customerNumber) {
    this.customerNumber = customerNumber;
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

  @Override
  public String idToString() {
    return customerNumber + "/" + checkNumber;
  }

  /**
   * Composite ID Class
   */
  public static class PaymentId implements Serializable {

    private Integer customerNumber;

    private String checkNumber;

    public PaymentId() {
    }

    public PaymentId(Integer customerNumber, String checkNumber) {
      this.customerNumber = customerNumber;
      this.checkNumber = checkNumber;
    }

    @Override
    public int hashCode() {
      return Objects.hash(customerNumber, checkNumber);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null || getClass() != obj.getClass()) {
        return false;
      }
      PaymentId pk = (PaymentId) obj;
      return Objects.equals(customerNumber, pk.customerNumber) &&
              Objects.equals(checkNumber, pk.checkNumber);
    }

    public Integer getCustomerNumber() {
      return customerNumber;
    }

    public void setCustomerNumber(Integer customerNumber) {
      this.customerNumber = customerNumber;
    }

    public String getCheckNumber() {
      return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
      this.checkNumber = checkNumber;
    }
  }
}
