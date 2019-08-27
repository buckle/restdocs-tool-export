package restdocs.tool.export.insomnia.exporter.creators;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import restdocs.tool.export.insomnia.exporter.InsomniaConstants;
import restdocs.tool.export.insomnia.exporter.Pair;

import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.ALLOW;
import static restdocs.tool.export.utils.RestDocUtils.findHttpHeaderForName;
import static restdocs.tool.export.insomnia.exporter.InsomniaAssertionUtils.assertIdMatches;

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
