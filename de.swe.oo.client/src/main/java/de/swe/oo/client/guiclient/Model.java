package de.swe.oo.client.guiclient;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Holds the data of the application and manages all the business logic
 */
public class Model {

    private static volatile Model instance;

    private Model(){
    }

    /**
     * Creates, if not already created, and returns an instance of {@link Model} using the Singleton-Pattern.
     *
     * @see <a href="https://en.wikipedia.org/wiki/Singleton_pattern#Lazy_initialization">Singleton Wikipedia</a>
     *
     * @return the {@link Model} instance
     */
    public static Model getInstance() {
        if (instance == null) {
            synchronized (Model.class) {
                if (instance == null) {
                    instance = new Model();
                }
            }
        }
        return instance;
    }

    /**
     * Holds the list of strings we want to show on the screen.
     */
    private final ObservableList<String> listContent = FXCollections.observableArrayList();

    public ObservableList<String> getListContentProperty() {
        return listContent;
    }

    /**
     * This property holds the user's current input.
     */
    private final StringProperty textFieldContent = new SimpleStringProperty("");

    public StringProperty getTextFieldContent(){
        return textFieldContent;
    }

    /**
     * Adds a string as a new item to the list.
     * @param listItem the string to add
     */
    public void addNewListItem(String listItem) {
        listContent.add(listItem);
    }
}
