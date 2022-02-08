package victor.training.pact.provider;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@RestController
@SpringBootApplication
public class ProductApp {
   public static void main(String[] args) {
      SpringApplication.run(ProductApp.class, args);
   }
}

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "not found")
class NotFoundException extends RuntimeException { }

@RestController
@RequiredArgsConstructor
class ProductController {
   private final ProductRepository productRepository;

   @GetMapping("/products")
   public ProductsResponse getAllProducts() {
      return new ProductsResponse((List<Product>) productRepository.findAll());
   }

   @GetMapping("/products/{id}") // TODO introduce typo
   public Product getProductById(@PathVariable("id") Long id) {
      return productRepository.findById(id).orElseThrow(NotFoundException::new);
   }

   private final StreamBridge streamBridge;
   @PutMapping("/products/{id}") // TODO introduce typo
   public void updateProduct(@PathVariable("id") Long id, @PathVariable Product newProduct) {
      newProduct.setId(id);
      productRepository.save(newProduct);

      ProductChangedEvent event = new ProductChangedEvent(id, newProduct.getName());
      streamBridge.send("productChanged-out-0", event);
   }
}

@Data
class ProductsResponse {
   private final List<Product> products;
}


@Entity
@Data
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
class Product {
   @Id
   private Long id;
   private String name;
   private String type;
   private String version;
   private String code;
   @JsonProperty("supplier")
   private Long supplierId;// TODO remove
}

interface ProductRepository extends CrudRepository<Product, Long> {

}

@Data
@AllArgsConstructor
@NoArgsConstructor
class ProductChangedEvent {
   private Long productId;
   private String productName; // some state
}