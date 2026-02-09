/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import utils.BD;

public class SolicitudDAO {

    // Este método busca todas las solicitudes que ha recibido un PROPIETARIO
    // Método para ver las solicitudes que ME han enviado (Como PROPIETARIO)
    public static ArrayList<Solicitud> obtenerSolicitudesDeMisHabitaciones(String emailPropietario) {
        ArrayList<Solicitud> lista = new ArrayList<>();
        
        // MODIFICADO: Traemos imagen y datos de la habitación
        // Asegúrate de que la columna de imagen se llama 'imagenHabitacion' en tu tabla habitacion
        String sql = "SELECT s.*, h.imagenHabitacion, h.ciudad, h.direccion " +
                     "FROM solicitud s " +
                     "JOIN habitacion h ON s.codHabi = h.codHabi " +
                     "WHERE h.emailPropietario = ? " +
                     "ORDER BY s.idSolicitud DESC";
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = BD.getInstancia().getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, emailPropietario);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Solicitud s = new Solicitud();
                s.setIdSolicitud(rs.getInt("idSolicitud"));
                s.setCodHabi(rs.getInt("codHabi"));
                s.setEmailInquilino(rs.getString("emailInquilino"));
                s.setFechaInicioPosibleAlquiler(rs.getDate("fechaInicioPosibleAlquiler"));
                s.setFechaFinPosibleAlquiler(rs.getDate("fechaFinPosibleAlquiler"));
                s.setEstado(rs.getString("estado"));
                
                // --- DATOS VISUALES (Igual que en inquilino) ---
                String img = rs.getString("imagenHabitacion");
                s.setImagenHabitacion((img != null && !img.isEmpty()) ? img : "img/sin_foto.jpg");
                s.setTituloHabitacion(rs.getString("ciudad") + " - " + rs.getString("direccion"));
                
                lista.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if(rs!=null) rs.close(); } catch(Exception e){}
            try { if(ps!=null) ps.close(); } catch(Exception e){}
            // No cerramos con porque es Singleton
        }
        return lista;
    }
    
