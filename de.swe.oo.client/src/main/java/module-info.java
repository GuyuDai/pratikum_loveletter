module de.swe.oo.client {
    exports de.swe.oo.client.debugClient;
    exports de.swe.oo.client.connection;
    exports de.swe.oo.client;

    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
  requires de.swe.oo.server;
  opens de.swe.oo.client.guiclient;
}