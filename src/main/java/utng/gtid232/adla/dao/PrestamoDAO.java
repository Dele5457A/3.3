package utng.gtid232.adla.dao;

import utng.gtid232.adla.conexion.ConexionBD;
import utng.gtid232.adla.modelo.Prestamo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {

    /* Consulta base de datos para traer todos los registros */
    public List<Prestamo> obtenerTodos() {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT id_prestamo, usuario_matricula, codigo_equipo, "
                   + "fecha_prestamo, fecha_dev_est, estado, material "
                   + "FROM prestamos ORDER BY id_prestamo DESC";
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            /* Recorre los registros devueltos fila por fila */
            while (rs.next()) {
                Prestamo p = new Prestamo();
                p.setIdPrestamo(rs.getInt("id_prestamo"));
                p.setUsuarioMatricu(rs.getString("usuario_matricula"));
                p.setCodigoEquipo(rs.getString("codigo_equipo"));
                /* Convierte las fechas de formato SQL al formato de Java */
                Date fPrestamo = rs.getDate("fecha_prestamo");
                if (fPrestamo != null) {
                    p.setFechaPrestamo(fPrestamo.toLocalDate());
                }
                Date fDevEst = rs.getDate("fecha_dev_est");
                if (fDevEst != null) {
                    p.setFechaDevEst(fDevEst.toLocalDate());
                }
                p.setEstado(rs.getString("estado"));
                p.setMaterial(rs.getString("material"));
                /* Agrega el préstamo completamente relleno a la lista */
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener préstamos: " + e.getMessage());
        }
        /* Devuelve la lista vacía o llena */
        return lista;
    }

    public boolean insertar(Prestamo prestamo) {
        /* Los signos '?' son comodines de seguridad para evitar ataques de Inyección SQL */
        String sql = "INSERT INTO prestamos (usuario_matricula, codigo_equipo, fecha_prestamo, fecha_dev_est, estado, material) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            /* Sustituye los signos '?' en orden estricto por las variables reales del objeto */
            ps.setString(1, prestamo.getUsuarioMatricu());
            ps.setString(2, prestamo.getCodigoEquipo());
            /* Date.valueOf convierte LocalDate a java.sql.Date */
            ps.setDate(3, prestamo.getFechaPrestamo() != null ? Date.valueOf(prestamo.getFechaPrestamo()) : null);
            ps.setDate(4, prestamo.getFechaDevEst() != null ? Date.valueOf(prestamo.getFechaDevEst()) : null);
            ps.setString(5, prestamo.getEstado());
            ps.setString(6, prestamo.getMaterial());
            /* Si se insertó con éxito, devuelve true */
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar préstamo: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Prestamo prestamo) {
        /* Sentencia SQL para modificar un registro existente */
        String sql = "UPDATE prestamos SET usuario_matricula = ?, codigo_equipo = ?, fecha_prestamo = ?, fecha_dev_est = ?, estado = ?, material = ? WHERE id_prestamo = ?";
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            /* Asigna los nuevos valores provenientes del formulario */
            ps.setString(1, prestamo.getUsuarioMatricu());
            ps.setString(2, prestamo.getCodigoEquipo());
            ps.setDate(3, prestamo.getFechaPrestamo() != null ? Date.valueOf(prestamo.getFechaPrestamo()) : null);
            ps.setDate(4, prestamo.getFechaDevEst() != null ? Date.valueOf(prestamo.getFechaDevEst()) : null);
            ps.setString(5, prestamo.getEstado());
            ps.setString(6, prestamo.getMaterial());
            /* El último parámetro es el WHERE */
            ps.setInt(7, prestamo.getIdPrestamo());
            /* Retorna true si encontró el ID y lo modificó */
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar préstamo: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idPrestamo) {
        /* Sentencia SQL para borrar mediante su ID único */
        String sql = "DELETE FROM prestamos WHERE id_prestamo = ?";
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            /* Asigna el ID que recibió como parámetro al signo '?' */
            ps.setInt(1, idPrestamo);
            /* Retorna true si borró el registro correctamente */
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar préstamo: " + e.getMessage());
            return false;
        }
    }
}