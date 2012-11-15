#˜/bin/bash

mvn clean install -DskipTests
cp target/easyFaces-core-1.1.jar /Users/fmq/servers/jboss-as-7.1.1.Final/standalone/deployments/easyfaces.war/WEB-INF/lib/easyFaces-core-1.1.jar 
touch /Users/fmq/servers/jboss-as-7.1.1.Final/standalone/deployments/easyfaces.war.dodeploy
