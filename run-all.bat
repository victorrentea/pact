cd catalog
call mvn clean test pact:publish

cd ../order-history
call mvn clean test pact:publish

cd ../product
call mvn clean verify -Dpact.verifier.publishResults=true -Dpact.provider.version=1.0-SNAPSHOT
