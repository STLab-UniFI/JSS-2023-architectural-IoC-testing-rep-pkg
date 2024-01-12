# Proof of Concept Implementation

The present Proof of Concept (PoC) enables the automatic generation of managed component Data Flow Graphs (mcDFG).

The PoC consists of a CDI extension that extracts runtime information from the three-tier software architecture under observation and generates the corresponding mcDFG. 
The provided PoC can be associated with Java applications and is capable of generating mcDFGs in the presence of components managed by CDI or EJB, both from Java EE and Jakarta EE.

To obtain the mcDFG through the PoC from a system of your choice, follow these steps:

## Getting started

1. Clone the repository
2. Copy the directories `beanTimelineManager`, `timeLine`, and `unravel` from `PoC_code/src/main/java/` of the PoC to the source directory of the target application. If the application is managed with maven the default path is  `/src/main/java/`. 
3. Add the following dependencies in the pom.xml of the target application:

```
<dependency>
    <groupId>javax.enterprise</groupId>
  <artifactId>cdi-api</artifactId>
  <version>2.0.SP1</version>
  <scope>provided</scope>
</dependency>
 
<dependency>
  <groupId>org.jboss.weld</groupId>
  <artifactId>weld-core-impl</artifactId>
  <version>5.0.0.Final</version>
</dependency>
```
4. Register the PoC as CDI extension for your application
    - To do this, it is sufficient to copy the directory services from the `PoC_code/src/main/resources/META-INF/` path of the PoC to the `src/main/resources/META-INF/` path of the target application.
5. Re-build the target application.

#### Run the PoC

1. *Optional*: Configure the filter. Namely [InstanceFilter.java](PoC_code/src/main/java/beanTimelineManager/filter/InstanceFilter.java).  
    - In order to observe exclusively the instances of your interest, it is sufficient to update the variable `PACKAGE_OF_INTEREST` inside the [InstanceFilter.java](PoC_code/src/main/java/beanTimelineManager/filter/InstanceFilter.java) file to include the desired classes/packages.
2. Build the project and deploy it on the server
3. Use the application under observation normally
4. Upon completion, the resulting mcDFG will be displayed in the console.
