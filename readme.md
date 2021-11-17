#Consumer-Driven Contract Testing Workshop

Ported and adapted from https://github.com/pact-foundation/pact-workshop-Maven-Springboot-JUnit5

### Broker
Start docker-compose.yaml
Navigate to http://localhost:9292/
user/pass: pact_workshop / pact_workshop

### Publish Pact from Consumer to Broker
mvn pact:publish

### Provider checks all Pacts from Broker
enable @PactBroker instead of @PactFolder

mvnw verify -Dpact.verifier.publishResults=true -Dpact.provider.version=1.0-SNAPSHOT

[WIP] take broker conn details from command line


