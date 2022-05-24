module de.swe.oo.client {
    exports de.swe.oo.client.guiclient;
    exports de.swe.oo.client.connection;
    exports de.swe.oo.client;

    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.base;

  opens de.swe.oo.client.guiclient;
}