package utng.gtid232.adla.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static ConexionBD instancia;
    private static final String URL = "jdbc:postgresql://localhost:5432/prestamosdb";
    private static final String USUARIO = "admin";
    private static final String PASSWORD = "Dele5457";
    private Connection conexion;

    private ConexionBD() throws SQLException {
        this.conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    public static synchronized ConexionBD getInstancia() throws SQLException {
        if (instancia == null || instancia.conexion == null || instancia.conexion.isClosed()) {
            instancia = new ConexionBD();
        }
        return instancia;
    }

    public Connection getConexion() {
        return conexion;
    }

    public static void cerrar() {
        try {
            if (instancia != null && instancia.conexion != null && !instancia.conexion.isClosed()) {
                instancia.conexion.close();
                System.out.println("Conexión cerrada exitosamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}