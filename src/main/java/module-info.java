module tn.ensit.miniprojetbibliotheque {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires jfoenix;
    requires boxable;
    requires pdfbox;
    requires gson;
    requires org.apache.commons.codec;
    requires org.apache.logging.log4j;
    requires org.apache.commons.lang3;

    opens tn.ensit.miniprojetbibliotheque to javafx.fxml;
    opens tn.ensit.miniprojetbibliotheque.models to javafx.base;
    exports tn.ensit.miniprojetbibliotheque;
}