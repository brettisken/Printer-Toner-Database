package main;


import java.io.IOException;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Database;
import model.Printer;
import model.Toner;
import view.EditPrinterController;
import view.EditTonerController;
import view.PrinterOverviewController;
import view.PrinterSelectorController;
import view.RootLayoutController;
import view.TonerOverviewController;
import view.TonerSelectorController;
/**
 * 
 * Main java application class. Handles the observable lists of data used for
 * displaying toner and printer information in JAVAFX tables. Handles initializing all
 * the scenes and controllers.
 *
 */
public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    
    /**
     * String holds the path of which .fxml document has been loaded last
     * this is used for knowing if toner or printer table is loaded
     */
    private String currPane;
    
    /**
     * Observable list of printers
     */
    private ObservableList<Printer> printerList;
    
    /**
     * Observable list of toners
     */
    private ObservableList<Toner> tonerList;
    
    /**
     * Access to the database of printers and toners
     */
    private Database database;
    
    /**
     * MainApp Constructor
     * Initializes a PrinterList and tonerList to avoid 
     * Null pointer exceptions prior to adding data
     */
    public MainApp() {
    	database = new Database();
    	printerList = FXCollections.observableArrayList();
    	tonerList = FXCollections.observableArrayList();
    }
    
    /**
     * Returns the database of printers and toners. 
     * @return
     */
    public Database getDatabase() {
        return database;
    }
    
    /**
     * Returns the observable list of printers.
     * @return
     */
    public ObservableList<Printer> getPrinterList() {
		return printerList;
	}
    /**
     * Returns the observable list of toners. 
     * @return
     */
	public ObservableList<Toner> getTonerList() {
		return tonerList;
	}
	
    /**
     * clears the observable list and adds the data objects back in after changes
     * have been made to the database
     * @return
     */
	public void updateTonerList() {
		database.updateTonerStock();
		tonerList.setAll(database.getTonerSet());
	}
	
    /**
     * clears the observable list and adds the data objects back in after changes
     * have been made to the database
     * @return
     */
	public void updatePrinterList() {
		printerList.setAll(database.getPrinterSet());
	}
	
	/**
     * First thing JavaFX runs 
     * Initializes the root layout which is the title bar and 
     * window body. Then puts the printer table overview inside
     * the anchor pane window
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("PrinterApp");

        initRootLayout();

        showPrinterOverview();
    }

    /**
     * Initializes the root layout
     * Root layout is the main window 
     * And file bar
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);      
       
            // Give the controller access to the main app.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

      
    }

    /**
     * Shows the Printer overview inside the root layout.
     * Handles the printer table view
     */
    public void showPrinterOverview() {
        try {
            // Load printer overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/PrinterOverview.fxml"));
            AnchorPane printerOverView = (AnchorPane) loader.load();

            // Set printer overview into the center of root layout.
            rootLayout.setCenter(printerOverView);

            currPane ="/view/PrinterOverview.fxml";
            
            
            // Give the controller access to the main app.
            PrinterOverviewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows Toner overview inside the root layout.
     * Handles the toner table view
     */
    public void showTonerOverview() {
        try {
            // Load printer overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/TonerOverview.fxml"));
            AnchorPane tonerOverView = (AnchorPane) loader.load();

            // Set printer overview into the center of root layout.
            rootLayout.setCenter(tonerOverView);
            
            currPane ="/view/TonerOverview.fxml";
            
            // Give the controller access to the main app.
            TonerOverviewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * Opens a dialog to edit details for the specified printer. If the user
     * clicks OK, the changes are saved into the provided printer object and true
     * is returned.
     * 
     * @param printer the printer object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showPrinterEditDialog(Printer printer) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/EditPrinterWindow.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Printer");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the printer into the controller.
            EditPrinterController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainApp(this);
            controller.setPrinter(printer);

            
            
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            if (controller.isOkClicked()) {
            	database.getPrinterSet().remove(printer);
            	database.getPrinterSet().add(controller.getPrinter());
            	updatePrinterList();
            	return true;
            } else if (controller.isDeleteClicked()) {
            	database.getPrinterSet().remove(printer);
            	updatePrinterList();
            	return true;
            } else {
            	return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Initializes toner selector controller
     * Returns the list of toners selected by the user
     * 
     * @return List of toners
     */
    public List<Toner> showTonerSelector() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/TonerSelector.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Select Toners");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the toner into the controller.
            TonerSelectorController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setListView(tonerList);
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();   
            //After tonerSelector closes, resets all toner objects to not be checked
        	tonerList.forEach(toner->toner.setSelected(false));

            if (controller.isOkClicked()) {
            	return controller.getSelectedToners();
            } else {
            	return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Opens a dialog to edit details for the specified toner. If the user
     * clicks OK, the changes are saved into the provided toner object and true
     * is returned.
     * 
     * @param toner the toner object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showTonerEditDialog(Toner toner) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/EditTonerWindow.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Toner");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            EditTonerController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainApp(this);
            controller.setToner(toner);

            
            
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            if (controller.isOkClicked()) {
            	database.getTonerSet().remove(toner);
            	database.getTonerSet().add(controller.getToner());
            	updateTonerList();
            	return true;
            } else if (controller.isDeleteClicked()) {
            	database.getTonerSet().remove(toner);
            	updateTonerList();
            	return true;
            } else {
            	return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    /**
     * Initializes printer selector controller
     * Returns the list of printers selected by the user
     * 
     * @return List of printers
     */
    public List<Printer> showPrinterSelector() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/PrinterSelector.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Select Printers");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            PrinterSelectorController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setListView(printerList);
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();   
            //After tonerSelector closes, resets all toner objects to not be checked
            printerList.forEach(toner->toner.setSelected(false));

            if (controller.isOkClicked()) {
            	return controller.getSelectedPrinters();
            } else {
            	return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public BorderPane getRootLayout() {
		return rootLayout;
	}

	public static void main(String[] args) {
        launch(args);
    }

	public String getCurrPane() {
		return currPane;
	}


	
	
}