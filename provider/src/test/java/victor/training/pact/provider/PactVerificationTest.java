package victor.training.pact.provider;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.PactBrokerAuth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Provider("ProductService")
//@PactFolder("pacts")
//@IgnoreMissingStateChange
@PactBroker(host="localhost", port = "9292",  authentication = @PactBrokerAuth(username = "pact_workshop", password = "pact_workshop"))
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

   @State("products exist")
   void productsExist() {
      productRepository.deleteAll();
      productRepository.save(new Product(1L, "Headphones2","CREDIT_CARD", "v1", "code1"));
      productRepository.save(new Product(2L, "Microphone","CREDIT_CARD", "v1", "code2"));
      productRepository.save(new Product(3L, "Monitor","CREDIT_CARD", "v1", "code3"));
   }
   @State("product with id 10 exists")
   void product10State() {
      productRepository.deleteAll();
      productRepository.save(new Product(10L, "Headphones2","ceva orice", "v1", "code1"));
   }
//
//   @State("no products exist")
//   void noProductsExist() {
//      productRepository.deleteAll();
//   }
//
//   @State("product id 10 exist")
//   void insertProduct10() {
//      productRepository.save(new Product(10L, "Headphones", "CREDIT_CARD", "v1", "code1"));
//   }

}
