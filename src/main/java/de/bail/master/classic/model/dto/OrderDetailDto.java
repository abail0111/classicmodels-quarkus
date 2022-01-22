package de.bail.master.classic.model.dto;

import java.io.Serializable;

public class OrderDetailDto implements Serializable {

  private Integer order;

  private String product;

  private Integer quantityOrdered;

  private Double priceEach;

  private Short orderLineNumber;

  public Integer getOrder() {
    return order;
  }

  public void setOrder(Integer order) {
    this.order = order;
  }

  public String getProduct() {
    return product;
  }

  public void setProduct(String product) {
    this.product = product;
  }

  public Integer getQuantityOrdered() {
    return quantityOrdered;
  }

  public void setQuantityOrdered(Integer quantityOrdered) {
    this.quantityOrdered = quantityOrdered;
  }

  public Double getPriceEach() {
    return priceEach;
  }

  public void setPriceEach(Double priceEach) {
    this.priceEach = priceEach;
  }

  public Short getOrderLineNumber() {
    return orderLineNumber;
  }

  public void setOrderLineNumber(Short orderLineNumber) {
    this.orderLineNumber = orderLineNumber;
  }
}
