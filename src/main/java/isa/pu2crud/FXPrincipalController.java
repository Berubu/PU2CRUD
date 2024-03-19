package isa.pu2crud;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
//hola
public class FXPrincipalController implements Initializable {
    public Button ImagButton;
    @FXML
    private TextField ClientTextField;

    @FXML
    private Spinner<Integer> CantSpinner;

    @FXML
    private TextField TotalTextField;

    @FXML
    private TextArea NoteTextArea;

    @FXML
    private DatePicker FechaDatePicker;

    @FXML
    private ImageView imagView;

    @FXML
    private TabPane MesTablePane;

    private static final Logger logger = LoggerFactory.getLogger(FXPrincipalController.class);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    public void guardarDatos() {
        String campo1 = ClientTextField.getText();
        int campo2 = CantSpinner.getValue();
        String campo3 = TotalTextField.getText();
        String campo4 = NoteTextArea.getText();
        LocalDate fecha = FechaDatePicker.getValue();

        if (validarDatos(campo1, campo2, campo3, campo4, fecha)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ClientName", campo1);
            jsonObject.put("SpinnerValue", campo2);
            jsonObject.put("TotalAmount", campo3);
            jsonObject.put("NotesAndSizes", campo4);
            jsonObject.put("Fecha", fecha.toString());

            SaveDatesInA(jsonObject);

            Tab mesTab = getMonth(fecha.getMonthValue());
            JSONArray registros = obRDesdeArchivo();
            if (registros != null) {
                registros.add(jsonObject);
                String formattedJson = FormLeible(registros);
                System.out.println(formattedJson);
            } else {
                registros = new JSONArray();
                registros.add(jsonObject);
            }
            mesTab.setContent(new Label(registros.toJSONString()));
            mesTab.setUserData(registros);
        } else {
            System.out.println("Error: Datos inválidos");
        }
    }
    private String FormLeible(JSONArray jsonArray) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonArray);
    }

    private boolean validarDatos(String campo1, int campo2, String campo3, String campo4, LocalDate fecha) {
        return campo1 != null && !campo1.isEmpty() &&
                campo2 > 0 &&
                campo3 != null && !campo3.isEmpty() &&
                campo4 != null && !campo4.isEmpty() &&
                fecha != null;
    }

    private Tab getMonth(int month) {
        String tabText = getMonthName(month);
        for (Tab tab : MesTablePane.getTabs()) {
            if (tab.getText().equals(tabText)) {
                return tab;
            }
        }
        Tab newTab = new Tab(tabText);
        MesTablePane.getTabs().add(newTab);
        return newTab;
    }

    private void SaveDatesInA(JSONObject jsonObject) {
        JSONArray jsonArray = obRDesdeArchivo();
        jsonArray.add(jsonObject);

        try (FileWriter file = new FileWriter("datos.json")) {
            file.write(jsonArray.toJSONString());
            System.out.println("Datos guardados correctamente en datos.json");
        } catch (IOException e) {
            logger.error("Error al guardar datos en archivo", e);
        }
    }

    private JSONArray obRDesdeArchivo() {
        JSONArray registros = new JSONArray();
        try (BufferedReader reader = new BufferedReader(new FileReader("datos.json"))) {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(reader);
            if (obj instanceof JSONArray) {
                registros = (JSONArray) obj;
            } else {
                logger.error("El archivo JSON no contiene un array de registros");
            }
        } catch (IOException e) {
            logger.error("Error al abrir el archivo JSON", e);
        } catch (org.json.simple.parser.ParseException e) {
            logger.error("Error al parsear el archivo JSON", e);
        }
        return registros;
    }

    private String getMonthName(int month) {
        return switch (month) {
            case 1 -> "Enero";
            case 2 -> "Febrero";
            case 3 -> "Marzo";
            case 4 -> "Abril";
            case 5 -> "Mayo";
            case 6 -> "Junio";
            case 7 -> "Julio";
            case 8 -> "Agosto";
            case 9 -> "Septiembre";
            case 10 -> "Octubre";
            case 11 -> "Noviembre";
            case 12 -> "Diciembre";
            default -> "";
        };
    }

    @FXML
    public void abrirDatos() {
        JSONArray registros = obRDesdeArchivo();
        if (registros != null) {
            for (Object o : registros) {
                if (o instanceof JSONObject jsonObject) {
                    String fechaRegistroStr = (String) jsonObject.get("Fecha");
                    LocalDate fechaRegistro = LocalDate.parse(fechaRegistroStr);

                    mostrarEnTab(jsonObject, fechaRegistro);
                } else {
                    logger.error("El objeto en el array no es un objeto JSON válido");
                }
            }
        } else {
            logger.error("No se encontraron registros en el archivo");
        }
    }



    //este muestra el contenido del tabePane y sus Tab de forma ordenada
    private void mostrarEnTab(JSONObject jsonObject, LocalDate fechaRegistro) {
        Tab mesTab = getMonth(fechaRegistro.getMonthValue());

        VBox registroBox = new VBox();
        registroBox.setSpacing(10);

        Label clienteLabel = new Label("Cliente:");
        TextField clienteTextField = new TextField((String) jsonObject.get("ClientName"));
        clienteTextField.setEditable(false);

        Label cantidadLabel = new Label("Cantidad:");
        Spinner<Integer> cantidadSpinner = new Spinner<>(1, 100, Integer.parseInt(jsonObject.get("SpinnerValue").toString()));
        cantidadSpinner.setEditable(false);

        Label totalLabel = new Label("Total:");
        TextField totalTextField = new TextField((String) jsonObject.get("TotalAmount"));
        totalTextField.setEditable(false);

        Label notasLabel = new Label("Notas y Tallas:");
        TextArea notasTextArea = new TextArea((String) jsonObject.get("NotesAndSizes"));
        notasTextArea.setEditable(false);

        Label fechaLabel = new Label("Fecha: " + fechaRegistro);

        // llamada a hfxml
        registroBox.getChildren().addAll(clienteLabel, clienteTextField, cantidadLabel, cantidadSpinner,
                totalLabel, totalTextField, notasLabel, notasTextArea, fechaLabel);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(registroBox);

        // Verificar si esta el contenido en el Tab
        if (mesTab.getContent() != null) {
            VBox existingContent = (VBox) mesTab.getContent();
            existingContent.getChildren().add(scrollPane);
        } else {
            VBox newContent = new VBox(scrollPane);
            mesTab.setContent(newContent);
        }
    }

    @FXML
    public void borrarDatos() {
        try {
            FileWriter fileWriter = new FileWriter("datos.json");
            fileWriter.write("[]"); // Vaciar el archivo
            fileWriter.close();
            logger.info("Datos borrados correctamente de datos.json");
        } catch (IOException e) {
            logger.error("Error al borrar datos de datos.json", e);
        }
    }

    @FXML
    public void showStage() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXPrincipal.fxml"));
            Parent root = loader.load();
            stage.setTitle("Ventana Principal");
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (IOException e) {
            logger.error("Error al cargar la ventana principal", e);
        }
    }

    @FXML
    public void agregarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                Image image = new Image(selectedFile.toURI().toString());
                imagView.setImage(image);
                imagView.setFitWidth(200);
                imagView.setFitHeight(200);

                // Convierte la imagen a formato BufferedImage
                javafx.scene.image.Image fxImage = imagView.getImage();
                java.awt.image.BufferedImage bImage = SwingFXUtils.fromFXImage(fxImage, null);

                // Guardar la imagen en el archivo de datos.json---- Falta llamar a la imagen en le table
                ImageIO.write(bImage, "png", new File("imagen.png"));
                System.out.println("Imagen guardada correctamente en imagen.png");
            } catch (IOException e) {
                logger.error("Error al guardar la imagen", e);
            }
        }
    }
}
