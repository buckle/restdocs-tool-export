# Restdocs Tool Export [![CircleCI](https://circleci.com/gh/buckle/restdocs-tool-export/tree/master.svg?style=svg)](https://circleci.com/gh/buckle/restdocs-tool-export/tree/master)

Generates AsciiDoc snippets via Spring Restdocs that are exports for Insomnia or Postman that can be download and imported. 

## Artifact Version
`com.github.buckle:restdocs-tool-export:0.0.1-RELEASE`

## Test Configuration

`ToolExportSnippet` needs to be initialized and then called for each documentation request. Once integration tests have ran 
include the generated Insomnia/Postman snippet in your AsciiDoc file. The generated link contains an export of the included tool. 
A very simple example can be seen in the integration test [RestDocsIT](src/test/java/restdocs/tool/export/RestDocsIT.java).

### Initialize
```java
  @Value("${spring.application.name}") protected String applicationName;

  @BeforeEach
  void setUp() throws Exception {
    ToolExportSnippet.initInstance(applicationName, ToolHandlers.INSOMNIA);
  }
```

### Document
```java
    document("request-test",
             pathParameters(parameterWithName("key")
                                       .description("Test path parameter")
             ),
             responseFields(fieldWithPath("responseField")
                                       .description("Test response field")),
             ToolExportSnippet.getInstance()
    );
```

### ADoc Include
```
== Downloads
Insomnia
include::{snippets}/insomnia-download/insomnia.adoc[]
```
