package victor.training.pact.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@SpringBootApplication
public class ConsumerApp {
   public static void main(String[] args) {
      SpringApplication.run(ConsumerApp.class, args);
   }

   @Bean
   public RestTemplate restTemplate() {
      return new RestTemplate();
   }

   @Autowired
   private ProductServiceClient productServiceClient;

   @GetMapping
   public List<Product> getAllProducts() {
      return productServiceClient.fetchAllProducts().getProducts();
   }

   @GetMapping("{id}")
   public Product getProductById(@PathVariable Long id) {
      return productServiceClient.fetchProductById(id);
   }
}


