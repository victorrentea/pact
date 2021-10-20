
### Ported and adapted from 
https://github.com/pact-foundation/pact-workshop-Maven-Springboot-JUnit5

### Broker
Consumer:
mvn pact:publish

Provider:
mvnw verify -Dpact.verifier.publishResults=true -Dpact.provider.version=1.0-SNAPSHOT 
