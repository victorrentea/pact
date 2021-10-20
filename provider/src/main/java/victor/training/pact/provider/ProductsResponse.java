package victor.training.pact.provider;

import lombok.Data;

import java.util.List;

@Data
public class ProductsResponse {
  private final List<Product> products;
}