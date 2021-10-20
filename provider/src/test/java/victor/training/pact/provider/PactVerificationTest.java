package victor.training.pact.provider;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.IgnoreMissingStateChange;
import au.com.dius.pact.provider.junitsupport.Provider;
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
@IgnoreMissingStateChange // ?
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

}
