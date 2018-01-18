package authoring.levelEditor;

public class Resource {
	private String resourceType;
	private Double amount;
	
	public Resource(String type, double double1) {
		setResourceType(type);
		setAmount(double1);
	}
	
	
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double double1) {
		this.amount = double1;
	}
	
	
	
}
