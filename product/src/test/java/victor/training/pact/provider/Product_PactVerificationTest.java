package victor.training.pact.provider;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.IgnoreMissingStateChange;
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
@IgnoreMissingStateChange
//@PactFolder("pacts")
@PactBroker(url="http://localhost:9292",  authentication = @PactBrokerAuth(username = "pact_workshop", password = "pact_workshop"))
public class Product_PactVerificationTest {

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
      productRepository.save(new Product(1L, "Headphones","CREDIT_CARD", "v1", "code1", 11L));
      productRepository.save(new Product(2L, "Microphone","CREDIT_CARD", "v1", "code2", 12L));
   }

//   @State("no products exist")
//   void noProductsExist() {
//      productRepository.deleteAll();
//   }
//
//   @State("product id 10 exists")
//   void insertProduct10() {
//      productRepository.save(new Product(10L, "Headphones", "CREDIT_CARD", "v1", "code1", 11L));
//   }

}
