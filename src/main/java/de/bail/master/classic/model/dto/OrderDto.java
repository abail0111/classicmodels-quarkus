package de.bail.master.classic.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class OrderDto implements Serializable {

  private Integer id;

  private LocalDateTime orderDate;

  private LocalDateTime requiredDate;

  private LocalDateTime shippedDate;

  private String status;

  private String comments;

  private Integer customer;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public LocalDateTime getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(LocalDateTime orderDate) {
    this.orderDate = orderDate;
  }

  public LocalDateTime getRequiredDate() {
    return requiredDate;
  }

  public void setRequiredDate(LocalDateTime requiredDate) {
    this.requiredDate = requiredDate;
  }

  public LocalDateTime getShippedDate() {
    return shippedDate;
  }

  public void setShippedDate(LocalDateTime shippedDate) {
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
