package interfaces;

import javafx.stage.Stage;
import main.MainApp;

public interface EditTableController {

	 /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage);
    
	public boolean isOkClicked();
	
	public boolean isDeleteClicked();

    /**
     * Is called by the main application to give a reference back to itself.
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp);
}
