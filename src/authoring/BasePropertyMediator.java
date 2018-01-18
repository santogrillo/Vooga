package authoring;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 
 * @author mmosca
 *
 */
public class BasePropertyMediator {
	private Map<String, List<String>> allBaseProperties;
	private List<BasePropertySelector> dropdownSelectors;
	private Map<String, String> selectedBaseProperties;
	
	public BasePropertyMediator(Map<String, List<String>> allProperties) {
		allBaseProperties = allProperties;
		dropdownSelectors = new ArrayList<BasePropertySelector>();
		selectedBaseProperties = new TreeMap<String, String>();
		addSelectors();
	}
	
	private void addSelectors() {
		for(int i = 0; i < allBaseProperties.size(); i++) {
			List<String> propertyNameList = new ArrayList<>(allBaseProperties.keySet());
			dropdownSelectors.add(new BasePropertySelector(this, propertyNameList.get(i), allBaseProperties.get(i)));
		}
	}
	
	void addBaseProperty(String propertyName, String propertySelection) {
		selectedBaseProperties.put(propertyName, propertySelection);
	}
 }
