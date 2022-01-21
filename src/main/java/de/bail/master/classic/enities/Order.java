package de.bail.master.classic.enities;

import de.bail.master.classic.util.GenericEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "orders")
@NamedQueries({
        @NamedQuery(name = "Order.count", query = "select count(f) from Order f"),
        @NamedQuery(name = "Order.getAll", query = "select f from Order f order by f.id asc")
})
public class Order extends GenericEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "orderNumber")
  private Integer id;

  @NotNull
  private Timestamp orderDate;

  @NotNull
  private Timestamp requiredDate;

  private Timestamp shippedDate;

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

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customerNumber) {
    this.customer = customerNumber;
  }
}
