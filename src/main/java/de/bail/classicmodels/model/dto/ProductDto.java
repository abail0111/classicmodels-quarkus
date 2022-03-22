package de.bail.classicmodels.model.dto;

import java.io.Serializable;

public class ProductDto implements Serializable {

  private String id;

  private String productName;

  private Link<String> productLine;

  private String productScale;

  private String productVendor;

  private String productDescription;

  private Short quantityInStock;

  private Double buyPrice;

  private Double msrp;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public Link<String> getProductLine() {
    return productLine;
  }

  public void setProductLine(Link<String> productLine) {
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
}
