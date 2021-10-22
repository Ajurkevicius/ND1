module com.example.minesweeper_javafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.minesweeper_javafx to javafx.fxml;
    exports com.example.minesweeper_javafx;
}