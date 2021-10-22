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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "ProductService") // EI
public class ProductServiceClientPactTest {

   @Autowired
   ProductServiceClient productServiceClient;

   @Pact(consumer = "ProductCatalog") // eu
   public RequestResponsePact allProductsX(PactDslWithProvider builder) {
      return builder
          .given("products exist")
          .uponReceiving("get all products")
          .path("/products")
          .willRespondWith()
          .status(200)
          .body(new PactDslJsonBody()
              .minArrayLike("products", 1, 2)
                 .integerType("id", 9L, 10L)
                 .stringType("name", "::name::","::name2::")
                 .stringType("type", "CREDIT_CARD", "LOAN")
                  // only the required fields
                  .closeObject()
              .closeArray())
          .toPact();
   }

   // Consumer-Driven Contract Testing
   @Test
   @PactTestFor(pactMethod = "allProductsX") // arbitrary name
   void testAllProducts(MockServer mockServer) {
      productServiceClient.baseUrl = mockServer.getUrl();

      ProductServiceResponse response = productServiceClient.fetchAllProducts();

      assertThat(response.getProducts())
          .hasSize(2)
          .extracting("id").containsExactlyInAnyOrder(9L, 10L);
   }

   @Pact(consumer = "ProductCatalog") // eu
   public RequestResponsePact fetchAProduct(PactDslWithProvider builder) {
      return builder
          .given("product with id 10 exists")
          .uponReceiving("get product id 10")
          .path("/product/10")
          .willRespondWith()
          .status(200)
          .body(new PactDslJsonBody()
                 .integerType("id", 10L)
                 .stringType("name", "::name::")
                 .stringType("type", "CREDIT_CARD")
                 .stringType("version", "v1")
                  )
          .toPact();
   }

   // Consumer-Driven Contract Testing
   @Test
   @PactTestFor(pactMethod = "fetchAProduct") // arbitrary name
   void getFetchOneProduct(MockServer mockServer) {
      productServiceClient.baseUrl = mockServer.getUrl();

      Product response = productServiceClient.fetchProductById(10L);

      assertThat(response).isEqualTo(new Product(10L, "::name::", "CREDIT_CARD", "v1"));
   }

}
