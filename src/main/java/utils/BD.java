/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BD {
    // 1. Variable estática privada: LA ÚNICA INSTANCIA
    private static BD instanciaUnica;
    
    // Variable para la conexión real
    private Connection con;
    
    // Datos de conexión
    private final String URL = "jdbc:mysql://localhost:3306/vitobadi16?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private final String USER = "root"; 
    private final String PASS = "root";

    // 2. Constructor PRIVADO (Nadie puede hacer new BD())
    private BD() {
        try {
            // Cargar el driver moderno
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("--- CONEXIÓN SINGLETON ESTABLECIDA ---");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Error grave al conectar BD: " + e.getMessage());
        }
    }

    // 3. Método estático público para dar acceso a la instancia
    public static BD getInstancia() {
        if (instanciaUnica == null) {
            instanciaUnica = new BD();
        }
        return instanciaUnica;
    }

    // Getter para la conexión
    public Connection getConexion() {
        return con;
    }
    
    // Cierre seguro (Opcional pero recomendado)
    public void cerrar() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}