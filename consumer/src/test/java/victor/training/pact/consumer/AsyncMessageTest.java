package victor.training.pact.consumer;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.dsl.Matchers;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.junit5.ProviderType;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.messaging.Message;
import au.com.dius.pact.core.model.messaging.MessagePact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "ProductServiceAsync", providerType = ProviderType.ASYNCH)
public class AsyncMessageTest {

  @Pact(consumer = "ProductCatalog")
  MessagePact createPact(MessagePactBuilder builder) {
    PactDslJsonBody body = new PactDslJsonBody();
    body.stringValue("testParam1", "value1");
    body.stringValue("testParam2", "value2");

    Map<String, Object> metadata = new HashMap<>();
    metadata.put("destination", Matchers.regexp("\\w+\\d+", "X001"));

    return builder.given("SomeProviderState")
      .expectsToReceive("a test message")
      .withMetadata(metadata)
      .withContent(body)
      .toPact();
  }


  @Test
  @PactTestFor(pactMethod = "createPact")
  void test(List<Message> messages) {
    assertThat(new String(messages.get(0).contentsAsBytes())).isEqualTo("{\"testParam1\":\"value1\",\"testParam2\":\"value2\"}");
    assertThat(messages.get(0).getMetadata()).containsEntry("destination", "X001");
  }

}