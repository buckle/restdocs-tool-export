package restdocs.tool.export.insomnia.exporter.creators;

import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.OperationRequest;
import restdocs.tool.export.insomnia.exporter.Body;
import restdocs.tool.export.insomnia.exporter.Pair;
import restdocs.tool.export.insomnia.exporter.variable.InsomniaVariableHandler;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BodyCreatorTest {

  @Test
  void create() {
    String contentBody = UUID.randomUUID().toString();

    OperationRequest operationRequest = mock(OperationRequest.class);
    when(operationRequest.getContentAsString()).thenReturn(contentBody);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    when(operationRequest.getHeaders()).thenReturn(headers);

    InsomniaVariableHandler insomniaVariableHandler = spy(new InsomniaVariableHandler());
    Body body = new BodyCreator(insomniaVariableHandler).create(operationRequest);

    assertNotNull(body);
    assertEquals(contentBody, body.getText());
    assertEquals(MediaType.APPLICATION_JSON.toString(), body.getMimeType());
    verify(insomniaVariableHandler).replaceVariables(contentBody);
  }

  @Test
  void createWhenNullRequest() {
    Body body = new BodyCreator().create(null);

    assertNull(body);
  }

  @Test
  void createWhenHeadersNull() {
    String contentBody = UUID.randomUUID().toString();

    OperationRequest operationRequest = mock(OperationRequest.class);
    when(operationRequest.getContentAsString()).thenReturn(contentBody);

    when(operationRequest.getHeaders()).thenReturn(null);

    Body body = new BodyCreator().create(operationRequest);

    assertNotNull(body);
    assertEquals(contentBody, body.getText());
    assertNull(body.getMimeType());
  }

  @Test
  void createWhenContentTypeNull() {
    String contentBody = UUID.randomUUID().toString();

    OperationRequest operationRequest = mock(OperationRequest.class);
    when(operationRequest.getContentAsString()).thenReturn(contentBody);

    HttpHeaders headers = new HttpHeaders();
    headers.setAllow(Sets.newSet(HttpMethod.GET));
    when(operationRequest.getHeaders()).thenReturn(headers);

    Body body = new BodyCreator().create(operationRequest);

    assertNotNull(body);
    assertEquals(contentBody, body.getText());
    assertNull(body.getMimeType());
  }

  @Test
  void createWhenWhenContentTypeIsFormUrlEncoded() {
    String formValue = UUID.randomUUID().toString();
    String contentBody = "form1=" + formValue;

    OperationRequest operationRequest = mock(OperationRequest.class);
    when(operationRequest.getContentAsString()).thenReturn(contentBody);

    HttpHeaders headers = new HttpHeaders();
    headers.setAllow(Sets.newSet(HttpMethod.GET));
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    when(operationRequest.getHeaders()).thenReturn(headers);

    InsomniaVariableHandler insomniaVariableHandler = spy(new InsomniaVariableHandler());
    Body body = new BodyCreator(insomniaVariableHandler).create(operationRequest);

    assertNotNull(body);
    assertEquals(1, body.getParams().size());
    Pair firstParam = body.getParams().iterator().next();
    assertEquals("form1", firstParam.getName());
    assertEquals(formValue, firstParam.getValue());
    assertEquals(MediaType.APPLICATION_FORM_URLENCODED_VALUE, body.getMimeType());
    verify(insomniaVariableHandler).replaceVariables("form1");
    verify(insomniaVariableHandler).replaceVariables(formValue);
  }

  @Test
  void createWhenContentTypeIsFormUrlEncodedButNoFormParameters() {
    OperationRequest operationRequest = mock(OperationRequest.class);
    when(operationRequest.getContentAsString()).thenReturn(null);

    HttpHeaders headers = new HttpHeaders();
    headers.setAllow(Sets.newSet(HttpMethod.GET));
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    when(operationRequest.getHeaders()).thenReturn(headers);

    Body body = new BodyCreator().create(operationRequest);

    assertNotNull(body);
    assertNull(body.getText());
    assertNull(body.getParams());
    assertEquals(MediaType.APPLICATION_FORM_URLENCODED_VALUE, body.getMimeType());
  }

  @Test
  void createWhenWhenContentTypeIsFormUrlEncoded_WithVariables() {
    String formValue = "testValue";
    String contentBody = "<<form1>>=<<" + formValue + ">>";

    OperationRequest operationRequest = mock(OperationRequest.class);
    when(operationRequest.getContentAsString()).thenReturn(contentBody);

    HttpHeaders headers = new HttpHeaders();
    headers.setAllow(Sets.newSet(HttpMethod.GET));
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    when(operationRequest.getHeaders()).thenReturn(headers);

    Body body = new BodyCreator().create(operationRequest);

    assertNotNull(body);
    assertEquals(1, body.getParams().size());
    Pair firstParam = body.getParams().iterator().next();
    assertEquals("{{_.form1}}", firstParam.getName());
    assertEquals("{{_.testvalue}}", firstParam.getValue());
    assertEquals(MediaType.APPLICATION_FORM_URLENCODED_VALUE, body.getMimeType());
  }
}
