package model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


/**
 * Handles the printer and toner collections. Stores the objects in a hashset.
 * Both printers and toners use a UID and one unique attribute to create the hash. 
 * Importing CSV files
 * Saving and loading .xml files through JAXB
 *
 */
@XmlRootElement(name = "Database")
public class Database {
	
	/**
	 * Hashset of all printers stored in database
	 */
	private HashSet<Printer> printerSet = new HashSet<>();
	/**
	 * Hashset of all toners stored in database
	 */
    private HashSet<Toner> tonerSet = new HashSet<>();
	/**
	 * Int used to validate printer import. The number is how
	 * many values the CSV string should contain. Not necessarily how many
	 * attributes the printer class has.
	 */
	private final static int numOfPrinterAttributes = 10;
	/**
	 * Int used to validate toner import. The number is how
	 * many values the CSV string should contain. Not necessarily how many
	 * attributes the toner class has.
	 */
	private static final int numOfTonerAttributes = 8;
	
    /**
     * Current save location
     */
    private File saveFile;
    
    
    @XmlElement(name = "printer", type = Printer.class)
    public HashSet<Printer> getPrinterSet() {
        return printerSet;
    }

    @XmlElement(name = "toner", type = Toner.class)
    public HashSet<Toner> getTonerSet() {
        return tonerSet;
    }
    
    
	public Database() {
		super();
		this.printerSet = new HashSet<Printer>();
		this.tonerSet = new HashSet<Toner>();
	}

	/**
     * Returns the current save file
     * @return saveFile
     */
	public File getSaveFile() {
		return saveFile;
	}

	/**
	 * Loads Printer data from the specified file. The current Printer data will
	 * be replaced.
	 * @param file
	 */
	public void loadPrinterDataFromFile(File file) {
	    try {
	        JAXBContext context = JAXBContext
	                .newInstance(Database.class);
	        Unmarshaller um = context.createUnmarshaller();

	        // Reading XML from the file and unmarshalling.
	        Database data = (Database) um.unmarshal(file);
	        
	        printerSet.clear();
	        tonerSet.clear();
	        printerSet = data.getPrinterSet();
	        tonerSet = data.getTonerSet();
	        
	        // Save the file path to the registry.
	       saveFile = file;

	    } catch (Exception e) { 
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Could not load data");
	        alert.setContentText("Could not load data from file:\n" + file.getPath());
	        e.printStackTrace();
	        alert.showAndWait();
	    }
	}

	/**
	 * Saves the current printer data to the specified file.
	 * 
	 * @param file
	 */
	public void savePrinterDataToFile(File file) {
	    try {
	        JAXBContext context = JAXBContext
	                .newInstance(Database.class);
	        Marshaller m = context.createMarshaller();
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

	        //Marshaling current instance of database and saving XML to the file.
	        m.marshal(this, file);
	        
	        // Save the file path to the registry.
	        saveFile = file;
	    } catch (Exception e) { // catches ANY exception
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Could not save data");
	        alert.setContentText("Could not save data to file:\n" + file.getPath());
	        e.printStackTrace();
	        alert.showAndWait();
	    }
	}
	
	
	/**
	 * reads CSV file of toners. Each line of the CSV is put in a set. Then
	 * each line of the CSV is used to create toner objects. Toner objects are
	 * then stored in the toner hash set
	 * @param file
	 */
	public void importTonerData(File file) {
    	try {
    		//Create Set of strings, each element is a string for making printer objects
    		//Opens the file passed in as a stream of each line. Skip first line. Filter each line. IF validatePrinterString() fails
    		//discard line. At the end add the stream to a set
    		Set<String> tonerSet = Files.lines(file.toPath()).skip(1).filter(line -> validateTonerString(line)).collect(Collectors.toSet());
			//put the Set into a stream.for each element create a new printer and add to printerGroup
			tonerSet.stream().forEach(line -> this.tonerSet.add(new Toner(line)));
		} catch (java.lang.NullPointerException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
		    alert.setHeaderText("Invalid Input");
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.ERROR);
		    alert.setTitle("Error");
		    alert.setHeaderText("Could not load data");
		    alert.setContentText("Could not load data from file:\n" + file.getPath());
		}  	
	}
	
	/**
	 * Validates tonerString from CSV file contains proper data
	 * @param printerString
	 * @return
	 */
	private static boolean validateTonerString(String tonerString) {
		//make scanner on printer string
		String[] attributeArray = tonerString.split(",");
		int i = 0;
		if (attributeArray.length < numOfTonerAttributes) return false;
		try {
			i++; //printerModel
			i++; //brand
			if (attributeArray[i++].equals("")) throw new Exception(); //model. cant be null
			i++; //printers
			Integer.parseInt(attributeArray[i++]); //minStock. must be int
			Integer.parseInt(attributeArray[i++]); //CurStock. must be int
			Boolean.parseBoolean(attributeArray[i++]); //order. must be bool
			Integer.parseInt(attributeArray[i++]); //needed. must be int.
			return true;
		} catch (Exception e) {
			System.out.println("failed importing a toner");
			return false;
		}
	}
	
	
	/**
	 * reads CSV file of printers. Each line of the CSV is put in a set. Then
	 * each line of the CSV is used to create printer objects. Printer objects are
	 * then stored in the printer hash set
	 * @param file
	 */
	public void importPrinterData(File file) {

    	try {
    		//Create Set of strings, each element is a string for making printer objects
    		//Opens the file passed in as a stream of each line. Skip first line. Filter each line. IF validatePrinterString() fails
    		//discard line. At the end add the stream to a set
    		Set<String> printerSet = Files.lines(file.toPath()).skip(1).filter(line -> validatePrinterString(line)).collect(Collectors.toSet());
			//put the Set into a stream.for each element create a new printer and add to printerGroup
			printerSet.stream().forEach(line -> this.printerSet.add(new Printer(line)));
		} catch (java.lang.NullPointerException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
		    alert.setHeaderText("Invalid Input");
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.ERROR);
		    alert.setTitle("Error");
		    alert.setHeaderText("Could not load data");
		    alert.setContentText("Could not load data from file:\n" + file.getPath());
		}  	
	}
    
	public void updateTonerStock() {
		for (Toner toner : tonerSet) {
			int needed = toner.getMinStock()-toner.getCurStock();
			if (needed>0) {
				toner.setNeeded(needed);
				toner.setOrder(true);
			} else {
				toner.setNeeded(0);
				toner.setOrder(false);
			}
		}
	}
	

	/**
	 * Validates printerString from CSV file contains proper data
	 * @param printerString
	 * @return
	 */
	private static boolean validatePrinterString(String printerString) {
		String[] attributeArray = printerString.split(",");
		int i = 0;
		if (attributeArray.length < numOfPrinterAttributes) return false;
		try {
			i++; //barcode
			i++; //description
			i++; //category
			i++; //location
			if (attributeArray[i++].equals("")) throw new Exception(); //serialnumber. it cant be null
			i++; //manufacturer
			i++; //division
			i++; //department
			i++; //campus
			i++; //status
			return true;
		} catch (Exception e) {
			System.out.println("failed importing a printer");
			return false;
		}
	}
	
}
