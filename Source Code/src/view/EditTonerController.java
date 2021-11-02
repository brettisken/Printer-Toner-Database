package view;

import java.util.ArrayList;

import interfaces.EditTableController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.MainApp;
import model.Printer;
import model.Toner;

public class EditTonerController implements EditTableController {
	
	@FXML
	private TextField printerModelField;
	@FXML
	private TextField brandField;
	@FXML
	private TextField modelField;
	@FXML
	private TextField minStockField;
	@FXML
	private TextField curStockField;
	
	@FXML
    private TableView<Printer> printerTable;
	@FXML
	private TableColumn<Printer, String> descriptionColumn;
	@FXML
	private TableColumn<Printer, String> serialNumberColumn;
	@FXML
	private TableColumn<Printer, String> locationColumn;
	@FXML
	private TableColumn<Printer, String> campusColumn;
	
	private MainApp mainApp;
	private Stage dialogStage;
	private Toner toner;
	private boolean okClicked;
	private boolean deleteClicked;
	
	private ObservableList<Printer> printerList= FXCollections.observableArrayList();
	
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    	descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
    	serialNumberColumn.setCellValueFactory(cellData -> cellData.getValue().serialNumberProperty());
    	locationColumn.setCellValueFactory(cellData -> cellData.getValue().locationProperty());
    	campusColumn.setCellValueFactory(cellData -> cellData.getValue().campusProperty());
    	printerTable.setItems(printerList);
    }

    
    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
	
	
	
	/**
     * Sets the person to be edited in the dialog.
     * 
     * @param printer
     */
    public void setToner(Toner toner) {
        this.toner = toner;
        printerModelField.setText(this.toner.getPrinterModel());
        brandField.setText(this.toner.getBrand());
        modelField.setText(this.toner.getModel());
        minStockField.setText(Integer.toString(this.toner.getMinStock()));
        curStockField.setText(Integer.toString(this.toner.getCurStock()));
        printerList.setAll(printerUIDtoPrinterObj(toner.getLinkedPrinters()));
    }

    private ArrayList<Printer> printerUIDtoPrinterObj(ArrayList<String> linkedPrinters) {
    	ArrayList<Printer> tempPrinterList = new ArrayList<Printer>();
    	for (String uid : linkedPrinters) {
    		for (Printer printer : mainApp.getDatabase().getPrinterSet()) {
    			if (printer.getUid() == uid) tempPrinterList.add(printer);
    		}
    	}
    	return tempPrinterList;
    }
    
    private ArrayList<String> printerObjtoPrinterUID(ArrayList<Printer> selectedPrinters) {
    	ArrayList<String> linkedPrinters = new ArrayList<String>();
    	for (Printer printer : selectedPrinters) {
    		linkedPrinters.add(printer.getUid());
    	}
    	return linkedPrinters;
    }
	
    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
        	toner.setPrinterModel(printerModelField.getText());
            toner.setBrand(brandField.getText());
            toner.setModel(modelField.getText());
            toner.setMinStock(Integer.parseInt(minStockField.getText()));
            toner.setCurStock(Integer.parseInt(curStockField.getText()));
            
        	okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleDelete() {
    	if (RootLayoutController.isContinueAlert("Are you sure you want to delete this toner?")) {
    		deleteClicked = true;
            dialogStage.close();
		}
    	
    }
    
    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleSetPrinter() {
    	try {
    		//sets the current printer's tonerModels to be checked when TonerSelectorController opens the listview
    		printerList.forEach(printer->printer.setSelected(true));
    		//opens toner selection window and assigns the checked toners to an arraylist
    	   	ArrayList<Printer> selectedPrinters = new ArrayList<Printer>(mainApp.showPrinterSelector());
    	   	//sets the selected toners to the listview on the editprinter window
    		printerList.setAll(selectedPrinters);
    		//sets the printer being currently edited to the newly selected toners
    		toner.setLinkedPrinters(printerObjtoPrinterUID(selectedPrinters));
    	} catch (java.lang.NullPointerException e) {
    		//cancel button is clicked or window is closed
    	}
    }
    
	public Toner getToner() {
		return toner;
	}

	public boolean isOkClicked() {
		return okClicked;
	}
	
	public boolean isDeleteClicked() {
		return deleteClicked;
	}
	
    /**
     * Validates the user input in the text fields.
     * 
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";


        if (modelField.getText() == null || modelField.getText().length() == 0) {
            errorMessage += "No valid model!\n"; 
        }
        if (minStockField.getText() == null || minStockField.getText().length() == 0) {
            errorMessage += "No valid min stock!\n"; 
        } else {
            // try to parse the postal code into an int.
            try {
                Integer.parseInt(minStockField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid min stock (must be an integer)!\n"; 
            }
        }
        
        if (curStockField.getText() == null || curStockField.getText().length() == 0) {
            errorMessage += "No valid current stock!\n"; 
        } else {
            // try to parse the postal code into an int.
            try {
                Integer.parseInt(curStockField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid current stock (must be an integer)!\n"; 
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            
            alert.showAndWait();
            
            return false;
        }
    }
	
    /**
     * Is called by the main application to give a reference back to itself.
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
}
