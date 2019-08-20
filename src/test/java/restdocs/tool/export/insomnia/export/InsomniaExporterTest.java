package restdocs.tool.export.insomnia.export;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.internal.util.collections.Sets;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.spy;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.ALLOW;
import static restdocs.tool.export.RestDocUtils.findHttpHeaderForName;
import static restdocs.tool.export.insomnia.export.InsomniaAssertionUtils.assertIdMatches;

@ExtendWith(MockitoExtension.class)
public class InsomniaExporterTest {

  @InjectMocks private InsomniaExporter insomniaExporter = spy(InsomniaExporter.class);

  @Test
  void createHeaders() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML));
    httpHeaders.setAllow(Sets.newSet(HttpMethod.GET, HttpMethod.POST));

    Set<Header> headers = insomniaExporter.createHeaders(httpHeaders);

    assertNotNull(headers);
    Header acceptHeader = findHeaderForName(headers, ACCEPT);
    assertNotNull(acceptHeader);
    assertIdMatches(InsomniaConstants.PAIR_ID, acceptHeader.getId());
    assertNotNull(acceptHeader.getId());
    assertEquals(StringUtils.join(findHttpHeaderForName(httpHeaders, ACCEPT).getValue(), ","), acceptHeader.getValue());

    Header allowHeader = findHeaderForName(headers, ALLOW);
    assertNotNull(allowHeader);
    assertIdMatches(InsomniaConstants.PAIR_ID, allowHeader.getId());
    assertEquals(StringUtils.join(findHttpHeaderForName(httpHeaders, ALLOW).getValue(), ","), allowHeader.getValue());
  }

  @Test
  void createHeadersWhenNull() {
    Set<Header> headers = insomniaExporter.createHeaders(null);

    assertNull(headers);
  }

  private Header findHeaderForName(Set<Header> headers, String name) {
    if(headers != null) {
      return headers.stream()
                    .filter(header -> name.equals(header.getName()))
                    .findFirst()
                    .orElse(null);
    }

    return null;
  }
}
