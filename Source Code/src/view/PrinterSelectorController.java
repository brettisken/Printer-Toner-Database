package view;

import java.util.List;

import interfaces.SelectorController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.stage.Stage;
import model.Printer;

public class PrinterSelectorController implements SelectorController {

	@FXML
	private ListView<Printer> printerListView;
	
	private List<Printer> selectedPrinters;
	
	private Stage dialogStage;
	private boolean okClicked;
	
	
	public PrinterSelectorController() {
		printerListView = new ListView<Printer>();
	}
	
	/**
	 * Assigns observable list of toners to listview
	 * Adds check box to each toner in the list view
	 * @param tonerList
	 */
	public void setListView(ObservableList<Printer> printerList) {
		printerListView.setItems(printerList);
		//Uses the toner boolean attribute 'selected' for the checkbox mapping
		printerListView.setCellFactory(CheckBoxListCell.forListView(Printer::selectedProperty));
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
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
    	selectedPrinters = printerListView.getItems().filtered(Printer::isSelected);
    	okClicked = true;
        dialogStage.close();
    }
    
    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
    
	public boolean isOkClicked() {
		return okClicked;
	}

	public List<Printer> getSelectedPrinters() {
		return selectedPrinters;
	}
}
