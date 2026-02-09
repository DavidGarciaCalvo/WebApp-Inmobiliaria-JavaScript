/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.Date;

public class Solicitud {
    
    // Atributos (Iguales a las columnas de la Base de Datos)
    private int idSolicitud; // ESTE ES EL QUE TE FALTABA
    private int codHabi;
    private String emailInquilino;
    private Date fechaInicioPosibleAlquiler;
    private Date fechaFinPosibleAlquiler;
    private String estado; // "pendiente", "aceptada", "rechazada"
       // Atributo nuevo para mostrar la foto en la lista de solicitudes
    private String imagenHabitacion; 

    public String getImagenHabitacion() { return imagenHabitacion; }
    public void setImagenHabitacion(String imagenHabitacion) { this.imagenHabitacion = imagenHabitacion; }

    private String tituloHabitacion; // Para guardar la dirección/ciudad y mostrarla

    public String getTituloHabitacion() {
        return tituloHabitacion;
    }

    public void setTituloHabitacion(String tituloHabitacion) {
        this.tituloHabitacion = tituloHabitacion;
    }
    // Constructor vacío
    public Solicitud() {
    }

    // --- GETTERS Y SETTERS ---

    public int getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public int getCodHabi() {
        return codHabi;
    }

    public void setCodHabi(int codHabi) {
        this.codHabi = codHabi;
    }

    public String getEmailInquilino() {
        return emailInquilino;
    }

    public void setEmailInquilino(String emailInquilino) {
        this.emailInquilino = emailInquilino;
    }

    public Date getFechaInicioPosibleAlquiler() {
        return fechaInicioPosibleAlquiler;
    }

    public void setFechaInicioPosibleAlquiler(Date fechaInicioPosibleAlquiler) {
        this.fechaInicioPosibleAlquiler = fechaInicioPosibleAlquiler;
    }

    public Date getFechaFinPosibleAlquiler() {
        return fechaFinPosibleAlquiler;
    }

    public void setFechaFinPosibleAlquiler(Date fechaFinPosibleAlquiler) {
        this.fechaFinPosibleAlquiler = fechaFinPosibleAlquiler;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}