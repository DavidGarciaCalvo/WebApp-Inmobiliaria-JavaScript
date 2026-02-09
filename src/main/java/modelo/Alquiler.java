/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.Date;

public class Alquiler {
    
    // Atributos según Requisito 
    private int idAlquiler; // Autonumérico
    private int codHabi;
    private String emailInquilino;
    private Date fechaInicioAlqui;
    private Date fechaFinAlqui;

    public Alquiler() {
    }

    public Alquiler(int codHabi, String emailInquilino, Date fechaInicioAlqui, Date fechaFinAlqui) {
        this.codHabi = codHabi;
        this.emailInquilino = emailInquilino;
        this.fechaInicioAlqui = fechaInicioAlqui;
        this.fechaFinAlqui = fechaFinAlqui;
    }

    // GETTERS Y SETTERS
    public int getIdAlquiler() { return idAlquiler; }
    public void setIdAlquiler(int idAlquiler) { this.idAlquiler = idAlquiler; }

    public int getCodHabi() { return codHabi; }
    public void setCodHabi(int codHabi) { this.codHabi = codHabi; }

    public String getEmailInquilino() { return emailInquilino; }
    public void setEmailInquilino(String emailInquilino) { this.emailInquilino = emailInquilino; }

    public Date getFechaInicioAlqui() { return fechaInicioAlqui; }
    public void setFechaInicioAlqui(Date fechaInicioAlqui) { this.fechaInicioAlqui = fechaInicioAlqui; }

    public Date getFechaFinAlqui() { return fechaFinAlqui; }
    public void setFechaFinAlqui(Date fechaFinAlqui) { this.fechaFinAlqui = fechaFinAlqui; }
    
    // ... (después de los atributos que ya tenías)
    
    // ATRIBUTO EXTRA (Para poder mostrar datos de la casa en el JSP)
    private Habitacion habitacion; 

    // GETTER Y SETTER
    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }
    // --- CAMPOS EXTRA PARA MOSTRAR INFO DE LA CASA (NO TOCAR BASE DE DATOS) ---
    private String imagenHabitacion;
    private String tituloHabitacion;

    public String getImagenHabitacion() { return imagenHabitacion; }
    public void setImagenHabitacion(String imagenHabitacion) { this.imagenHabitacion = imagenHabitacion; }

    public String getTituloHabitacion() { return tituloHabitacion; }
    public void setTituloHabitacion(String tituloHabitacion) { this.tituloHabitacion = tituloHabitacion; }
}