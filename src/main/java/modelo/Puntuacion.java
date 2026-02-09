/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

public class Puntuacion {
    private int codHabi;
    private String emailInquilino;
    private int puntos; // De 0 a 5

    public Puntuacion() {
    }

    public Puntuacion(int codHabi, String emailInquilino, int puntos) {
        this.codHabi = codHabi;
        this.emailInquilino = emailInquilino;
        this.puntos = puntos;
    }

    public int getCodHabi() { return codHabi; }
    public void setCodHabi(int codHabi) { this.codHabi = codHabi; }

    public String getEmailInquilino() { return emailInquilino; }
    public void setEmailInquilino(String emailInquilino) { this.emailInquilino = emailInquilino; }

    public int getPuntos() { return puntos; }
    public void setPuntos(int puntos) { this.puntos = puntos; }
}