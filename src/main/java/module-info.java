module com.codetravel.core {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires org.xerial.sqlitejdbc;
    requires org.slf4j;
    
    opens com.codetravel.core to javafx.fxml;
    opens com.codetravel.ui to javafx.fxml;
    opens com.codetravel.entities to javafx.fxml;
    opens com.codetravel.combat to javafx.fxml;
    opens com.codetravel.levels to javafx.fxml;
    opens com.codetravel.database to javafx.fxml;
    
    exports com.codetravel.core;
    exports com.codetravel.ui;
    exports com.codetravel.entities;
    exports com.codetravel.combat;
    exports com.codetravel.levels;
    exports com.codetravel.database;
}