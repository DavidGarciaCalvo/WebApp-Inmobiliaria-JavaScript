package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import utils.BD;

public class HabitacionDAO {

    // Método auxiliar para evitar nulos en las imágenes
    private static String procesarImagen(String imgBD) {
        if (imgBD == null || imgBD.trim().isEmpty()) {
            return "img/sin_foto.jpg"; // Imagen por defecto si no hay nada
        }
        return imgBD; // Devolvemos la ruta tal cual viene de la BD (ej: "img/hab1.jpg")
    }

    // 1. Obtener habitaciones de un propietario
    public static ArrayList<Habitacion> obtenerMisHabitaciones(String emailPropietario) {
        ArrayList<Habitacion> lista = new ArrayList<>();
        try {
            Connection con = BD.getInstancia().getConexion();
            String sql = "SELECT * FROM habitacion WHERE emailPropietario = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, emailPropietario);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Habitacion h = mapearHabitacion(rs);
                lista.add(h);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    // 2. Obtener TODAS (para el mapa y listado general)
    public static ArrayList<Habitacion> obtenerTodas() {
        ArrayList<Habitacion> lista = new ArrayList<>();
        try {
            Connection con = BD.getInstancia().getConexion();
            String sql = "SELECT * FROM habitacion"; 
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Habitacion h = mapearHabitacion(rs);
                lista.add(h);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    // 3. Buscar disponibles (Filtros)
    public static ArrayList<Habitacion> buscarDisponibles(String ciudad, java.sql.Date fInicio, java.sql.Date fFin, String emailUsuarioLogueado) {
        ArrayList<Habitacion> lista = new ArrayList<>();
        try {
            Connection con = BD.getInstancia().getConexion();
            String sql = "SELECT * FROM habitacion h " +
                         "WHERE h.ciudad = ? " +
                         "AND h.emailPropietario != ? " +
                         "AND h.codHabi NOT IN ( " +
                            "SELECT codHabi FROM alquiler " +
                            "WHERE fechaInicioAlqui <= ? AND fechaFinAlqui >= ? " +
                         ")";
            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, ciudad);
            ps.setString(2, emailUsuarioLogueado);
            ps.setDate(3, fFin);   
            ps.setDate(4, fInicio); 
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Habitacion h = mapearHabitacion(rs);
                lista.add(h);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Método auxiliar para no repetir código de mapeo
    private static Habitacion mapearHabitacion(ResultSet rs) throws Exception {
        Habitacion h = new Habitacion();
        h.setCodHabi(rs.getInt("codHabi"));
        h.setCiudad(rs.getString("ciudad"));
        h.setDireccion(rs.getString("direccion"));
        h.setEmailPropietario(rs.getString("emailPropietario"));
        
        // AQUI USAMOS EL PROCESADOR DE IMAGEN
        h.setImagenHabitacion(procesarImagen(rs.getString("imagenHabitacion")));
        
        h.setLatitudH(rs.getDouble("latitudH"));
        h.setLongitudH(rs.getDouble("longitudH"));
        h.setPrecioMes(rs.getInt("precioMes"));
        return h;
    }
}