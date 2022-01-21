package de.bail.master.classic.enities;

import de.bail.master.classic.util.GenericEntityStr;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "productlines")
@NamedQueries({
        @NamedQuery(name = "ProductLine.count", query = "select count(f) from ProductLine f"),
        @NamedQuery(name = "ProductLine.getAll", query = "select f from ProductLine f order by f.id asc")
})
public class ProductLine extends GenericEntityStr implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "productLine")
  private String id;

  @Column(length = 4000)
  private String textDescription;

  @Column(columnDefinition = "mediumtext")
  private String htmlDescription;

  @Lob
  @Column(columnDefinition="mediumblob")
  private byte[] image;

  public String getId() {
    return id;
  }

  public void setId(String productLine) {
    this.id = productLine;
  }

  public String getTextDescription() {
    return textDescription;
  }

  public void setTextDescription(String textDescription) {
    this.textDescription = textDescription;
  }

  public String getHtmlDescription() {
    return htmlDescription;
  }

  public void setHtmlDescription(String htmlDescription) {
    this.htmlDescription = htmlDescription;
  }

  public byte[] getImage() {
    return image;
  }

  public void setImage(byte[] image) {
    this.image = image;
  }

}
