package victor.training.pact.consumer;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.junit5.ProviderType;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.messaging.Message;
import au.com.dius.pact.core.model.messaging.MessagePact;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "ProductServiceAsync", providerType = ProviderType.ASYNCH)
public class Catalog_ProductAsync_PactTest {

  @Pact(consumer = "ProductCatalog")
  MessagePact createPact(MessagePactBuilder builder) {
    return builder.given("product id 10 exists")
      .expectsToReceive("when product id 10 changed")
//      .withMetadata(Map.of("destination", Matchers.regexp("\\w+\\d+", "X001")))
      .withContent(new PactDslJsonBody()
          .integerType("productId", 10)
          .stringValue("productName", "::name::"))
      .toPact();
  }

  @Test
  @PactTestFor(pactMethod = "createPact")
  void test(List<Message> messages) throws JsonProcessingException {
//    assertThat(messages.get(0).getMetadata()).containsEntry("destination", "X001");

    String contentAsString = messages.get(0).contentsAsString();
    ProductChangedEvent event = new ObjectMapper().readValue(contentAsString, ProductChangedEvent.class);

    assertThat(event).isEqualTo(new ProductChangedEvent(10L, "::name::"));
  }

}