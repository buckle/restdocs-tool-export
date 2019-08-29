# Restdocs Tool Export [![CircleCI](https://circleci.com/gh/buckle/restdocs-tool-export/tree/master.svg?style=svg)](https://circleci.com/gh/buckle/restdocs-tool-export/tree/master)

Generates AsciiDoc snippets via Spring Restdocs that are exports for Insomnia or Postman that can be download and imported. 

## Test Configuration

`ToolExportSnippet` needs to be initialized and called for each documentation request. Then include the appropriate snippet in your docs. 

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
    document("reqeust-test",
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
