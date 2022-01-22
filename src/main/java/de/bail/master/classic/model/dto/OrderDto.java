package de.bail.master.classic.model.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class OrderDto implements Serializable {

  private Integer id;

  private Timestamp orderDate;

  private Timestamp requiredDate;

  private Timestamp shippedDate;

  private String status;

  private String comments;

  private Integer customer;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Timestamp getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(Timestamp orderDate) {
    this.orderDate = orderDate;
  }

  public Timestamp getRequiredDate() {
    return requiredDate;
  }

  public void setRequiredDate(Timestamp requiredDate) {
    this.requiredDate = requiredDate;
  }

  public Timestamp getShippedDate() {
    return shippedDate;
  }

  public void setShippedDate(Timestamp shippedDate) {
    this.shippedDate = shippedDate;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public Integer getCustomer() {
    return customer;
  }

  public void setCustomer(Integer customer) {
    this.customer = customer;
  }
}
