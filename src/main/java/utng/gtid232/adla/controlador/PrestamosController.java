package utng.gtid232.adla.controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import utng.gtid232.adla.dao.PrestamoDAO;
import utng.gtid232.adla.modelo.Prestamo;
import java.time.LocalDate;
import java.util.List;

public class PrestamosController {

    @FXML private TextField txtUsuario;
    @FXML private TextField txtEquipoPrestado;
    @FXML private TextField txtEquipoPrestado1;
    @FXML private DatePicker dpFechaPrestamo;
    @FXML private DatePicker dpFechaDevEst;
    @FXML private ChoiceBox<String> cbEstado;
    @FXML private TableView<Prestamo> tblPrestamos;
    @FXML private TableColumn<Prestamo, String> colUsuario;
    @FXML private TableColumn<Prestamo, String> colMaterial;
    @FXML private TableColumn<Prestamo, LocalDate> colFechaPrestamo;
    @FXML private TableColumn<Prestamo, LocalDate> colFechaDev;
    @FXML private TableColumn<Prestamo, String> colEstado;
    @FXML private TableColumn<Prestamo, String> colCodigo;

    /*Crea un objeto DAO (Data Access Object) que contiene las consultas SQL */
    private final PrestamoDAO prestamoDAO = new PrestamoDAO();

    /*Es un tipo especial de lista de JavaFX. Se reflejan los cambios en tiempo real */
    private final ObservableList<Prestamo> listaPrestamos = FXCollections.observableArrayList();

    /*Una variable temporal para guardar el préstamo que el usuario sirve para guardar temporales */
    private Prestamo prestamoSeleccionado;

