package de.swe.oo.client.guiclient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

/**
 * Binds the data coming from the {@link Model} with its respective GUI-elements and manages the presentation logic
 */
public class ViewModel {

    @FXML
    public VBox container;
    @FXML
    private ListView<String> list;
    @FXML
    private TextField input;
    @FXML
    private Button button;

    private final Model model;

    public ViewModel() {
        model = Model.getInstance();
    }

    /**
     * This method is called automatically if the FXML-Loader loads a scene (state this class as the controller
     * in the respective fxml-file) and must be called manually when using views constructed
     * with java code (e.g. {@link View#View()})
     */
    public void initialize() {
        // Bindings
        list.itemsProperty().set(model.getListContentProperty());
        input.textProperty().bindBidirectional(model.getTextFieldContent());
        button.disableProperty().bind(input.textProperty().isEmpty());
    }

    /**
     * Sets the node elements manually.
     * This function is only used if the view is generated in java code by {@link View}.
     */
    public void setNodeElements(VBox container, ListView<String> list, TextField input, Button btn) {
        this.container = container;
        this.list = list;
        this.input = input;
        this.button = btn;
    }

    /**
     * This function is called if the button is pressed.
     */
    @FXML
    public void handleButtonPress(ActionEvent actionEvent) {
        model.addNewListItem(model.getTextFieldContent().get());
        model.getTextFieldContent().set("");
        input.requestFocus();
    }

    @FXML
    public void handleKeyPressed(KeyEvent keyEvent){
        if (keyEvent.getCode() == KeyCode.ENTER) {
            handleButtonPress(null);
        }
    }
}
