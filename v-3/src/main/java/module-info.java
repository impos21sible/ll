module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires telegrambots.meta;
    requires telegrambots;


    opens org.example.demo to javafx.fxml;
    exports org.example.demo;
}