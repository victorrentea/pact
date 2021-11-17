package victor.training.pact.consumer;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static java.time.LocalDate.parse;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "ProductService")
public class OrderHistory_Product_PactTest {

   @Autowired
   ProductServiceClient productServiceClient;

   @Pact(consumer = "OrderHistory")
   public RequestResponsePact pact_product10(PactDslWithProvider builder) {
      return builder
          .given("product id 10 exists")
          .uponReceiving("get product with id 10")
          .path("/products/10")
          .willRespondWith()
          .status(200)
          .body(new PactDslJsonBody()
              .integerType("id", 10L)
              .stringType("name", "::name::")
              .integerType("supplierId", 13)
          )
          .toPact();
   }

   @Test
   @PactTestFor(pactMethod = "pact_product10", port = "9999")
   void thin_clientTest(MockServer mockServer) throws IOException {
      productServiceClient.baseUrl = mockServer.getUrl();
      Product product = productServiceClient.fetchProductById(10L);

      System.out.println(product);

      assertThat(product).isEqualTo(new Product(10L, "::name::", 13L));
   }

   @Autowired
   OrderHistoryController controller;
   @Autowired
   OrderHistoryRepo orderHistoryRepo;

   @Test
   @PactTestFor(pactMethod = "pact_product10", port = "9999")
   void thick_E2eTest(MockServer mockServer) throws IOException {
      productServiceClient.baseUrl = mockServer.getUrl();
      orderHistoryRepo.save(new OrderHistory(55L, 10L, 1L, 2, parse("2021-11-17")));

      List<OrderHistoryDto> list = controller.getHistory(1L);

      System.out.println(list);

      assertThat(list)
          .hasSize(1)
          .first()
          .isEqualTo(new OrderHistoryDto(55L, "::name::", 2, "2021-11-17", 13L));
   }

}
