package victor.training.pact.consumer;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductServiceResponse {
   private List<Product> products = new ArrayList<>();
}
