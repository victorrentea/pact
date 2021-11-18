package victor.training.pact.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Catalog_Product_PactTest {

   @Autowired
   ProductServiceClient productServiceClient;
}
