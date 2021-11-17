package victor.training.pact.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class OrderHistoryApp {
   public static void main(String[] args) {
      SpringApplication.run(OrderHistoryApp.class, args);
   }

   @Bean
   public RestTemplate restTemplate() {
      return new RestTemplate();
   }

}
@RestController
@RequiredArgsConstructor
class OrderHistoryController {
   private final ProductServiceClient productServiceClient;
   private final OrderHistoryRepo repo;

   @GetMapping("history/{customerId}")
   public List<OrderHistoryDto> getHistory(@PathVariable Long customerId) {
      return repo.findByCustomerId(customerId).stream()
          .map(history -> enhanceWithProductDetails(history))
          .collect(Collectors.toList());
   }

   private OrderHistoryDto enhanceWithProductDetails(OrderHistory history) {
      Product product = productServiceClient.fetchProductById(history.getProductId());

      OrderHistoryDto dto = new OrderHistoryDto();
      dto.setId(history.getId());
      dto.setCount(history.getItemCount());
      dto.setDate(history.getOrderDate().toString());
      dto.setProductName(product.getName());
      dto.setSupplierId(product.getSupplierId());
      return dto;
   }
}

@NoArgsConstructor
@AllArgsConstructor
@Data
class OrderHistoryDto {
   private Long id;
   private String productName;
   private int count;
   private String date;
   private Long supplierId;
}

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
class OrderHistory {
   @Id
   private Long id;
   private Long productId;
   private Long customerId;
   private int itemCount;
   private LocalDate orderDate;
}
interface OrderHistoryRepo extends JpaRepository<OrderHistory, Long> {
   List<OrderHistory> findByCustomerId(Long customerId);
}

@Component
@RequiredArgsConstructor
class ProductServiceClient {
   private final RestTemplate restTemplate;

   @Value("${serviceClients.products.baseUrl}")
   String baseUrl;

   public Product fetchProductById(long id) {
      return restTemplate.getForObject(baseUrl + "/products/" + id, Product.class);
   }
}
@Data
@AllArgsConstructor
@NoArgsConstructor
class Product {
   private Long id;
   private String name;
   private Long supplierId;
}
