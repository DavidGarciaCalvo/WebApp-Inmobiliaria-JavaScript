/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import utils.BD;

public class PuntuacionDAO {

    // 1. Guardar una puntuación
    public static boolean insertarPuntuacion(Puntuacion p) {
        boolean exito = false;
        
        // SEGURIDAD EXTRA: Antes de insertar, verificamos si tiene derecho
        // (Alquiler terminado y NO ha votado ya)
        if (!tienePermisoParaVotar(p.getCodHabi(), p.getEmailInquilino())) {
            System.out.println("ERROR: Intento de voto fraudulento (Alquiler no finalizado o ya votado).");
            return false;
        }

        try {
            Connection con = BD.getInstancia().getConexion();
            String sql = "INSERT INTO puntuacion (codHabi, emailInquilino, puntos) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, p.getCodHabi());
            ps.setString(2, p.getEmailInquilino());
            ps.setInt(3, p.getPuntos());
            
            int filas = ps.executeUpdate();
            if (filas > 0) exito = true;
            
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exito;
    }

    // 2. Comprobar si YA ha votado
    public static boolean yaHaVotado(int codHabi, String emailInquilino) {
        boolean existe = false;
        try {
            Connection con = BD.getInstancia().getConexion();
            String sql = "SELECT * FROM puntuacion WHERE codHabi=? AND emailInquilino=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, codHabi);
            ps.setString(2, emailInquilino);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                existe = true;
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return existe;
    }

    // 3. NUEVO MÉTODO DE SEGURIDAD: Verificar si el alquiler existe y ha terminado
    public static boolean tienePermisoParaVotar(int codHabi, String emailInquilino) {
        // Requisito: Debe tener un alquiler con fechaFin < Hoy
        boolean permiso = false;
        try {
            Connection con = BD.getInstancia().getConexion();
            
            // Buscamos si existe un alquiler para esa sala y usuario que YA haya acabado
            // CURDATE() devuelve la fecha de hoy en MySQL
            String sql = "SELECT idAlquiler FROM alquiler " +
                         "WHERE codHabi=? AND emailInquilino=? AND fechaFinAlqui < CURDATE()";
            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, codHabi);
            ps.setString(2, emailInquilino);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                // Si encontramos un alquiler terminado...
                // ... verificamos que NO haya votado ya (Reutilizamos el método 2)
                if (!yaHaVotado(codHabi, emailInquilino)) {
                    permiso = true;
                }
            }
            
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return permiso;
    }

    // --- CALCULAR MEDIA DE UNA HABITACIÓN ---
    public static double obtenerMedia(int codHabi) {
        double media = 0.0;
        try {
            java.sql.Connection con = utils.BD.getInstancia().getConexion();
            String sql = "SELECT AVG(puntos) FROM puntuacion WHERE codHabi = ?";
            java.sql.PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, codHabi);
            java.sql.ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                media = rs.getDouble(1);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return media;
    }
}