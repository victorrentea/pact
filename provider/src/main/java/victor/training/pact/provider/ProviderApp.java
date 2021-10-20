package victor.training.pact.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@SpringBootApplication
public class ProviderApp implements CommandLineRunner {
   public static void main(String[] args) {
       SpringApplication.run(ProviderApp.class, args);
   }
   @Override
   public void run(String... args) {
      productRepository.save(new Product("Headphones","CREDIT_CARD", "v1", "code1"));
      productRepository.save(new Product("Microphone","CREDIT_CARD", "v1", "code2"));
      productRepository.save(new Product("Monitor","CREDIT_CARD", "v1", "code3"));
   }

   @Autowired
   private ProductRepository productRepository;

   @GetMapping("/products")
   public ProductsResponse allProducts() {
      return new ProductsResponse((List<Product>) productRepository.findAll());
   }

   @GetMapping("/product/{id}")
   public Optional<Product> productById(@PathVariable("id") Long id) {
      return productRepository.findById(id);
   }
}
