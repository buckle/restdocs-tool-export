package restdocs.tool.export.insomnia.exporter.creators;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import restdocs.tool.export.insomnia.exporter.InsomniaConstants;
import restdocs.tool.export.insomnia.exporter.Pair;
import restdocs.tool.export.insomnia.exporter.variable.InsomniaVariableHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.ALLOW;
import static restdocs.tool.export.insomnia.utils.InsomniaAssertionUtils.assertIdMatches;
import static restdocs.tool.export.utils.RestDocUtils.findHttpHeaderForName;

public class HeadersCreatorTest {

  @Test
  void create() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML));
    httpHeaders.setAllow(Sets.newSet(HttpMethod.GET, HttpMethod.POST));

    Set<Pair> headers = new HeadersCreator().create(httpHeaders);

    assertNotNull(headers);
    Pair acceptHeader = findHeaderForName(headers, ACCEPT);
    assertNotNull(acceptHeader);
    assertIdMatches(InsomniaConstants.PAIR_ID, acceptHeader.getId());
    assertNotNull(acceptHeader.getId());
    assertEquals(StringUtils.join(findHttpHeaderForName(httpHeaders, ACCEPT).getValue(), ","), acceptHeader.getValue());

    Pair allowHeader = findHeaderForName(headers, ALLOW);
    assertNotNull(allowHeader);
    assertIdMatches(InsomniaConstants.PAIR_ID, allowHeader.getId());
    assertEquals(StringUtils.join(findHttpHeaderForName(httpHeaders, ALLOW).getValue(), ","), allowHeader.getValue());
  }

  @Test
  void createWithVariables() {
    HttpHeaders httpHeaders = new HttpHeaders();
    String headerKey = "<<variable>>";
    httpHeaders.put(headerKey, List.of("<<value1>>", "<<value2>>"));
    InsomniaVariableHandler insomniaVariableHandler = spy(new InsomniaVariableHandler());
    String replacedKey = "replacement";
    when(insomniaVariableHandler.replaceVariables(eq(headerKey))).thenReturn(replacedKey);
    String replacedValue = "{{_.value1}},{{_.value2}}";
    when(insomniaVariableHandler.replaceVariables(eq("<<value1>>,<<value2>>"))).thenReturn(replacedValue);

    Set<Pair> headers = new HeadersCreator(insomniaVariableHandler).create(httpHeaders);

    assertNotNull(headers);
    Pair variableHeader = findHeaderForName(headers, replacedKey);
    assertNotNull(variableHeader);
    assertIdMatches(InsomniaConstants.PAIR_ID, variableHeader.getId());
    assertNotNull(variableHeader.getId());
    assertEquals(replacedValue, variableHeader.getValue());

    verify(insomniaVariableHandler).replaceVariables(headerKey);
    verify(insomniaVariableHandler).replaceVariables("<<value1>>,<<value2>>");
    verify(insomniaVariableHandler, times(2)).replaceVariables(anyString());
  }

  @Test
  void createWhenHeadersNull() {
    Set<Pair> pairs = new HeadersCreator().create(null);

    assertNull(pairs);
  }

  @Test
  void createWhenHeadersEmpty() {
    HttpHeaders httpHeaders = new HttpHeaders();

    Set<Pair> pairs = new HeadersCreator().create(httpHeaders);

    assertNull(pairs);
  }

  private Pair findHeaderForName(Set<Pair> pairs, String name) {
    if(pairs != null) {
      return pairs.stream()
                    .filter(header -> name.equals(header.getName()))
                    .findFirst()
                    .orElse(null);
    }

    return null;
  }
}
