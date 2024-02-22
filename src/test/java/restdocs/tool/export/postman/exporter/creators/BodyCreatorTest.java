package restdocs.tool.export.postman.exporter.creators;

import org.junit.jupiter.api.Test;
import org.springframework.restdocs.operation.OperationRequest;
import restdocs.tool.export.postman.exporter.Body;
import restdocs.tool.export.postman.variable.PostmanVariableHandler;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BodyCreatorTest {

  @Test
  void create() {
    String sBody = UUID.randomUUID().toString();
    OperationRequest request = mock(OperationRequest.class);
    when(request.getContentAsString()).thenReturn(sBody);

    Body body = new BodyCreator().create(request);

    assertNotNull(body);
    assertEquals("raw", body.getMode());
    assertEquals(sBody, body.getRaw());
  }

  @Test
  void createWithVariable() {
    String sBody = "test <<here>>";
    OperationRequest request = mock(OperationRequest.class);
    when(request.getContentAsString()).thenReturn(sBody);

    PostmanVariableHandler postmanVariableHandler = spy(new PostmanVariableHandler());
    Body body = new BodyCreator(postmanVariableHandler).create(request);

    assertNotNull(body);
    assertEquals("raw", body.getMode());
    assertEquals("test {{here}}", body.getRaw());
    verify(postmanVariableHandler).replaceVariables(sBody);
  }

  @Test
  void createWhenRequestNull() {
    assertNull(new BodyCreator().create(null));
  }
}
