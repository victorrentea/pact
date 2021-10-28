package victor.training.pact.provider;

import au.com.dius.pact.core.model.Interaction;
import au.com.dius.pact.core.model.Pact;
import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit5.MessageTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

@Slf4j
@Provider("ProductServiceAsync")
@PactFolder("pacts")
public class AmqpContractTest {

  @TestTemplate
  @ExtendWith(PactVerificationInvocationContextProvider.class)
  void testTemplate(Pact pact, Interaction interaction, PactVerificationContext context) {
    log.info("testTemplate called: " + pact.getProvider().getName() + ", " + interaction);
    context.verifyInteraction();
  }

  @BeforeEach
  void before(PactVerificationContext context) {
    context.setTarget(new MessageTestTarget());
  }

  @State("SomeProviderState")
  public void someProviderState() {
    log.info("SomeProviderState callback");
  }

  @PactVerifyProvider("a test message")
  public String verifyMessageForOrder() {
    return "{\"testParam1\": \"value1\",\"testParam2\": \"value2\"}";
  }

}