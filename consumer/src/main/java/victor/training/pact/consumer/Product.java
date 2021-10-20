package victor.training.pact.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
   private Long id;
   private String name;
   private String type;
   private String version;
}
