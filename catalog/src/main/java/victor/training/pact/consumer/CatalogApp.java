package victor.training.pact.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@SpringBootApplication
public class CatalogApp {
   public static void main(String[] args) {
      SpringApplication.run(CatalogApp.class, args);
   }

   @Bean
   public RestTemplate restTemplate() {
      return new RestTemplate();
   }

   @Autowired
   private ProductServiceClient productServiceClient;

   @GetMapping
   public List<Product> getAllProducts() {
      return productServiceClient.fetchAllProducts();
   }

   @GetMapping("{id}")
   public Product getProductById(@PathVariable Long id) {
      return productServiceClient.fetchProductById(id);
   }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Product {
   private Long id;
   private String name;
   private String type;
   private String version;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class ProductChangedEvent {
   private Long productId;
   private String productName; // some state
}

@Component
@RequiredArgsConstructor
class ProductServiceClient {
   private final RestTemplate restTemplate;

   @Value("${serviceClients.products.baseUrl}")
   String baseUrl;

   @Data
   static public class ProductServiceResponse {
      private List<Product> products = new ArrayList<>();
   }

   public List<Product> fetchAllProducts() {
      return restTemplate.getForObject(baseUrl + "/products", ProductServiceResponse.class).getProducts();
   }

   public Product fetchProductById(long id) {
      return restTemplate.getForObject(baseUrl + "/products/" + id, Product.class);
   }
}