// Método para ver mis solicitudes enviadas (Como INQUILINO)
    // Método para ver mis solicitudes enviadas (Como INQUILINO)
    // AJUSTADO EXACTAMENTE A TU SQL (tabla 'solicitud', campo 'emailInquilino', etc.)
    public static ArrayList<Solicitud> obtenerMisSolicitudes(String emailInquilino) {
        ArrayList<Solicitud> lista = new ArrayList<>();
        
        // SQL corregido: usa 'solicitud' (singular) y los nombres exactos de tus columnas
        String sql = "SELECT s.*, h.ciudad, h.direccion, h.imagenHabitacion " +
                     "FROM solicitud s " + 
                     "JOIN habitacion h ON s.codHabi = h.codHabi " +
                     "WHERE s.emailInquilino = ? " +
                     "ORDER BY s.idSolicitud DESC"; 
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = BD.getInstancia().getConexion(); 
            
            if (con != null) {
                ps = con.prepareStatement(sql);
                ps.setString(1, emailInquilino);
                rs = ps.executeQuery();
                
                while (rs.next()) {
                    Solicitud s = new Solicitud();
                    
                    // Mapeo exacto con tu CREATE TABLE
                    s.setIdSolicitud(rs.getInt("idSolicitud")); 
                    s.setCodHabi(rs.getInt("codHabi"));
                    s.setEmailInquilino(rs.getString("emailInquilino"));
                    s.setEstado(rs.getString("estado"));
                    
                    // Fechas (según tu SQL son DATE)
                    s.setFechaInicioPosibleAlquiler(rs.getDate("fechaInicioPosibleAlquiler"));
                    s.setFechaFinPosibleAlquiler(rs.getDate("fechaFinPosibleAlquiler"));
                    
                    // Datos visuales extra (de la tabla habitacion)
                    String img = rs.getString("imagenHabitacion");
                    s.setImagenHabitacion((img != null && !img.isEmpty()) ? img : "img/sin_foto.jpg");
                    s.setTituloHabitacion(rs.getString("ciudad") + " - " + rs.getString("direccion"));
                    
                    lista.add(s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if(rs!=null) rs.close(); } catch(Exception e){}
            try { if(ps!=null) ps.close(); } catch(Exception e){}
        }
        
        return lista;
    }
    
    // --- NUEVO MÉTODO PARA ACEPTAR/RECHAZAR ---
    public static boolean actualizarEstado(int idSolicitud, String nuevoEstado) {
        boolean exito = false;
        try {
            Connection con = BD.getInstancia().getConexion();
            // Actualizamos el estado de una solicitud concreta
            String sql = "UPDATE solicitud SET estado = ? WHERE idSolicitud = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idSolicitud);
            
            int filas = ps.executeUpdate();
            if (filas > 0) exito = true;
            
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exito;
    }
    
    // --- MÉTODO FINAL: ACEPTAR CON VERIFICACIÓN PREVIA Y LIMPIEZA ---
    public static boolean aceptarSolicitud(int idSolicitud) {
        Connection con = null;
        PreparedStatement psGet = null;
        PreparedStatement psCheck = null; // <--- NUEVO: Para comprobar disponibilidad
        PreparedStatement psInsert = null;
        PreparedStatement psUpdate = null;
        PreparedStatement psRechazo = null;
        ResultSet rs = null;
        ResultSet rsCheck = null;

        try {
            con = BD.getInstancia().getConexion();
            
            // 1. Obtener datos de la solicitud que queremos aceptar
            String sqlGet = "SELECT * FROM solicitud WHERE idSolicitud = ?";
            psGet = con.prepareStatement(sqlGet);
            psGet.setInt(1, idSolicitud);
            rs = psGet.executeQuery();
            
            if (rs.next()) {
                int codHabi = rs.getInt("codHabi");
                String emailInq = rs.getString("emailInquilino");
                java.sql.Date fechaIni = rs.getDate("fechaInicioPosibleAlquiler");
                java.sql.Date fechaFin = rs.getDate("fechaFinPosibleAlquiler");
                
                // -----------------------------------------------------------------
                // 1.5 VERIFICACIÓN DE DISPONIBILIDAD (EL BLINDAJE)
                // -----------------------------------------------------------------
                // Consultamos si YA existe un alquiler para esa habitación en esas fechas.
                // Lógica de solape: (InicioExistente <= FinNuevo) AND (FinExistente >= InicioNuevo)
                String sqlCheck = "SELECT count(*) FROM alquiler " +
                                  "WHERE codHabi = ? " +
                                  "AND fechaInicioAlqui <= ? " +
                                  "AND fechaFinAlqui >= ?";
                
                psCheck = con.prepareStatement(sqlCheck);
                psCheck.setInt(1, codHabi);
                psCheck.setDate(2, fechaFin);
                psCheck.setDate(3, fechaIni);
                rsCheck = psCheck.executeQuery();
                
                if (rsCheck.next()) {
                    int ocupados = rsCheck.getInt(1);
                    if (ocupados > 0) {
                        System.out.println("ERROR: No se puede aceptar. La habitación ya está alquilada en esas fechas.");
                        return false; // <--- AQUÍ SE DETIENE SI HAY CONFLICTO
                    }
                }
                // -----------------------------------------------------------------

                // 2. Si llegamos aquí, está libre. Insertamos en ALQUILER.
                String sqlInsert = "INSERT INTO alquiler (codHabi, emailInquilino, fechaInicioAlqui, fechaFinAlqui) VALUES (?, ?, ?, ?)";
                psInsert = con.prepareStatement(sqlInsert);
                psInsert.setInt(1, codHabi);
                psInsert.setString(2, emailInq);
                psInsert.setDate(3, fechaIni);
                psInsert.setDate(4, fechaFin);
                psInsert.executeUpdate();
                
                // 3. Actualizar la solicitud actual a 'aceptada'
                String sqlUpdate = "UPDATE solicitud SET estado='aceptada' WHERE idSolicitud=?";
                psUpdate = con.prepareStatement(sqlUpdate);
                psUpdate.setInt(1, idSolicitud);
                psUpdate.executeUpdate();
                
                // 4. Rechazo automático de otras pendientes que solapen
                String sqlRechazo = "UPDATE solicitud SET estado = 'rechazada' " +
                                    "WHERE codHabi = ? " +
                                    "AND idSolicitud != ? " +
                                    "AND estado = 'pendiente' " +
                                    "AND fechaInicioPosibleAlquiler <= ? " +
                                    "AND fechaFinPosibleAlquiler >= ?";

                psRechazo = con.prepareStatement(sqlRechazo);
                psRechazo.setInt(1, codHabi);
                psRechazo.setInt(2, idSolicitud);
                psRechazo.setDate(3, fechaFin);
                psRechazo.setDate(4, fechaIni);
                psRechazo.executeUpdate();

                return true;
            }
            return false;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (rsCheck != null) rsCheck.close(); } catch (Exception e) {}
            try { if (psGet != null) psGet.close(); } catch (Exception e) {}
            try { if (psCheck != null) psCheck.close(); } catch (Exception e) {}
            try { if (psInsert != null) psInsert.close(); } catch (Exception e) {}
            try { if (psUpdate != null) psUpdate.close(); } catch (Exception e) {}
            try { if (psRechazo != null) psRechazo.close(); } catch (Exception e) {}
        }
    }
}
