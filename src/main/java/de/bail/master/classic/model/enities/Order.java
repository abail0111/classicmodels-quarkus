package de.bail.master.classic.model.enities;

import de.bail.master.classic.util.GenericEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@NamedQueries({
        @NamedQuery(name = "Order.count", query = "select count(f) from Order f"),
        @NamedQuery(name = "Order.getAll", query = "select f from Order f order by f.id asc"),
        @NamedQuery(name = "Order.filterByStatus", query = "select f from Order f where f.status like :status order by f.id asc"),
        @NamedQuery(name = "Order.filterByStatus.count", query = "select count(f) from Order f where f.status like :status")
})
public class Order extends GenericEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "orderNumber")
  private Integer id;

  private LocalDateTime orderDate;

  private LocalDateTime requiredDate;

  private LocalDateTime shippedDate;

  @NotNull
  private String status;

  @Column(columnDefinition = "text")
  private String comments;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customerNumber")
  private Customer customer;

  @Override
  public Integer getId() {
    return id;
  }

  @Override
  public void setId(Integer orderNumber) {
    this.id = orderNumber;
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
}
