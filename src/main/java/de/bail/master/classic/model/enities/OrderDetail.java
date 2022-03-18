package de.bail.master.classic.model.enities;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "orderdetails")
@NamedQueries({
        @NamedQuery(name = "OrderDetail.count", query = "select count(f) from OrderDetail f"),
        @NamedQuery(name = "OrderDetail.getAll", query = "select f from OrderDetail f order by f.orderNumber asc"),
        @NamedQuery(name = "OrderDetail.getAllByOrder", query = "select f from OrderDetail f where f.orderNumber = :orderNumber order by f.product.id asc"),
        @NamedQuery(name = "OrderDetail.getAllByOrders", query = "select f from OrderDetail f where f.orderNumber in :orderNumbers order by f.orderNumber asc"),
        @NamedQuery(name = "OrderDetail.getAllByOrder.count", query = "select count(f) from OrderDetail f where f.orderNumber = :orderNumber")
})
@IdClass(OrderDetail.OrderDetailId.class)
public class OrderDetail implements GenericEntity, Serializable {

  @Id
  @JoinColumn(name = "orderNumber", nullable = false)
  private Integer orderNumber;

  @Id
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "productCode", nullable = false)
  private Product product;

  @NotNull
  private Integer quantityOrdered;

  @NotNull
  private Double priceEach;

  @NotNull
  private Short orderLineNumber;

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  public Integer getOrder() {
    return orderNumber;
  }

  public void setOrder(Integer orderNumber) {
    this.orderNumber = orderNumber;
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
    if (product != null) {
      return orderNumber + "/" + product.getId();
    }
    return "";
  }

  /**
   * Composite ID Class
   */
  public static class OrderDetailId implements Serializable {

    @JoinColumn(name = "orderNumber", nullable = false)
    private Integer orderNumber;

    private Product product;

    public OrderDetailId() {
      product = new Product();
    }

    public OrderDetailId(Integer orderNumber, Product product) {
      this.orderNumber = orderNumber;
      this.product = product;
    }

    public OrderDetailId(Integer orderNumber, String product) {
      this.orderNumber = orderNumber;
      this.product = new Product();
      this.product.setId(product);
    }

    @Override
    public int hashCode() {
      return Objects.hash(orderNumber, product);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null || getClass() != obj.getClass()) {
        return false;
      }
      OrderDetailId pk = (OrderDetailId) obj;
      return Objects.equals(orderNumber, pk.orderNumber) &&
              Objects.equals(product.getId(), pk.product.getId());
    }

    public Integer getOrder() {
      return orderNumber;
    }

    public void setOrder(Integer orderNumber) {
      this.orderNumber = orderNumber;
    }

    public Product getProduct() {
      return product;
    }

    public void setProduct(Product product) {
      this.product = product;
    }
  }
}
