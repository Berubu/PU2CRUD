package isa.pu2crud;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private FXPrincipalController fxPrincipalController;

    public void setFxPrincipalController(FXPrincipalController fxPrincipalController) {
        this.fxPrincipalController = fxPrincipalController;
    }

    public void login() {
        if (fxPrincipalController == null) {
            System.out.println("Error: FXPrincipalController no está inicializado");
            return;
        }

        String username = usernameField.getText();
        String password = passwordField.getText();

        if (isValidCredentials(username, password)) {
            System.out.println("Inicio de sesión exitoso");
            fxPrincipalController.showStage(); // Llamar al método showStage en el cotrolador principal
        } else {
            System.out.println("Nombre de usuario o contraseña incorrectos");
        }
    }

    private boolean isValidCredentials(String username, String password) {
        // Validación de credenciales
        return username.equals("isa") && password.equals("isa");
    }
}
