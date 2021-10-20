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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "ProductService")
public class ProductServiceClientPactTest {

   @Autowired
   ProductServiceClient productServiceClient;

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
              .integerType("id", 9L)
              .stringType("name", "::name::")
              .stringType("type", "CREDIT_CARD")
                  // only the required fields
              .closeObject()
              .closeArray()
          )
          .toPact();
   }

   @Test
   @PactTestFor(pactMethod = "allProducts", port="9999")
   void testAllProducts(MockServer mockServer) throws IOException {
      productServiceClient.baseUrl = mockServer.getUrl();
      ProductServiceResponse response = productServiceClient.fetchAllProducts();

      System.out.println(response.getProducts());

      assertThat(response.getProducts())
          .hasSize(2)
          .first().isEqualTo(new Product(9L, "::name::", "CREDIT_CARD", null));
   }
}
