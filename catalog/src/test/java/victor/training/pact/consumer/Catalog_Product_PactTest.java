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
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "ProductService")
public class Catalog_Product_PactTest {

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
      List<Product> response = productServiceClient.fetchAllProducts();

      System.out.println(response);

      assertThat(response)
          .hasSize(2)
          .first().isEqualTo(new Product(9L, "::name::", "CREDIT_CARD", null));
   }

   @Pact(consumer = "ProductCatalog")
   public RequestResponsePact allProducts_zero(PactDslWithProvider builder) {
      return builder
          .given("no products exist")
          .uponReceiving("get all products")
          .path("/products")
          .willRespondWith()
          .status(200)
          .body(new PactDslJsonBody()
              .array("products")
              .closeArray()
          )
          .toPact();
   }

   @Test
   @PactTestFor(pactMethod = "allProducts_zero", port="9999")
   void testAllProducts_noneReturned(MockServer mockServer) throws IOException {
      productServiceClient.baseUrl = mockServer.getUrl();
      List<Product> response = productServiceClient.fetchAllProducts();

      System.out.println(response);

      assertThat(response).isEmpty();
   }

   // ==============


   @Pact(consumer = "ProductCatalog")
   public RequestResponsePact pact_productById(PactDslWithProvider builder) {
      return builder
          .given("product id 10 exists")
          .uponReceiving("get product with ID 10")
          .path("/products/10")
          .willRespondWith()
          .status(200)
          .body(new PactDslJsonBody()
              .integerType("id", 10L)
              .stringType("name", "::name::")
              .stringType("type", "CREDIT_CARD")
              .stringType("version", "::version::")
          )
          .toPact();
   }

   @Test
   @PactTestFor(pactMethod = "pact_productById", port="9999")
   void testProductById(MockServer mockServer) throws IOException {
      productServiceClient.baseUrl = mockServer.getUrl();
      Product product = productServiceClient.fetchProductById(10L);

      System.out.println(product);

      assertThat(product).isEqualTo(new Product(10L, "::name::", "CREDIT_CARD", "::version::"));
   }

   @Pact(consumer = "ProductCatalog")
   public RequestResponsePact productById404(PactDslWithProvider builder) {
      return builder
          .given("product id 13 does not exist")
          .uponReceiving("get product with ID 13")
          .path("/products/13")
          .willRespondWith()
          .status(404)
          .toPact();
   }

   @Test
   @PactTestFor(pactMethod = "productById404", port="9999")
   void testProductByIdNotFound(MockServer mockServer) throws IOException {
      productServiceClient.baseUrl = mockServer.getUrl();

      assertThatThrownBy(() -> productServiceClient.fetchProductById(13L))
          .isInstanceOf(HttpClientErrorException.class)
          .hasMessageContaining("404");

   }
}
