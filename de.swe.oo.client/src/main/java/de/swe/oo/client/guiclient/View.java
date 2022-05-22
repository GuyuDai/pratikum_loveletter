package de.swe.oo.client.guiclient;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * This is an example of how to create a view solely with java code (<b>not recommended</b>).
 */
public class View extends VBox {
    public View() {
        // creating node elements
        Label label = new Label("Created with Java class");
        ListView<String> list = new ListView<>();
        TextField input = new TextField();
        input.setPromptText("Enter text here");
        Button button = new Button();
        button.setText("ADD");   //What does this actually do? The actual Text is changed in game.fxml

        // adding node elements to container
        getChildren().addAll(label, list, input, button);

        // creating controller
        ViewModel viewModel = new ViewModel();
        viewModel.setNodeElements(this, list, input, button);
        viewModel.initialize();

        // setting button pressed event
        button.setOnAction(viewModel::handleButtonPress);
    }
}
