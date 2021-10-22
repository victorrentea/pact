package victor.training.pact.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@SpringBootApplication
public class ProviderApp {
   public static void main(String[] args) {
       SpringApplication.run(ProviderApp.class, args);
   }

   @Autowired
   private ProductRepository productRepository;

   @GetMapping("/products")
   public ProductsResponse allProducts() {
      return new ProductsResponse((List<Product>) productRepository.findAll());
   }

   @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "not found")
   public static class NotFoundException extends RuntimeException { }


   @GetMapping("/product/{id}") // TODO introduce typo
   public Product productById(@PathVariable("id") Long id) {
      return productRepository.findById(id).orElseThrow(NotFoundException::new);
   }
}
