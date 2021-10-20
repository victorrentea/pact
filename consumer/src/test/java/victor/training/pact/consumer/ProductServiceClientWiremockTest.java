package victor.training.pact.consumer;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.lanwen.wiremock.ext.WiremockResolver;
import ru.lanwen.wiremock.ext.WiremockResolver.Wiremock;
import ru.lanwen.wiremock.ext.WiremockUriResolver;
import ru.lanwen.wiremock.ext.WiremockUriResolver.WiremockUri;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({WiremockResolver.class, WiremockUriResolver.class})
@SpringBootTest
public class ProductServiceClientWiremockTest {

   @Autowired
   ProductServiceClient productServiceClient;

   @Test
   void fetchProducts(@Wiremock WireMockServer server, @WiremockUri String uri) {
      productServiceClient.baseUrl = uri;
      server.stubFor(
          get(urlPathEqualTo("/products"))
              .willReturn(aResponse()
                  .withStatus(200)
                  .withBody("""
                      {
                      "products": [
                            {
                                "id": 9,
                                "type": "CREDIT_CARD",
                                "name": "GEM Visa",
                                "version": "v2"
                            },
                            {
                                "id": 10,
                                "type": "CREDIT_CARD",
                                "name": "28 Degrees",
                                "version": "v1"
                            }
                        ]
                      }
                      """)
                  .withHeader("Content-Type", "application/json"))
      );

      ProductServiceResponse response = productServiceClient.fetchAllProducts();
      assertThat(response.getProducts())
          .hasSize(2)
          .extracting("id").containsExactlyInAnyOrder(9L, 10L);
   }

   @Test
   void getProductById(@Wiremock WireMockServer server, @WiremockUri String uri) {
      productServiceClient.baseUrl = uri;
      server.stubFor(get(urlPathEqualTo("/products/10"))
          .willReturn(aResponse()
              .withStatus(200)
              .withHeader("Content-Type", "application/json")
              .withBody("""
                   {
                      "id": 10,
                      "type": "CREDIT_CARD",
                      "name": "28 Degrees",
                      "version": "v1"
                  }
                   """)
          )
      );

      Product product = productServiceClient.fetchProductById(10);
      assertThat(product).isEqualTo(new Product(10L, "28 Degrees", "CREDIT_CARD", "v1"));
   }


}

