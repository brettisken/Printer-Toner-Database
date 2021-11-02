package view;


import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import main.MainApp;
import model.Printer;

public class PrinterOverviewController {
	
	@FXML
    private TableView<Printer> printerTable;
	@FXML
    private TextField filterField;
    @FXML
    private TableColumn<Printer, String> descriptionColumn;
    @FXML
    private TableColumn<Printer, String> categoryColumn;
    @FXML
    private TableColumn<Printer, String> manufacturerColumn;
    @FXML
    private TableColumn<Printer, String> locationColumn;
    @FXML
    private TableColumn<Printer, String> barCodeColumn;
    @FXML
    private TableColumn<Printer, String> divisionColumn;
    @FXML
    private TableColumn<Printer, String> departmentColumn;
    @FXML
    private TableColumn<Printer, String> serialNumberColumn;
    @FXML
    private TableColumn<Printer, String> statusColumn;
    @FXML
    private TableColumn<Printer, String> campusColumn;

    
    // Reference to the main application.
    private MainApp mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public PrinterOverviewController() {
    	
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the printer table with the two columns.
    	descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
    	categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
    	manufacturerColumn.setCellValueFactory(cellData -> cellData.getValue().manufacturerProperty());
    	locationColumn.setCellValueFactory(cellData -> cellData.getValue().locationProperty());
    	campusColumn.setCellValueFactory(cellData -> cellData.getValue().campusProperty());
    	barCodeColumn.setCellValueFactory(cellData -> cellData.getValue().barCodeProperty());
    	divisionColumn.setCellValueFactory(cellData -> cellData.getValue().divisionProperty());
    	departmentColumn.setCellValueFactory(cellData -> cellData.getValue().departmentProperty());
    	serialNumberColumn.setCellValueFactory(cellData -> cellData.getValue().serialNumberProperty());
    	statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
    	
    	//not sure if I want to use this or not
    	//printerTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
    	
    	printerTable.setRowFactory(tv -> {
            TableRow<Printer> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Printer printerClicked = row.getItem();
                    mainApp.showPrinterEditDialog(printerClicked);
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
    private void filterPrinterTable() {
    	FilteredList<Printer> filteredData = new FilteredList<>(mainApp.getPrinterList(), p -> true);
         
         // 2. Set the filter Predicate whenever the filter changes.
         filterField.textProperty().addListener((observable, oldValue, newValue) -> {
             filteredData.setPredicate(printer -> {
                 // If filter text is empty, display all persons.
                 if (newValue == null || newValue.isEmpty()) {
                     return true;
                 }
                 
                 // Compare first name and last name of every printer with filter text.
                 String lowerCaseFilter = newValue.toLowerCase();
                 
                 if (printer.getCampus().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                     return true; // Filter matches Campus
                 } else if (printer.getCategory().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                     return true; // Filter matches Category
                 } else if (printer.getManufacturer().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                     return true; // Filter matches Manufacturer
                 } else if (printer.getLocation().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                     return true; // Filter matches Location
                 } else if (printer.getBarCode().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                     return true; // Filter matches Barcode
                 } else if (printer.getDescription().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                     return true; // Filter matches description
                 } else if (printer.getSerialNumber().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                     return true; // Filter matches Serial Number
                 }  else if (printer.getDivision().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                     return true; // Filter matches Division
                 } else if (printer.getDepartment().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                     return true; // Filter matches department
                 } else if (printer.getStatus().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                     return true; // Filter matches status
                 }
                 return false; // Does not match.
             });
         });
         
         // 3. Wrap the FilteredList in a SortedList. 
         SortedList<Printer> sortedData = new SortedList<>(filteredData);
         
         // 4. Bind the SortedList comparator to the TableView comparator.
         sortedData.comparatorProperty().bind(printerTable.comparatorProperty());
         
         // 5. Add sorted (and filtered) data to the table.
         printerTable.setItems(sortedData);
     }


    /**
     * Is called by the main application to give a reference back to itself.
     * ****This is where filterPrinter is ran....***
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        filterPrinterTable();
    }
}