package restdocs.tool.export.postman.exporter.creators;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import restdocs.tool.export.postman.exporter.Header;

import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.ALLOW;
import static restdocs.tool.export.utils.RestDocUtils.findHttpHeaderForName;

public class HeadersCreatorTest {

  @Test
  void create() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML));
    httpHeaders.setAllow(Sets.newSet(HttpMethod.GET, HttpMethod.POST));

    Set<Header> headers = new HeadersCreator().create(httpHeaders);

    assertNotNull(headers);
    assertEquals(2, headers.size());

    Header acceptHeader = findHeaderForName(headers, ACCEPT);
    assertEquals(StringUtils.join(findHttpHeaderForName(httpHeaders, ACCEPT).getValue(), ","), acceptHeader.getValue());

    Header allowHeader = findHeaderForName(headers, ALLOW);
    assertEquals(StringUtils.join(findHttpHeaderForName(httpHeaders, ALLOW).getValue(), ","), allowHeader.getValue());
  }

  @Test
  void createWhenHttpHeadersNull() {
    assertNull(new HeadersCreator().create(null));
  }

  @Test
  void createWhenHttpHeadersEmpty() {
    HttpHeaders httpHeaders = new HttpHeaders();

    assertNull(new HeadersCreator().create(httpHeaders));
  }

  private Header findHeaderForName(Set<Header> headers, String name) {
    if(headers != null) {
      return headers.stream()
                    .filter(header -> name.equals(header.getKey()))
                    .findFirst()
                    .orElse(null);
    }

    return null;
  }
}
