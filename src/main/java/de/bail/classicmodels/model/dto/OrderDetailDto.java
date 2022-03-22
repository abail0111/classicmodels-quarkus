package de.bail.classicmodels.model.dto;

import java.io.Serializable;

public class OrderDetailDto implements Serializable {

  private Link<Integer> order;

  private Link<String> product;

  private Integer quantityOrdered;

  private Double priceEach;

  private Short orderLineNumber;

  public Link<Integer> getOrder() {
    return order;
  }

  public void setOrder(Link<Integer> order) {
    this.order = order;
  }

  public Link<String> getProduct() {
    return product;
  }

  public void setProduct(Link<String> product) {
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
