package restdocs.tool.export.insomnia.export;

import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PairUtilsTest {

  @Test
  void findPairByNameValue() {
    Pair pair1 = PairBuilder.builder().build();
    Pair pair2 = PairBuilder.builder().build();
    Set<Pair> pairs = Sets.newSet(pair1, pair2);

    assertEquals(pair1, PairUtils.findPairByNameValue(pairs, pair1.getName(), pair1.getValue()));
    assertEquals(pair2, PairUtils.findPairByNameValue(pairs, pair2.getName(), pair2.getValue()));
    assertNull(PairUtils.findPairByNameValue(pairs, UUID.randomUUID().toString(), UUID.randomUUID().toString()));
  }

  @Test
  void findPairByNameValueWhenNullPairs() {
    Pair pair1 = PairBuilder.builder().build();

    assertNull(PairUtils.findPairByNameValue(null, pair1.getName(), pair1.getValue()));
  }

  @Test
  void findPairByNameValueWhenNullName() {
    Pair pair1 = PairBuilder.builder().build();
    Set<Pair> pairs = Sets.newSet(pair1);

    assertNull(PairUtils.findPairByNameValue(pairs, null, pair1.getValue()));
  }

  @Test
  void findPairByNameValueWhenNullValue() {
    Pair pair1 = PairBuilder.builder().build();
    Set<Pair> pairs = Sets.newSet(pair1);

    assertNull(PairUtils.findPairByNameValue(pairs, pair1.getName(), null));
  }
}
