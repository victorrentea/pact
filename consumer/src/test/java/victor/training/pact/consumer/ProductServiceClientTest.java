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

@ExtendWith({ WiremockResolver.class, WiremockUriResolver.class })
@SpringBootTest
public class ProductServiceClientTest {

   @Autowired
   ProductServiceClient productServiceClient;

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

