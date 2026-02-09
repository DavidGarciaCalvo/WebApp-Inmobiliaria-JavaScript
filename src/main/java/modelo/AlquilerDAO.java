/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import utils.BD;

public class AlquilerDAO {

    // Método para insertar un nuevo alquiler en la BD
    public static boolean insertarAlquiler(Alquiler a) {
        boolean exito = false;
        try {
            Connection con = BD.getInstancia().getConexion();
            
            // Insertamos los datos obligatorios según Requisito 
            String sql = "INSERT INTO alquiler (codHabi, emailInquilino, fechaInicioAlqui, fechaFinAlqui) VALUES (?, ?, ?, ?)";
            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, a.getCodHabi());
            ps.setString(2, a.getEmailInquilino());
            ps.setDate(3, a.getFechaInicioAlqui());
            ps.setDate(4, a.getFechaFinAlqui());
            
            int filas = ps.executeUpdate();
            if (filas > 0) exito = true;
            
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exito;
    }
    
    // --- MÉTODO PARA EL INQUILINO: VER MIS ALQUILERES ---
    public static java.util.ArrayList<Alquiler> obtenerAlquileresDeInquilino(String emailInquilino) {
        java.util.ArrayList<Alquiler> lista = new java.util.ArrayList<>();
        
        // Usamos java.sql.* explícitamente para evitar líos con tu clase Connection
        java.sql.Connection con = null;
        java.sql.PreparedStatement ps = null;
        java.sql.ResultSet rs = null;

        try {
            con = BD.getInstancia().getConexion();
            
            // Tu SQL está perfecto: Trae todo (Alquiler + Habitación)
            String sql = "SELECT a.*, h.* " +
                         "FROM alquiler a " +
                         "JOIN habitacion h ON a.codHabi = h.codHabi " +
                         "WHERE a.emailInquilino = ? " +
                         "ORDER BY a.fechaInicioAlqui DESC"; // MEJORA: Ordenamos por fecha descendente (lo más nuevo arriba)
            
            if (con != null) {
                ps = con.prepareStatement(sql);
                ps.setString(1, emailInquilino);
                rs = ps.executeQuery();
                
                while (rs.next()) {
                    // 1. Crear objeto Alquiler y rellenar datos básicos
                    Alquiler a = new Alquiler();
                    a.setIdAlquiler(rs.getInt("idAlquiler"));
                    a.setCodHabi(rs.getInt("codHabi"));
                    a.setEmailInquilino(rs.getString("emailInquilino"));
                    a.setFechaInicioAlqui(rs.getDate("fechaInicioAlqui"));
                    a.setFechaFinAlqui(rs.getDate("fechaFinAlqui"));
                    
                    // 2. Crear objeto Habitación (Tu lógica original, la mantenemos por si acaso)
                    Habitacion h = new Habitacion();
                    h.setCodHabi(rs.getInt("codHabi")); 
                    h.setCiudad(rs.getString("ciudad"));
                    h.setDireccion(rs.getString("direccion"));
                    // OJO: Aquí usamos el nombre de columna que tú tienes en BD. 
                    // Si en BD es 'imagen', cambia "imagenHabitacion" por "imagen".
                    h.setImagenHabitacion(rs.getString("imagenHabitacion")); 
                    h.setPrecioMes(rs.getInt("precioMes"));
                    h.setEmailPropietario(rs.getString("emailPropietario"));
                    
                    // Metemos la habitación dentro del alquiler (Estructura clásica)
                    a.setHabitacion(h);
                    
                    // --- 3. LO NUEVO: Rellenamos los campos "visuales" para el JSP ---
                    // Esto es lo que permite que el JSP muestre foto y título directamente
                    
                    // TÍTULO: Ciudad - Dirección
                    a.setTituloHabitacion(h.getCiudad() + " - " + h.getDireccion());
                    
                    // FOTO: Usamos la misma que recuperaste, controlando nulos
                    String img = h.getImagenHabitacion();
                    a.setImagenHabitacion((img != null && !img.isEmpty()) ? img : "img/sin_foto.jpg");
                    
                    lista.add(a);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Cerramos recursos siempre
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            // No cerramos 'con' porque viene del Singleton BD
        }
        return lista;
    }
    
    // --- MÉTODO PARA EL PROPIETARIO: VER MIS CASAS ALQUILADAS ---
    public static java.util.ArrayList<Alquiler> obtenerAlquileresDePropietario(String emailPropietario) {
        java.util.ArrayList<Alquiler> lista = new java.util.ArrayList<>();
        try {
            Connection con = BD.getInstancia().getConexion();
            
            // REQUISITO: Visualizar habitaciones de mi propiedad alquiladas, ordenadas.
            // Hacemos JOIN para filtrar por el email del dueño (que está en la tabla habitacion)
            String sql = "SELECT a.*, h.* " +
                         "FROM alquiler a " +
                         "JOIN habitacion h ON a.codHabi = h.codHabi " +
                         "WHERE h.emailPropietario = ? " +
                         "ORDER BY a.codHabi ASC, a.fechaInicioAlqui ASC";
            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, emailPropietario);
            java.sql.ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Alquiler a = new Alquiler();
                a.setIdAlquiler(rs.getInt("idAlquiler"));
                a.setCodHabi(rs.getInt("codHabi"));
                a.setEmailInquilino(rs.getString("emailInquilino"));
                a.setFechaInicioAlqui(rs.getDate("fechaInicioAlqui"));
                a.setFechaFinAlqui(rs.getDate("fechaFinAlqui"));
                
                // Rellenamos datos de la habitación
                Habitacion h = new Habitacion();
                h.setCodHabi(rs.getInt("codHabi"));
                h.setCiudad(rs.getString("ciudad"));
                h.setDireccion(rs.getString("direccion"));
                h.setImagenHabitacion(rs.getString("imagenHabitacion"));
                h.setPrecioMes(rs.getInt("precioMes"));
                
                a.setHabitacion(h);
                lista.add(a);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}