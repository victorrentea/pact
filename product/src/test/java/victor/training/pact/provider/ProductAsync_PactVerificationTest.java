package victor.training.pact.provider;

import au.com.dius.pact.core.model.Interaction;
import au.com.dius.pact.core.model.Pact;
import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit5.MessageTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.IgnoreMissingStateChange;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.PactBrokerAuth;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.function.StreamBridge;

import static org.mockito.Mockito.*;

@Slf4j
@Provider("ProductServiceAsync")
@IgnoreMissingStateChange
@SpringBootTest

//@PactFolder("pacts")
@PactBroker(url="http://localhost:9292",  authentication = @PactBrokerAuth(username = "pact_workshop", password = "pact_workshop"))
public class ProductAsync_PactVerificationTest {

  @TestTemplate
  @ExtendWith(PactVerificationInvocationContextProvider.class)
  void testTemplate(Pact pact, Interaction interaction, PactVerificationContext context) {
    context.verifyInteraction();
  }

  @BeforeEach
  void before(PactVerificationContext context) {
    context.setTarget(new MessageTestTarget());
  }



  // OPTION 1: THIN direct trigger
//  @PactVerifyProvider("when product id 10 changed")
//  public String verifyMessageForOrder_thin() throws JsonProcessingException {
//    return new ObjectMapper().writeValueAsString(new ProductChangedEvent(10L, "::name::"));
//  }


  // OPTION 2: THICK trigger through logic
  @Autowired
  ProductRepository productRepository;
  @Autowired
  ProductController controller;
  @MockBean
  StreamBridge streamBridge;
  @Captor
  ArgumentCaptor<Object> messageCaptor;

  // OPTION 2: Deep trigger
  @PactVerifyProvider("when product id 10 changed")
  public String verifyMessageForOrder_e2e() throws JsonProcessingException {
    try {
      // given
      productRepository.save(new Product().setId(10L));

      //when
      controller.updateProduct(10L, new Product().setName("::name::"));

      // then
      verify(streamBridge).send(eq("productChanged-out-0"), messageCaptor.capture());
      return new ObjectMapper().writeValueAsString(messageCaptor.getValue());
    } catch (Exception e) {
      log.error("FAIL", e);
      throw e;
    }
  }

}