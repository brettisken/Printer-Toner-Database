package model;

import java.util.ArrayList;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;

import interfaces.InventoryObject;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Toner implements InventoryObject {

	private StringProperty printerModel;
	private StringProperty brand;
	private StringProperty model;
	private StringProperty printers;
	private IntegerProperty minStock;
	private IntegerProperty curStock;
	private BooleanProperty order;
	private IntegerProperty needed;
	private final BooleanProperty selected = new SimpleBooleanProperty();
	private ArrayList<String> linkedPrinters;
	
	/**
	 * Unique ID for adding and removing from the set
	 */
	private String uid;
	
	/**
	 * Default constructor is needed for JAXB
	 * Marshaling
	 */
	public Toner() {
		linkedPrinters = new ArrayList<String>();
		this.printerModel = new SimpleStringProperty("");
		this.brand = new SimpleStringProperty("");
		this.model = new SimpleStringProperty("");
		this.printers = new SimpleStringProperty("");
		this.minStock = new SimpleIntegerProperty(0);
		this.curStock = new SimpleIntegerProperty(0);
		this.order = new SimpleBooleanProperty(false);
		this.needed = new SimpleIntegerProperty(0);
		this.uid = UUID.randomUUID().toString();
	}

	/**
	 * Takes string of comma separated values
	 * Split each element of the string into an array
	 * Iterate over and create SimpleString for each value
	 * Input validation is handled in PrinterList Class
	 * @param tonerString
	 */
	public Toner(String tonerString) {
		linkedPrinters = new ArrayList<String>();
		String[] attributeArray = tonerString.split(",");
		int i = 0;
		this.printerModel = new SimpleStringProperty(attributeArray[i++]);
		this.brand = new SimpleStringProperty(attributeArray[i++]);
		this.model = new SimpleStringProperty(attributeArray[i++]);
		this.printers = new SimpleStringProperty(attributeArray[i++]);
		this.minStock = new SimpleIntegerProperty(Integer.parseInt(attributeArray[i++]));
		this.curStock = new SimpleIntegerProperty(Integer.parseInt(attributeArray[i++]));
		this.order =new SimpleBooleanProperty(Boolean.parseBoolean(attributeArray[i++]));
		this.needed = new SimpleIntegerProperty(Integer.parseInt(attributeArray[i++]));
		this.uid = UUID.randomUUID().toString();
	}

	
	/*
	 * The following methods return the
	 * StringProperty object for JAVAFX
	 */
	
	public StringProperty printerModelProperty() {
		return printerModel;
	}

	public StringProperty brandProperty() {
		return brand;
	}

	public StringProperty modelProperty() {
		return model;
	}

	public StringProperty printersProperty() {
		return printers;
	}

	public IntegerProperty minStockProperty() {
		return minStock;
	}

	public IntegerProperty curStockProperty() {
		return curStock;
	}

	public BooleanProperty orderProperty() {
		return order;
	}

	public IntegerProperty neededProperty() {
		return needed;
	}

	
	/*
	 * The following getters have XML tag
	 * This tells marshaling to keep track 
	 * of these variables 
	 */
	
	@XmlElement
	public String getPrinterModel() {
		return printerModel.get();
	}
	@XmlElement
	public String getBrand() {
		return brand.get();
	}
	@XmlElement
	public String getModel() {
		return model.get();
	}
	@XmlElement
	public String getPrinters() {
		return printers.get();
	}
	@XmlElement
	public int getMinStock() {
		return minStock.get();
	}
	@XmlElement
	public int getCurStock() {
		return curStock.get();
	}
	@XmlElement
	public boolean getOrder() {
		return order.get();
	}
	@XmlElement
	public int getNeeded() {
		return needed.get();
	}
	@XmlElement
	public String getUid() {
		return this.uid;
	}
	
	
	/*
	 * Setters take string
	 * Created new SimpleString and assign to 
	 * Class attribute
	 * The unmarshaling process needs these 
	 * to re-create the objects
	 */
	
	public void setPrinterModel(String printerModel) {
		this.printerModel =  new SimpleStringProperty(printerModel);
	}

	public void setBrand(String brand) {
		this.brand =  new SimpleStringProperty(brand);
	}

	public void setModel(String model) {
		this.model =  new SimpleStringProperty(model);
	}

	public void setPrinters(String printers) {
		this.printers =  new SimpleStringProperty(printers);
	}

	public void setMinStock(int minStock) {
		this.minStock =  new SimpleIntegerProperty(minStock);
	}

	public void setCurStock(int curStock) {
		this.curStock =  new SimpleIntegerProperty(curStock);
	}

	public void setOrder(boolean order) {
		this.order =  new SimpleBooleanProperty(order);
	}

	public void setNeeded(int needed) {
		this.needed =  new SimpleIntegerProperty(needed);
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
	
    @XmlElement
	public ArrayList<String> getLinkedPrinters() {
		return linkedPrinters;
	}

	public void setLinkedPrinters(ArrayList<String> linkedPrinters) {
		this.linkedPrinters = linkedPrinters;
	}

	@Override
	public String toString() {
		return "Name: " + printerModel.get() + "   Brand: " + brand.get() + "   Model:" + model.get();
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
		Toner other = (Toner) obj;
		if (uid == other.getUid()) {
			return true;
		}
		return false;	
	}


}
