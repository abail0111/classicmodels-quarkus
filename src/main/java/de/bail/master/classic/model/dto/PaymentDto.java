package de.bail.master.classic.model.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class PaymentDto implements Serializable {

  private Integer customer;

  private String checkNumber;

  private Timestamp paymentDate;

  private Double amount;

  public Integer getCustomer() {
    return customer;
  }

  public void setCustomer(Integer customer) {
    this.customer = customer;
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