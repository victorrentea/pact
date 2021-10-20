package victor.training.pact.provider;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.IgnoreMissingStateChange;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Provider("ProductService")
@PactFolder("pacts")
@IgnoreMissingStateChange
//@PactBroker(host="localhost", port = "9292",  authentication = @PactBrokerAuth(username = "pact_workshop", password = "pact_workshop"))
public class PactVerificationTest {

   @LocalServerPort
   int port;

   @Autowired
   ProductRepository productRepository;

   @BeforeEach
   final void before(PactVerificationContext context) {
      context.setTarget(new HttpTestTarget("localhost", port));
   }

   @TestTemplate
   @ExtendWith(PactVerificationInvocationContextProvider.class)
   void test(PactVerificationContext context) {
      context.verifyInteraction();
   }

//   @State(value = "products exists", action = StateChangeAction.SETUP)
//   void productsExists() {
//      productRepository.deleteAll();
//      productRepository.saveAll(Arrays.asList(
//          new Product(100L, "Test Product 1", "CREDIT_CARD", "v1", "CC_001"),
//          new Product(200L, "Test Product 2", "CREDIT_CARD", "v1", "CC_002"),
//          new Product(300L, "Test Product 3", "PERSONAL_LOAN", "v1", "PL_001"),
//          new Product(400L, "Test Product 4", "SAVINGS", "v1", "SA_001")
//      ));
//   }

   @State("products exist")
   void productsExist() {
      productRepository.save(new Product(1L, "Headphones","CREDIT_CARD", "v1", "code1"));
      productRepository.save(new Product(2L, "Microphone","CREDIT_CARD", "v1", "code2"));
      productRepository.save(new Product(3L, "Monitor","CREDIT_CARD", "v1", "code3"));
   }

   @State("no products exist")
   void noProductsExist() {
      productRepository.deleteAll();
   }

   @State("product id 10 exist")
   void insertProduct10() {
      productRepository.save(new Product(10L, "Headphones", "CREDIT_CARD", "v1", "code1"));
   }

}
