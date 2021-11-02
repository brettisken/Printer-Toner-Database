package view;


import java.io.File;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import main.MainApp;
import model.Printer;
import model.Toner;

/**
 * The controller for the root layout. The root layout provides the basic
 * application layout containing a menu bar and space where other JavaFX
 * elements can be placed.
 */
public class RootLayoutController {

    // Reference to the main application
    private MainApp mainApp;

    
    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handlePrinterView() {
    	mainApp.showPrinterOverview();
    }
    
    @FXML
    private void handleTonerView() {
    	mainApp.showTonerOverview();
    }
    
    /**
     * Opens a FileChooser to let the user select a CSV file to load.
     */
    @FXML
    private void handleImport() {
    		
    	FileChooser fileChooser = new FileChooser();
    	FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                 "CSV files (*.csv)", "*.csv");
         fileChooser.getExtensionFilters().add(extFilter);
         File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
         if (file != null) {
            if (mainApp.getCurrPane().equals("/view/PrinterOverview.fxml")) {
            	 mainApp.getDatabase().importPrinterData(file);
            	 mainApp.updatePrinterList();
            } else if (mainApp.getCurrPane().equals("/view/TonerOverview.fxml")) {
            	mainApp.getDatabase().importTonerData(file);
            	mainApp.updateTonerList();
            }
         }
    }
    
    /**
     * Opens a FileChooser to let the user select a JAXB file to load.
     */
    @FXML
    private void handleOpen() {
    	
    	//Check if there is already printer data loaded 
    	//Calls overWriteAlert with the message to display which makes a pop up window
    	//If user hits cancel, RETURNS and abots the open
    	if (!mainApp.getDatabase().getPrinterSet().isEmpty() && !isContinueAlert("Importing new Database will overwrite Progress")) {
    		return;
    	}
    	
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            mainApp.getDatabase().loadPrinterDataFromFile(file);
            mainApp.updatePrinterList();
            mainApp.updateTonerList();
        }
    }

    /**
     * Saves the file to the printer file that is currently open. If there is no
     * open file, the "save as" dialog is shown.
     */
    @FXML
    private void handleSave() {
        if (mainApp.getDatabase().getSaveFile() != null) {
        	mainApp.getDatabase().savePrinterDataToFile(mainApp.getDatabase().getSaveFile());
        } else {
            handleSaveAs();
        }
    }

     /**
     * Opens a FileChooser to let the user select a file to save to.
     */
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            mainApp.getDatabase().savePrinterDataToFile(file);
        }
    }

    @FXML
    private void handleAdd() {
    	if (mainApp.getCurrPane().equals("/view/PrinterOverview.fxml")) {
    		Printer newPrinter = new Printer();
            mainApp.showPrinterEditDialog(newPrinter);
       } else if (mainApp.getCurrPane().equals("/view/TonerOverview.fxml")) {
    	   Toner newToner = new Toner();
           mainApp.showTonerEditDialog(newToner);
       }   
    }
    
    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }
    
    
    /**
     * Closes the application.
     */
    @FXML
    private void handleDelete() {
    	if (mainApp.getCurrPane().equals("/view/PrinterOverview.fxml")) {
    		//mainApp.deletePrinter();
       } else if (mainApp.getCurrPane().equals("/view/TonerOverview.fxml")) {
    	   //mainApp.deleteToner();
       }
    	
    }
    
    /**
     * Pop up message to warn user
     * @param displayMessage
     * @return True if user hits OK
     */
    protected static boolean isContinueAlert(String displayMessage) {
    	ButtonType ok = new ButtonType("Yes", ButtonData.YES);
    	ButtonType cancel = new ButtonType("No", ButtonData.NO);
    	Alert alert = new Alert(AlertType.WARNING,"Fart",ok,cancel);
    	alert.setTitle("WARNING");
    	alert.setHeaderText(displayMessage);
    	alert.setContentText("Do you want to continue?");

    	Optional<ButtonType> result = alert.showAndWait();

    	if (result.get() == cancel) {
    		return false;
    	} else {
    		return true;
    	}
    }
}