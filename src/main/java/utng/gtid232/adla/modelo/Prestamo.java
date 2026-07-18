
package utng.gtid232.adla.modelo;

import java.time.LocalDate;

public class Prestamo {
    private int idPrestamo;
    private String usuarioMatricu;
    private String codigoEquipo;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevEst;
    private String estado;
    private String material;

    public Prestamo() {}

    public Prestamo(int idPrestamo, String usuarioMatricu, String codigoEquipo,
                    LocalDate fechaPrestamo, LocalDate fechaDevEst, String estado, String material) {
        this.idPrestamo = idPrestamo;
        this.usuarioMatricu = usuarioMatricu;
        this.codigoEquipo = codigoEquipo;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevEst = fechaDevEst;
        this.estado = estado;
        this.material = material;
    }

    public int getIdPrestamo() { return idPrestamo; }
    public void setIdPrestamo(int idPrestamo) { this.idPrestamo = idPrestamo; }

    public String getUsuarioMatricu() { return usuarioMatricu; }
    public void setUsuarioMatricu(String usuarioMatricu) { this.usuarioMatricu = usuarioMatricu; }

    public String getCodigoEquipo() { return codigoEquipo; }
    public void setCodigoEquipo(String codigoEquipo) { this.codigoEquipo = codigoEquipo; }

    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(LocalDate fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }

    public LocalDate getFechaDevEst() { return fechaDevEst; }
    public void setFechaDevEst(LocalDate fechaDevEst) { this.fechaDevEst = fechaDevEst; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }
}

