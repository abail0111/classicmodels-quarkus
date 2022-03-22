package de.bail.classicmodels.model.enities;

import io.smallrye.graphql.api.AdaptToScalar;
import io.smallrye.graphql.api.Scalar;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@NamedQueries({
        @NamedQuery(name = "Order.count", query = "select count(f) from Order f"),
        @NamedQuery(name = "Order.getAll", query = "select f from Order f order by f.id asc"),
        @NamedQuery(name = "Order.getAllByCustomer", query = "select f from Order f where f.customer.id in :customers order by f.id asc"),
        @NamedQuery(name = "Order.filterByStatus", query = "select f from Order f where f.status like :status order by f.id asc"),
        @NamedQuery(name = "Order.filterByStatus.count", query = "select count(f) from Order f where f.status like :status")
})
public class Order implements GenericEntity, Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "orderNumber")
  private Integer id;

  @AdaptToScalar(Scalar.String.class)
  private LocalDateTime orderDate;

  @AdaptToScalar(Scalar.String.class)
  private LocalDateTime requiredDate;

  @AdaptToScalar(Scalar.String.class)
  private LocalDateTime shippedDate;

  @NotNull
  private String status;

  @Column(columnDefinition = "text")
  private String comments;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customerNumber")
  private Customer customer;

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

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customerNumber) {
    this.customer = customerNumber;
  }

  @Override
  public String idToString() {
    return String.valueOf(id);
  }
}
