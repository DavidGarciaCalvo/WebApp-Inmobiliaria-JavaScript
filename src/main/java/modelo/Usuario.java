/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

public class Usuario {
    private String email;
    private String contrasena;
    private String nombre;
    private String imagenUsuario;

    public Usuario() {
    }

    public Usuario(String email, String contrasena, String nombre, String imagenUsuario) {
        this.email = email;
        this.contrasena = contrasena;
        this.nombre = nombre;
        this.imagenUsuario = imagenUsuario;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getImagenUsuario() { return imagenUsuario; }
    public void setImagenUsuario(String imagenUsuario) { this.imagenUsuario = imagenUsuario; }
}
