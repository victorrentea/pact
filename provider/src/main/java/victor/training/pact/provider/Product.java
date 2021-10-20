package victor.training.pact.provider;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Data
@NoArgsConstructor(access = PROTECTED)
public class Product {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String type;
  private String version;
  private String code;

  public Product(String name, String type, String version, String code) {
    this.name = name;
    this.type = type;
    this.version = version;
    this.code = code;
  }
}