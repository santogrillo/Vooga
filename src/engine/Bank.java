package engine;

import engine.game_elements.GameElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores and checks resource constraints and unit costs, validating purchase
 * requests
 * 
 * @author radithya
 *
 */
public class Bank {

	private Map<String, Double> resourceEndowments = new HashMap<>();
	private Map<String, Map<String, Double>> unitCosts = new HashMap<>();

	public Bank fromBank() {
		Bank bankCopy = new Bank();
		bankCopy.setResourceEndowments(getResourceEndowments());
		bankCopy.setUnitCosts(getUnitCosts());
		return bankCopy;
	}
	
	public void setResourceEndowment(String resourceName, double newResourceEndowment) {
		resourceEndowments.put(resourceName, newResourceEndowment);
	}
	
	public void setResourceEndowments(Map<String, Double> resourceEndowments) {
		this.resourceEndowments = new HashMap<>(resourceEndowments);
	}

	public void setUnitCost(String unitName, Map<String, Double> costsForUnitName) {
		unitCosts.put(unitName, costsForUnitName);
	}

	public void resetResourceEndowment(String resourceName, Double newQuantity) {
		resourceEndowments.put(resourceName, newQuantity);
	}

	/**
	 * Increment the amount of the named resource by the specified quantity, for use
	 * by power-ups, new-wave-bonus, etc.
	 * 
	 * @param resourceName
	 *            name of resource to grant
	 * @param newQuantity
	 *            amount of resource to grant
	 */
	public void grantResource(String resourceName, Double newQuantity) {
		resourceEndowments.put(resourceName,
				resourceEndowments.containsKey(resourceName) ? resourceEndowments.get(resourceName) + newQuantity
						: newQuantity);
	}

	public Map<String, Double> getResourceEndowments() {
		return resourceEndowments;
	}

	public Map<String, Map<String, Double>> getUnitCosts() {
		return unitCosts;
	}
	
	public Map<String, Double> getCostsForUnit(String unitName) {
		return unitCosts.get(unitName);
	}

	/**
	 * Purchase the given quantity of the unit if it can be afforded, update bank
	 * account and return true, else return false
	 * 
	 * @param unitName
	 *            name of the unit to purchase
	 * @param quantity
	 *            number of instances of that unit to purchase
	 * @return true if unit could be afforded and was successfully purchased, false
	 *         otherwise
	 */
	public boolean purchase(String unitName, int quantity) {
		Map<String, Double> resourcesAfterPurchase = new HashMap<>(resourceEndowments);
		Map<String, Double> costsForUnit = getCostsForUnit(unitName);
		for (String resourceName : costsForUnit.keySet()) {
			double newQuantity = resourcesAfterPurchase.get(resourceName)
					- costsForUnit.getOrDefault(resourceName, 0.0) * quantity;
			if (newQuantity <= 0) {
				return false;
			}
			resourcesAfterPurchase.put(resourceName, newQuantity);
		}
		resourceEndowments = resourcesAfterPurchase;
		return true;
	}

	/**
	 * Calculate and update resource endowment additions and point additions from destroyed game elements.
	 *
	 * @return the aggregate point value associated with the destroyed elements
	 */
	public double processPointsAndResourcesFromDeadElements(List<GameElement> deadElements) {
		double points = 0;
		for (GameElement gameElement : deadElements) {
			String elementName = gameElement.getTemplateName();
			gainResourcesFromElement(elementName);
			points += getPointsValue(elementName);
		}
		return points;
	}

	/**
	 * Process the addition of resources associated with a particular game element (for example, when it's sold).
	 *
	 * @param elementName
	 */
	public void gainResourcesFromElement(String elementName) {
		Map<String, Double> elementCosts = unitCosts.getOrDefault(elementName, new HashMap<>());
		for (String resourceName : resourceEndowments.keySet()) {
			double currentEndowment = resourceEndowments.get(resourceName);
			currentEndowment += elementCosts.getOrDefault(resourceName, 0.0);
			resourceEndowments.put(resourceName, currentEndowment);
		}
	}

	/**
	 * Get the points value associated with (the destruction of) a particular element.
	 *
	 * @param elementName the template name of the element being queried
	 * @return the point cost associated with the element
	 */
	public double getPointsValue(String elementName) {
		return unitCosts.getOrDefault(elementName, new HashMap<>())
				.values()
				.stream()
				.mapToDouble(Double::doubleValue)
				.sum();
	}
	
	private void setUnitCosts(Map<String, Map<String, Double>> unitCosts) {
		this.unitCosts = new HashMap<>(unitCosts);
	}
}
