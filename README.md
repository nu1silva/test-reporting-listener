## test-reporting-listener
A Custom TestNG listener to write results to the database.


#### Building the listener
build the listener by giving `mvn clean package` from the source root


#### Usage of the listener
Add the following properties in the pom.xml of the project/module that has the tests
```xml
<dependencies>
    <dependency>
        <groupId>org.wso2.qa.testlink</groupId>
        <artifactId>test-reporting-listener</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
</dependencies>

<plugin>
   <groupId>org.apache.maven.plugins</groupId>
   <artifactId>maven-surefire-plugin</artifactId>
   <version>2.19.1</version>
   <configuration>
      <systemPropertyVariables>
         <component.name>${project.artifactId}</component.name>
         <component.version>${project.version}</component.version>
         <current.build.number>${build.number}</current.build.number>
         <current.platform>${platform}</current.platform>
      </systemPropertyVariables>
      <suiteXmlFiles>
         <suiteXmlFile>src/test/resources/testng.xml</suiteXmlFile>
      </suiteXmlFiles>
   </configuration>
</plugin>
```

Register the following listener and properties in testng.xml
```xml
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite name="Sample_TestSuite" verbose="1">
   <parameter name="component" value="${component.name}" />
   <parameter name="version" value="${component.version}" />
   <parameter name="buildNumber" value="${current.build.number}" />
   <parameter name="platform" value="${current.platform}" />
   <listeners>
      <listener class-name="org.wso2.qa.testlink.TestReporterListener" />
   </listeners>
   
   <test name="tests">
      <packages>
         <package name="org.wso2.carbon.core" />
      </packages>
   </test>
</suite>
```

Testing the listener
```bash
mvn clean test -Dbuild.number=1 -Dplatform=platform1
```