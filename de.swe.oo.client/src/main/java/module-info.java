module de.swe.oo.client {
    exports de.swe.oo.client.minimalClient;
    exports de.swe.oo.client.connection;
    exports de.swe.oo.client;

    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    opens de.swe.oo.client.guiclient;
}