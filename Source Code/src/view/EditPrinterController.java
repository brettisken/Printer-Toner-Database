package view;

import java.util.ArrayList;

import interfaces.EditTableController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import main.MainApp;
import model.Printer;
import model.Toner;

public class EditPrinterController implements EditTableController {
	
	@FXML
	private TextField descriptionField;
	@FXML
	private TextField categoryField;
	@FXML
	private TextField manufacturerField;
	@FXML
	private TextField locationField;
	@FXML
	private TextField barCodeField;
	@FXML
	private TextField divisionField;
	@FXML
	private TextField departmentField;
	@FXML
	private TextField serialNumberField;
	@FXML
	private TextField statusField;
	@FXML
	private TextField campusField;
	
	@FXML
	private TextArea printerNotes;
	
	@FXML
    private TableView<Toner> tonerTable;
	@FXML
	private TableColumn<Toner, Number> stockColumn;
	@FXML
	private TableColumn<Toner, String> printerModelColumn;
	
	private MainApp mainApp;
	private Stage dialogStage;
	private Printer printer;
	private boolean okClicked;
	private boolean deleteClicked;
	
	private ObservableList<Toner> tonerList= FXCollections.observableArrayList();
	
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    	stockColumn.setCellValueFactory(cellData -> cellData.getValue().curStockProperty());
    	printerModelColumn.setCellValueFactory(cellData -> cellData.getValue().printerModelProperty());
    	tonerTable.setItems(tonerList);
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
    public void setPrinter(Printer printer) {
        this.printer = printer;
        descriptionField.setText(this.printer.getDescription());
        categoryField.setText(this.printer.getCategory());
        manufacturerField.setText(this.printer.getManufacturer());
        locationField.setText(this.printer.getLocation());
        barCodeField.setText(this.printer.getBarCode());
        divisionField.setText(this.printer.getDivision());
        departmentField.setText(this.printer.getDepartment());
        serialNumberField.setText(this.printer.getSerialNumber());
        statusField.setText(this.printer.getStatus());
        campusField.setText(this.printer.getCampus());
        printerNotes.setText(this.printer.getPrinterNotes());
        tonerList.setAll(tonerUIDtoTonerObj(printer.getLinkedToners()));
    }

    private ArrayList<Toner> tonerUIDtoTonerObj(ArrayList<String> linkedToners) {
    	ArrayList<Toner> tempTonerList = new ArrayList<Toner>();
    	for (String uid : linkedToners) {
    		for (Toner toner : mainApp.getDatabase().getTonerSet()) {
    			if (toner.getUid() == uid) tempTonerList.add(toner);
    		}
    	}
    	return tempTonerList;
    }
    
    private ArrayList<String> tonerObjtoTonerUID(ArrayList<Toner> selectedToners) {
    	ArrayList<String> linkedToners = new ArrayList<String>();
    	for (Toner toner : selectedToners) {
    		linkedToners.add(toner.getUid());
    	}
    	return linkedToners;
    }
	
    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
        	printer.setDescription(descriptionField.getText());
            printer.setCategory(categoryField.getText());
            printer.setManufacturer(manufacturerField.getText());
            printer.setLocation(locationField.getText());
            printer.setBarCode(barCodeField.getText());
            printer.setDivision(divisionField.getText());
            printer.setDepartment(departmentField.getText());
            printer.setSerialNumber(serialNumberField.getText());
            printer.setStatus(statusField.getText());
            printer.setCampus(campusField.getText());
            printer.setPrinterNotes(printerNotes.getText());
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
    	if (RootLayoutController.isContinueAlert("Are you sure you want to delete this printer?")) {
    		deleteClicked = true;
            dialogStage.close();
		}
    	
    }
    
    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleSetToner() {
    	try {
    		//sets the current printer's tonerModels to be checked when TonerSelectorController opens the listview
    		tonerList.forEach(toner->toner.setSelected(true));
    		//opens toner selection window and assigns the checked toners to an arraylist
    	   	ArrayList<Toner> selectedToners = new ArrayList<Toner>(mainApp.showTonerSelector());
    	   	//sets the selected toners to the listview on the editprinter window
    		tonerList.setAll(selectedToners);
    		//sets the printer being currently edited to the newly selected toners
    		printer.setLinkedToners(tonerObjtoTonerUID(selectedToners));
    	} catch (java.lang.NullPointerException e) {
    		//cancel button is clicked or window is closed
    	}
    }
    
	public Printer getPrinter() {
		return printer;
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


        if (serialNumberField.getText() == null || serialNumberField.getText().length() == 0) {
            errorMessage += "No valid Serial Number!\n"; 
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