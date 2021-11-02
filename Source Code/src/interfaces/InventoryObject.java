package interfaces;

import javafx.beans.property.BooleanProperty;

public interface InventoryObject {
	
	//get unique identifier string
	public String getUid();
	//set uid, used for marshaling
	public void setUid(String uid);
	
	//selected properties used for the selector contollers 
	//gets set to true if user clicks the check box
	//after controller is closed, values are set back to false.
	public BooleanProperty selectedProperty();
	public boolean isSelected();
	public void setSelected(boolean selected);
	
	
}
