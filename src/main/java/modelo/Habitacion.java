/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

public class Habitacion {
    private int codHabi;
    private String ciudad;
    private String direccion;
    private String emailPropietario;
    private String imagenHabitacion;
    private double latitudH;
    private double longitudH;
    private int precioMes;

    public Habitacion() {
    }

    public Habitacion(int codHabi, String ciudad, String direccion, String emailPropietario, String imagenHabitacion, double latitudH, double longitudH, int precioMes) {
        this.codHabi = codHabi;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.emailPropietario = emailPropietario;
        this.imagenHabitacion = imagenHabitacion;
        this.latitudH = latitudH;
        this.longitudH = longitudH;
        this.precioMes = precioMes;
    }

    public int getCodHabi() { return codHabi; }
    public void setCodHabi(int codHabi) { this.codHabi = codHabi; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getEmailPropietario() { return emailPropietario; }
    public void setEmailPropietario(String emailPropietario) { this.emailPropietario = emailPropietario; }

    public String getImagenHabitacion() { return imagenHabitacion; }
    public void setImagenHabitacion(String imagenHabitacion) { this.imagenHabitacion = imagenHabitacion; }

    public double getLatitudH() { return latitudH; }
    public void setLatitudH(double latitudH) { this.latitudH = latitudH; }

    public double getLongitudH() { return longitudH; }
    public void setLongitudH(double longitudH) { this.longitudH = longitudH; }

    public int getPrecioMes() { return precioMes; }
    public void setPrecioMes(int precioMes) { this.precioMes = precioMes; }
}