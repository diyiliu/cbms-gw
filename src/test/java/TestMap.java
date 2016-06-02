import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: TestMap
 * Author: DIYILIU
 * Update: 2016-02-25 15:32
 */
public class TestMap {

    @Test
    public void test() {

        Map map1 = new HashMap();

        map1.put("a", 1);
        map1.put("b", 1);
        map1.put("c", 1);

        Map map2 = new HashMap();

        map2.put("a", 2);
        map2.put("b", 2);
        map2.put("c", 2);
        map2.put("d", 2);

        Map map = new HashMap();
        map.putAll(map1);
        map.putAll(map2);

        System.out.println(map.size());
        System.out.println(map.get("a"));

        map.put("a", 0);

        System.out.println(map.get("a"));
        System.out.println(map1.get("a"));
        System.out.println(map2.get("a"));
    }
}
