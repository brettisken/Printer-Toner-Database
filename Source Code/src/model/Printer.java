package model;

import java.util.ArrayList;
import java.util.UUID;
import javax.xml.bind.annotation.XmlElement;

import interfaces.InventoryObject;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Printer implements InventoryObject {
	
	
	private StringProperty barCode;
	private StringProperty description;
	private StringProperty category;
	private StringProperty location;
	private StringProperty manufacturer;
	private StringProperty division;
	private StringProperty department;
	private StringProperty campus;
	private StringProperty serialNumber;
	private StringProperty status;
	private ArrayList<String> linkedToners;
	private String printerNotes;
	
	private final BooleanProperty selected = new SimpleBooleanProperty();
	
	/**
	 * Unique ID for adding and removing from the set. As well as
	 * Linking pritners and toners together.
	 */
	private String uid;
	
	/**
	 * Default constructor is needed for JAXB
	 * marshaling and manually adding new printer.
	 * New UUID generated each time.
	 * SimpleStringPrperties initialized with empty string
	 * to dodge null pointer errors
	 */
	public Printer() {
		linkedToners = new ArrayList<String>();
		this.barCode = new SimpleStringProperty("");
		this.description = new SimpleStringProperty("");
		this.category = new SimpleStringProperty("");
		this.location = new SimpleStringProperty("");
		this.serialNumber = new SimpleStringProperty("");
		this.manufacturer = new SimpleStringProperty("");
		this.division = new SimpleStringProperty("");
		this.department = new SimpleStringProperty("");
		this.campus = new SimpleStringProperty("");
		this.status = new SimpleStringProperty("");
		uid = UUID.randomUUID().toString();
	}

	/**
	 * Takes string of comma separated values
	 * Split each element of the string into an array
	 * Iterate over and create SimpleString for each value
	 * Input validation is handled in database Class
	 * @param printerString
	 */
	public Printer(String printerString) {
		linkedToners = new ArrayList<String>();
		String[] attributeArray = printerString.split(",");
		int i = 0;
		this.barCode = new SimpleStringProperty(attributeArray[i++]);
		this.description = new SimpleStringProperty(attributeArray[i++]);
		this.category = new SimpleStringProperty(attributeArray[i++]);
		this.location = new SimpleStringProperty(attributeArray[i++]);
		this.serialNumber = new SimpleStringProperty(attributeArray[i++]);
		this.manufacturer = new SimpleStringProperty(attributeArray[i++]);
		this.division = new SimpleStringProperty(attributeArray[i++]);
		this.department = new SimpleStringProperty(attributeArray[i++]);
		this.campus = new SimpleStringProperty(attributeArray[i++]);
		this.status = new SimpleStringProperty(attributeArray[i++]);
		uid = UUID.randomUUID().toString();
	}
	
	/*
	 * The following methods return the
	 * StringProperty object for JAVAFX
	 */

	public StringProperty barCodeProperty() {
		return barCode;
	}

	public StringProperty descriptionProperty() {
		return description;
	}

	public StringProperty categoryProperty() {
		return category;
	}

	public StringProperty serialNumberProperty() {
		return serialNumber;
	}
	
	public StringProperty locationProperty() {
		return location;
	}

	public StringProperty manufacturerProperty() {
		return manufacturer;
	}

	public StringProperty divisionProperty() {
		return division;
	}

	public StringProperty departmentProperty() {
		return department;
	}

	public StringProperty campusProperty() {
		return campus;
	}

	public StringProperty statusProperty() {
		return status;
	}
	
	/*
	 * The following getters have XML tag
	 * This tells marshaling to keep track 
	 * of these variables 
	 */
	
	@XmlElement
	public String getBarCode() {
		return barCode.get();
	}
	@XmlElement
	public String getDescription() {
		return description.get();
	}
	@XmlElement
	public String getCategory() {
		return category.get();
	}
	@XmlElement
	public String getSerialNumber() {
		return serialNumber.get();
	}@XmlElement
	public String getLocation() {
		return location.get();
	}
	@XmlElement
	public String getManufacturer() {
		return manufacturer.get();
	}
	@XmlElement
	public String getDivision() {
		return division.get();
	}
	@XmlElement
	public String getDepartment() {
		return department.get();
	}
	@XmlElement
	public String getCampus() {
		return campus.get();
	}
	@XmlElement
	public String getStatus() {
		return status.get();
	}
	@XmlElement
	public String getPrinterNotes() {
		return printerNotes;
	}
	
	public void setPrinterNotes(String printerNotes) {
		this.printerNotes=printerNotes;
	}
	/*
	 * Setters take string
	 * Created new SimpleString and assign to 
	 * Class attribute
	 * The unmarshaling process needs these 
	 * to re-create the objects
	 */

	public void setBarCode(String barCode) {
		this.barCode = new SimpleStringProperty(barCode);
	}


	public void setDescription(String description) {
		this.description = new SimpleStringProperty(description);
	}


	public void setCategory(String category) {
		this.category = new SimpleStringProperty(category);
	}


	public void setLocation(String location) {
		this.location = new SimpleStringProperty(location);
	}


	public void setManufacturer(String manufacturer) {
		this.manufacturer = new SimpleStringProperty(manufacturer);
	}


	public void setDivision(String division) {
		this.division = new SimpleStringProperty(division);
	}


	public void setDepartment(String department) {
		this.department = new SimpleStringProperty(department);
	}


	public void setCampus(String campus) {
		this.campus = new SimpleStringProperty(campus);
	}


	public void setSerialNumber(String serialNumber) {
		this.serialNumber = new SimpleStringProperty(serialNumber);
	}


	public void setStatus(String status) {
		this.status = new SimpleStringProperty(status);
	}
	
	@XmlElement
	public ArrayList<String> getLinkedToners() {
		return linkedToners;
	}

	public void setLinkedToners(ArrayList<String> linkedToners) {
		this.linkedToners = linkedToners;
	}
	@XmlElement
	public String getUid() {
		return uid;
	}
	
	public void setUid(String uid) {
		this.uid =  uid;
	}

    public BooleanProperty selectedProperty() {
        return selected ;
    }

    public final boolean isSelected() {
        return selectedProperty().get();
    }

    public final void setSelected(boolean selected) {
        selectedProperty().set(selected);
    }

	@Override
	public String toString() {
		return "Description: " + description.get() + "   SerialNumber: " + serialNumber.get() + "   Location:" + location.get() + "   Campus:" + campus.get();
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + uid.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Printer other = (Printer) obj;
		if (uid == other.getUid()) {
			return true;
		}
		return false;	
	}
	
	
	
	
}
