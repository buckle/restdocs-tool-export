# Restdocs Tool Export [![CircleCI](https://circleci.com/gh/buckle/restdocs-tool-export/tree/master.svg?style=svg)](https://circleci.com/gh/buckle/restdocs-tool-export/tree/master)

Generates AsciiDoc snippets via Spring Restdocs that are exports for Insomnia or Postman that can be download and imported. 

## Artifact Version
`com.github.buckle:restdocs-tool-export:0.0.4-RELEASE`

## Test Configuration

`ToolExportSnippet` needs to be initialized and then called for each documentation request. Once integration tests have run 
include the generated Insomnia/Postman snippet in your AsciiDoc file. The generated link contains an export of the included tool. 
A very simple example can be seen in the integration test [RestDocsIT](src/test/java/restdocs/tool/export/RestDocsIT.java).

### Initialize
```java
  @Value("${spring.application.name}") protected String applicationName;

  @BeforeEach
  void setUp() throws Exception {
    ToolExportSnippet.initInstance(applicationName, ToolHandlers.INSOMNIA, ToolHandlers.POSTMAN);
    ToolExportSnippet.setProperty(HOST_VARIABLE_ENABLED, true);
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

Postman
include::{snippets}/postman-download/postman.adoc[]
```

### Host Variable
By default, host will be exported as its own variable, a property can be used to disable it
```java
    ToolExportSnippet.setProperty(HOST_VARIABLE_ENABLED, false);
```

### Variables
Anywhere `<<anystring>>` appears in the request, a variable will be created in the exports
```java
     mockMvc.perform(post("/post/test/{pathVariable}?param1={queryParam}", "<<path-variable>>", "<<query-param-variable>>")
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-Key", "<<header-variable>>")
        .content(objectMapper.writeValueAsBytes(postData))
        .secure(true))
        .andExpect(status().isOk())
        .andDo(document(...));

```

### Header Variables
When << can't be included in the request directly, you can update values using a preprocessor like HeaderVariablePreprocessor

replaceHeaderValueWithVariable() will change the value of any matching headers to a variable (ex. x_key_header).
```java
    document("request-test",
             preprocessRequest(
              replaceHeaderValueWithVariable("X-Key")),
              pathParameters(parameterWithName("key")
                                       .description("Test path parameter")
             ),
             ToolExportSnippet.getInstance()
    );
```
replaceHeaderValueWithVariable can be applied to all requests by customizing mockmvc
Use a preprocessor like modifyHeaders().add() if you want to add this header to every request,
otherwise it will just replace the values when the header is present
```java
  @Autowired protected WebApplicationContext context;
  
  MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.context)...
    .operationPreprocessors().withRequestDefaults(
        modifyHeaders().add("x-api-key", ""),
        replaceHeaderValueWithVariable("x-api-key")))
    .build();
```