module isa.pu2crud {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens isa.pu2crud to javafx.fxml;
    exports isa.pu2crud;
}