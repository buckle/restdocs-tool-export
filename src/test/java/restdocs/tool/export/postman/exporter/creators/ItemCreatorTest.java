package restdocs.tool.export.postman.exporter.creators;

import org.junit.jupiter.api.Test;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.operation.OperationRequest;
import restdocs.tool.export.postman.exporter.Item;
import restdocs.tool.export.postman.exporter.Request;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static restdocs.tool.export.common.assertion.AssertionUtils.assertName;

public class ItemCreatorTest {

  @Test
  void create() {
    String name = "basename-" + UUID.randomUUID().toString();
    OperationRequest operationRequest = mock(OperationRequest.class);
    Operation operation = mock(Operation.class);
    when(operation.getRequest()).thenReturn(operationRequest);
    when(operation.getName()).thenReturn(name);

    Request request = mock(Request.class);
    RequestCreator requestCreator = mock(RequestCreator.class);
    when(requestCreator.create(operationRequest)).thenReturn(request);

    Item item = new ItemCreator(requestCreator).create(operation);

    assertNotNull(item);
    assertName(item.getName(), "basename");
    assertEquals(request, item.getRequest());
  }

  @Test
  void createWhenOperationNull() {
    assertNull(new ItemCreator().create(null));
  }

  @Test
  void createWhenOperationNameNull() {
    OperationRequest operationRequest = mock(OperationRequest.class);
    Operation operation = mock(Operation.class);
    when(operation.getRequest()).thenReturn(operationRequest);

    Request request = mock(Request.class);
    RequestCreator requestCreator = mock(RequestCreator.class);
    when(requestCreator.create(operationRequest)).thenReturn(request);

    Item item = new ItemCreator(requestCreator).create(operation);

    assertNull(item);
  }

  @Test
  void createWhenOperationRequestNull() {
    String name = "basename-" + UUID.randomUUID().toString();
    Operation operation = mock(Operation.class);
    when(operation.getName()).thenReturn(name);

    Item item = new ItemCreator().create(operation);

    assertNull(item);
  }
}
