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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "ProductService")
public class Catalog_Product_PactTest {

   @Autowired
   private ProductServiceClient productServiceClient;

   @Pact(consumer = "ProductCatalog")
   public RequestResponsePact allProducts(PactDslWithProvider builder) {
      return builder
          .given("products exist")
          .uponReceiving("get all products")
          .path("/products")
          .willRespondWith()
          .status(200)
          .body(new PactDslJsonBody()
                  .minArrayLike("products", 1, 2)
                                .integerType("id", 9L, 10L)
                                .stringType("type", "CREDIT_CARD", "CREDIT_CARD")
                                .integerType("supplier", 1, 1)
          )
          .toPact();
   }

   @Test
   @PactTestFor(pactMethod = "allProducts", port = "9999")
   public void testAllProducts(MockServer mockServer) {
      productServiceClient.baseUrl = mockServer.getUrl();

      List<Product> products = productServiceClient.fetchAllProducts();

      assertThat(products)
          .hasSize(2)
          .extracting("id", "type", "supplier")
          .containsExactlyInAnyOrder(
              tuple(9L,"CREDIT_CARD", 1L),
              tuple(10L,"CREDIT_CARD", 1L));
   }

}
