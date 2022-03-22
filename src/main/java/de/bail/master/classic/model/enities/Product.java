package de.bail.master.classic.model.enities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Entity
@Table(name = "products")
@NamedQueries({
        @NamedQuery(name = "Product.count", query = "select count(f) from Product f"),
        @NamedQuery(name = "Product.getAll", query = "select f from Product f order by f.id asc"),
        @NamedQuery(name = "Product.getByIDs", query = "select f from Product f where f.id in :products order by f.id asc"),
        @NamedQuery(name = "Product.filterByProductLine", query = "select f from Product f where f.productLine.id like :productLine order by f.id asc"),
        @NamedQuery(name = "Product.filterByProductLine.count", query = "select count(f)  from Product f where f.productLine.id like :productLine")
})
public class Product implements GenericEntity, Serializable {

  @Id // ID Pattern: 'S<scale>_<id>' e.g. 'S12_1099'
  @Column(name = "productCode")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
  @GenericGenerator(
          name = "product_seq",
          strategy = "de.bail.master.classic.util.ProductIdSequenceGenerator")
  private String id;

  @NotNull
  private String productName;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "productLine", nullable = false)
  private ProductLine productLine;

  @NotNull
  @Pattern(regexp = "1:[0-9]{1,}")
  private String productScale;

  @NotNull
  private String productVendor;

  @Column(columnDefinition = "text")
  private String productDescription;

  @NotNull
  private Short quantityInStock;

  @NotNull
  private Double buyPrice;

  @NotNull
  private Double msrp;

  public String getId() {
    return id;
  }

  public void setId(String productCode) {
    this.id = productCode;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public ProductLine getProductLine() {
    return productLine;
  }

  public void setProductLine(ProductLine productLine) {
    this.productLine = productLine;
  }

  public String getProductScale() {
    return productScale;
  }

  public void setProductScale(String productScale) {
    this.productScale = productScale;
  }

  public String getProductVendor() {
    return productVendor;
  }

  public void setProductVendor(String productVendor) {
    this.productVendor = productVendor;
  }

  public String getProductDescription() {
    return productDescription;
  }

  public void setProductDescription(String productDescription) {
    this.productDescription = productDescription;
  }

  public Short getQuantityInStock() {
    return quantityInStock;
  }

  public void setQuantityInStock(Short quantityInStock) {
    this.quantityInStock = quantityInStock;
  }

  public Double getBuyPrice() {
    return buyPrice;
  }

  public void setBuyPrice(Double buyPrice) {
    this.buyPrice = buyPrice;
  }

  public Double getMsrp() {
    return msrp;
  }

  public void setMsrp(Double msrp) {
    this.msrp = msrp;
  }

  @Override
  public String idToString() {
    return id;
  }
}
