package view;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import main.MainApp;
import model.Toner;

public class TonerOverviewController {
	
	@FXML
    private TableView<Toner> tonerTable;
	@FXML
    private TextField filterField;
    @FXML
    private TableColumn<Toner, String> printerModelColumn;
    @FXML
    private TableColumn<Toner, String> brandColumn;
    @FXML
    private TableColumn<Toner, String> modelColumn;
    @FXML
    private TableColumn<Toner, String> printersColumn;
    @FXML
    private TableColumn<Toner, Number> minStockColumn;
    @FXML
    private TableColumn<Toner, Number> curStockColumn;
    @FXML
    private TableColumn<Toner, Boolean> orderColumn;
    @FXML
    private TableColumn<Toner, Number> neededColumn;
    
    // Reference to the main application.
    private MainApp mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public TonerOverviewController() {
    	
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the printer table with the two columns.
    	printerModelColumn.setCellValueFactory(cellData -> cellData.getValue().printerModelProperty());
    	brandColumn.setCellValueFactory(cellData -> cellData.getValue().brandProperty());
    	modelColumn.setCellValueFactory(cellData -> cellData.getValue().modelProperty());
    	printersColumn.setCellValueFactory(cellData -> cellData.getValue().printersProperty());
    	minStockColumn.setCellValueFactory(cellData -> cellData.getValue().minStockProperty());
    	curStockColumn.setCellValueFactory(cellData -> cellData.getValue().curStockProperty());
    	orderColumn.setCellValueFactory(cellData -> cellData.getValue().orderProperty());
    	neededColumn.setCellValueFactory(cellData -> cellData.getValue().neededProperty());
    	
    	tonerTable.setRowFactory(tv -> {
            TableRow<Toner> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Toner tonerClicked = row.getItem();
                    mainApp.showTonerEditDialog(tonerClicked);
                }
            });
            return row ;
        });
 
    }
    
    /*
     * Method is Initilized in setmain()
     * Code reference 
     * https://code.makery.ch/blog/javafx-8-tableview-sorting-filtering/
     * Makes a Filtered List with printerData
     * Then adds the JAVAFX listener and if it sees changes
     * It filters the list
     */
    @FXML	
    private void filterTonerTable() {
    	FilteredList<Toner> filteredData = new FilteredList<>(mainApp.getTonerList(), p -> true);
         
         // 2. Set the filter Predicate whenever the filter changes.
         filterField.textProperty().addListener((observable, oldValue, newValue) -> {
             filteredData.setPredicate(toner -> {
                 // If filter text is empty, display all persons.
                 if (newValue == null || newValue.isEmpty()) {
                     return true;
                 }
                 
                 // Compare first name and last name of every printer with filter text.
                 String lowerCaseFilter = newValue.toLowerCase();
                 
                 if (toner.getBrand().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                     return true; // Filter matches Campus
                 } else if (Integer.toString(toner.getCurStock()).indexOf(lowerCaseFilter) != -1) {
                     return true; // Filter matches Category
                 } else if (Integer.toString(toner.getMinStock()).indexOf(lowerCaseFilter) != -1) {
                     return true; // Filter matches Manufacturer
                 } else if (toner.getModel().indexOf(lowerCaseFilter) != -1) {
                     return true; // Filter matches Location
                 } else if (Integer.toString(toner.getNeeded()).indexOf(lowerCaseFilter) != -1) {
                     return true; // Filter matches Barcode
                 } else if (Boolean.toString(toner.getOrder()).indexOf(lowerCaseFilter) != -1) {
                     return true; // Filter matches description
                 } else if (toner.getPrinterModel().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                     return true; // Filter matches Serial Number
                 }  else if (toner.getPrinters().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                     return true; // Filter matches Division
                 }
                 return false; // Does not match.
             });
         });
         
         // 3. Wrap the FilteredList in a SortedList. 
         SortedList<Toner> sortedData = new SortedList<>(filteredData);
         
         // 4. Bind the SortedList comparator to the TableView comparator.
         sortedData.comparatorProperty().bind(tonerTable.comparatorProperty());
         
         // 5. Add sorted (and filtered) data to the table.
         tonerTable.setItems(sortedData);
     }


    /**
     * Is called by the main application to give a reference back to itself.
     * ****This is where filterPrinter is ran....***
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
       	filterTonerTable();
    }
}

