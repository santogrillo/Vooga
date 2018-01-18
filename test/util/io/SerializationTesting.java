package util.io;

import com.google.gson.reflect.TypeToken;
import org.junit.Test;


import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import static junit.framework.TestCase.assertEquals;

/**
 * Test GSON serialization, including and especially serialization of element properties.
 *
 * @author Ben Schwennesen
 */
public class SerializationTesting {

    private SerializationUtils serializationUtils = new SerializationUtils();

    @Test
    public void testNumberSerializing() {
        Class doubleClass = double.class;
        double num = 1.5;
        String doubleSerialized = serializationUtils.serializeElementProperty(num);
        assertEquals("Can (de)serialize double property using gson", num,
                serializationUtils.deserializeElementProperty(doubleSerialized, doubleClass));
    }

    @Test
    public void testMapSerialization() {
        Map<Double, String> map = new TreeMap<>();
        Class mapClass = map.getClass();
        System.out.println();
        map.put(0.5, "Troop1");
        map.put(0.4, "Troop2");
        map.put(0.1, "Troop3");
        String mapSerialized = serializationUtils.serializeElementProperty(map);
        Map<Double, String> mapAgain = (Map<Double, String>)
                serializationUtils.deserializeElementProperty(mapSerialized, map.getClass());
        assertEquals("Maps have same size", mapAgain.size(), map.size());
        for (Double key : map.keySet()) {
            System.out.println(key + " " + map.get(key));
            assertEquals("Can (de)serialize map property using gson", map.get(key), mapAgain.get(key));
        }
    }

    @Test
    public void testStringSerialization() {

    }
}