    @FXML
    public void initialize() {
        /*Enlazan cada columna de la tabla con los atributos (getters/setters) de tu clase modelo 'prestamo' */
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuarioMatricu"));
        colMaterial.setCellValueFactory(new PropertyValueFactory<>("material"));
        colFechaPrestamo.setCellValueFactory(new PropertyValueFactory<>("fechaPrestamo"));
        colFechaDev.setCellValueFactory(new PropertyValueFactory<>("fechaDevEst"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoEquipo"));

        /*Configura las opciones del menú desplegable de estados */
        cbEstado.getItems().clear();
        cbEstado.getItems().addAll("Activo", "Devuelto", "Mantenimiento");
        cbEstado.setValue("Activo");

        /*Detecta cuando haces clic en una fila de la tabla */
        tblPrestamos.getSelectionModel().selectedItemProperty().addListener(
            (obs, viejo, nuevo) -> {
                prestamoSeleccionado = nuevo;
                if (nuevo != null) {
                    cargarCamposDesdeSeleccion();
                }
            }
        );

        /*Llama al método para traer la información desde la base de datos */
        cargarPrestamos();
    }

    private void cargarPrestamos() {
        /*Vacía la lista actual */
        listaPrestamos.clear();
        /*Va a la base de datos */
        List<Prestamo> deBD = prestamoDAO.obtenerTodos();
        /*Los mete a la lista observable */
        listaPrestamos.addAll(deBD);
        /*Pinta la lista en la tabla visual */
        tblPrestamos.setItems(listaPrestamos);
    }

    private void cargarCamposDesdeSeleccion() {
        /*Pone los textos del préstamo seleccionado en las cajas de texto */
        txtUsuario.setText(prestamoSeleccionado.getUsuarioMatricu());
        txtEquipoPrestado.setText(prestamoSeleccionado.getCodigoEquipo());
        txtEquipoPrestado1.setText(prestamoSeleccionado.getMaterial());
        /*Asigna las fechas */
        if (dpFechaPrestamo != null)
            dpFechaPrestamo.setValue(prestamoSeleccionado.getFechaPrestamo());
        if (dpFechaDevEst != null)
            dpFechaDevEst.setValue(prestamoSeleccionado.getFechaDevEst());
        /*Asigna el estado al ChoiceBox */
        cbEstado.setValue(prestamoSeleccionado.getEstado());
    }

    @FXML
    private void registrarPrestamo() {
        /*Verifica que no haya campos vacíos */
        if (validarCampos()) {
            /*Crea un objeto préstamo vacío */
            Prestamo p = new Prestamo();
            p.setUsuarioMatricu(txtUsuario.getText());
            p.setCodigoEquipo(txtEquipoPrestado.getText());
            p.setMaterial(txtEquipoPrestado1.getText());
            /*Sin fecha puesta el sistema pone automaticamente la de ese dia */
            p.setFechaPrestamo(dpFechaPrestamo != null && dpFechaPrestamo.getValue() != null ? dpFechaPrestamo.getValue() : LocalDate.now());
            /*Sin fecha de devolucion puesta asigna 3 por defecto */
            p.setFechaDevEst(dpFechaDevEst != null && dpFechaDevEst.getValue() != null ? dpFechaDevEst.getValue() : LocalDate.now().plusDays(3));
            p.setEstado(cbEstado.getValue());
            /*Intenta guardarlo usando el DAO */
            if (prestamoDAO.insertar(p)) {
                mostrarAlerta("Éxito", "Préstamo registrado correctamente", Alert.AlertType.INFORMATION);
                /*Borra el formulario */
                limpiarFormulario();
                /*Recarga la tabla para ver el nuevo registro */
                cargarPrestamos();
            } else {
                mostrarAlerta("Error", "No se pudo guardar en la base de datos.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void editarPrestamo() {
        /*Alerta si no se ha elegido nada en la tabla */
        if (prestamoSeleccionado == null) {
            mostrarAlerta("Aviso", "Selecciona primero un préstamo de la tabla.", Alert.AlertType.WARNING);
            return;
        }
        /*Modifica el objeto seleccionado con los nuevos datos escritos */
        if (validarCampos()) {
            prestamoSeleccionado.setUsuarioMatricu(txtUsuario.getText());
            prestamoSeleccionado.setCodigoEquipo(txtEquipoPrestado.getText());
            prestamoSeleccionado.setMaterial(txtEquipoPrestado1.getText());
            if (dpFechaPrestamo != null)
                prestamoSeleccionado.setFechaPrestamo(dpFechaPrestamo.getValue());
            if (dpFechaDevEst != null)
                prestamoSeleccionado.setFechaDevEst(dpFechaDevEst.getValue());
            prestamoSeleccionado.setEstado(cbEstado.getValue());
            /*Intenta actualizar el registro en la BD usando su ID */
            if (prestamoDAO.actualizar(prestamoSeleccionado)) {
                mostrarAlerta("Éxito", "Préstamo editado correctamente", Alert.AlertType.INFORMATION);
                limpiarFormulario();
                cargarPrestamos();
            } else {
                mostrarAlerta("Error", "No se pudo actualizar el registro.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void eliminarPrestamo() {
        /*Alerta si no hay selección */
        if (prestamoSeleccionado == null) {
            mostrarAlerta("Aviso", "Selecciona primero un préstamo de la tabla.", Alert.AlertType.WARNING);
            return;
        }
        /*Crea una ventana de confirmación con botones SĨ NO */
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, "¿Estás seguro de que deseas eliminar este préstamo?", ButtonType.YES, ButtonType.NO);
        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                /*Intenta eliminar de la BD mandando el ID */
                if (prestamoDAO.eliminar(prestamoSeleccionado.getIdPrestamo())) {
                    mostrarAlerta("Éxito", "Préstamo eliminado de la base de datos.", Alert.AlertType.INFORMATION);
                    limpiarFormulario();
                    cargarPrestamos();
                } else {
                    mostrarAlerta("Error", "No se pudo eliminar el registro.", Alert.AlertType.ERROR);
                }
            }
        });
    }

    private boolean validarCampos() {
        /*Comprueba si alguna de las cajas de texto está vacía o solo tiene espacios en blanco */
        if (txtUsuario.getText().trim().isEmpty() ||
            txtEquipoPrestado.getText().trim().isEmpty() ||
            txtEquipoPrestado1.getText().trim().isEmpty() ||
            cbEstado.getValue() == null) {
            mostrarAlerta("Campos vacíos", "Por favor completa todos los campos para proceder.", Alert.AlertType.WARNING);
            return false;
        }
        /*Retorna verdadero si todo está bien relleno */
        return true;
    }

    private void limpiarFormulario() {
        /*Borra los textos, limpia las fechas, restablece el ChoiceBox y deselecciona la tabla */
        txtUsuario.clear();
        txtEquipoPrestado.clear();
        txtEquipoPrestado1.clear();
        if (dpFechaPrestamo != null) dpFechaPrestamo.setValue(null);
        if (dpFechaDevEst != null) dpFechaDevEst.setValue(null);
        cbEstado.setValue("Activo");
        prestamoSeleccionado = null;
        tblPrestamos.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        /*Crea y muestra una ventana emergente (Pop-up) con mensajes informativos, de advertencia o de error */
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}