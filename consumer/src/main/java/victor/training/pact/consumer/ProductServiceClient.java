package victor.training.pact.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class ProductServiceClient {
   private final RestTemplate restTemplate;

   @Value("${serviceClients.products.baseUrl}")
   String baseUrl;

   public ProductServiceResponse fetchAllProducts() {
      return restTemplate.getForObject(baseUrl + "/products", ProductServiceResponse.class);
   }

   public Product fetchProductById(long id) {
      return restTemplate.getForObject(baseUrl + "/product/" + id, Product.class);
   }
}
