package view;

import java.util.List;

import interfaces.SelectorController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.stage.Stage;
import model.Toner;

public class TonerSelectorController implements SelectorController {

	@FXML
	private ListView<Toner> tonerListView;
	
	private List<Toner> selectedToners;
	
	private Stage dialogStage;
	private boolean okClicked;
	
	
	public TonerSelectorController() {
		tonerListView = new ListView<Toner>();
	}
	
	/**
	 * Assigns observable list of toners to listview
	 * Adds check box to each toner in the list view
	 * @param tonerList
	 */
	public void setListView(ObservableList<Toner> tonerList) {
		tonerListView.setItems(tonerList);
		//Uses the toner boolean attribute 'selected' for the checkbox mapping
		tonerListView.setCellFactory(CheckBoxListCell.forListView(Toner::selectedProperty));
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
    	selectedToners = tonerListView.getItems().filtered(Toner::isSelected);
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

	public List<Toner> getSelectedToners() {
		return selectedToners;
	}
}
