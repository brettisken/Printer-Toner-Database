package interfaces;


import javafx.stage.Stage;


public interface SelectorController {

    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage);
    
    public boolean isOkClicked();
}
