package authoring.LevelToolBar;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.junit.Test;


public class LevelToolBarTest {
	

	public LevelToolBarTest() {
	}
	
	public Map<String,Data2> updateDataMap(Map<String, Data2> tester, int levelRemoved) {
		Map<String, Data2> tempMap = new TreeMap<String, Data2>();
		tester.keySet().stream().forEach(waveKey -> {
			int level = Integer.valueOf(waveKey.split("\\.+")[0]);
			int wave = Integer.valueOf(waveKey.split("\\.+")[1]);
			if (level < levelRemoved) tempMap.put(waveKey, tester.get(waveKey));
			if (level > levelRemoved) {
				tempMap.put(String.valueOf(level-1) + "." + String.valueOf(wave), 
						tester.get(waveKey));
			}
		});
		return tempMap;
	}
	
	@Test
	public void TestingMapRemoval() {
		Map<String, Data2> testMap = new TreeMap<String, Data2>();
		List<String> testList1 = new ArrayList<String>();
		String image1 = "monkey.png";
		testList1.add(image1);
		testList1.add(image1);
		List<String> testList2 = new ArrayList<String>();
		testList2.add(image1);
		
		Data2 a = new Data2(testList1, 1);
		Data2 b = new Data2(testList1, 2);
		Data2 c = new Data2(testList2, 3);
		Data2 d = new Data2(testList2, 4);
		Data2 e = new Data2(testList1, 5);
		Data2 f = new Data2(testList1, 6);
		Data2 g = new Data2(testList1, 7);
		Data2 h = new Data2(testList1, 8);
		Data2 i = new Data2(testList1, 7);
		Data2 j = new Data2(testList1, 8);
		
		testMap.put("1.1", a);
		testMap.put("2.1", b);
		testMap.put("2.2", c);
		testMap.put("3.1", d);
		testMap.put("3.2", e);
		testMap.put("3.3", f);
		testMap.put("3.4", g);
		testMap.put("3.5", h);
		testMap.put("4.1", i);
		testMap.put("5.1", j);
		Map<String, Data2> removeMax = new TreeMap<String, Data2>();
		removeMax.put("1.1", a);
		removeMax.put("2.1", b);
		removeMax.put("2.2", c);
		removeMax.put("3.1", d);
		removeMax.put("3.2", e);
		removeMax.put("3.3", f);
		removeMax.put("3.4", g);
		removeMax.put("3.5", h);
		removeMax.put("4.1", i);
		assertEquals(removeMax, updateDataMap(testMap, 5));
		Map<String, Data2> removeSecond = new TreeMap<String, Data2>();
		removeSecond.put("1.1", a);
		removeSecond.put("2.1", d);
		removeSecond.put("2.2", e);
		removeSecond.put("2.3", f);
		removeSecond.put("2.4", g);
		removeSecond.put("2.5", h);
		removeSecond.put("3.1", i);
		removeSecond.put("4.1", j);
	    assertEquals(removeSecond, updateDataMap(testMap, 2));
	}
	

}

class Data2{   
    List<String> spriteNames;  
    Integer waveId;  
    Data2(List<String> spriteNames, Integer waveId) {
        this.spriteNames = spriteNames; 
        this.waveId = waveId; 
    }  
} 
