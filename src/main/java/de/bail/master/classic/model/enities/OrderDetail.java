package de.bail.master.classic.model.enities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "orderdetails")
@NamedQueries({
        @NamedQuery(name = "OrderDetail.count", query = "select count(f) from OrderDetail f"),
        @NamedQuery(name = "OrderDetail.getAll", query = "select f from OrderDetail f order by f.order.id asc")
})
public class OrderDetail implements GenericEntity, Serializable {

  @Id
  @ManyToOne
  @JoinColumn(name = "orderNumber", nullable = false)
  private Order order;

  @Id
  @ManyToOne
  @JoinColumn(name = "productCode", nullable = false)
  private Product product;

  @NotNull
  private Integer quantityOrdered;

  @NotNull
  private Double priceEach;

  @NotNull
  private Short orderLineNumber;

  public Integer getId() {
    return order.getId();
  }

  public void setId(Integer id) {
    order.setId(id);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order orderNumber) {
    this.order = orderNumber;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product productCode) {
    this.product = productCode;
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

  @Override
  public String idToString() {
    return null;
  }
}
