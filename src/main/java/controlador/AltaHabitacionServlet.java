/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import modelo.Usuario;
import utils.BD;

@WebServlet(name = "AltaHabitacionServlet", urlPatterns = {"/AltaHabitacionServlet"})
public class AltaHabitacionServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8"); // Para que las tildes y ñ se guarden bien

        // 1. SEGURIDAD: ¿Quién intenta publicar?
        HttpSession session = request.getSession();
        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
        
        if (u == null) {
            response.sendRedirect("Login.jsp"); // Si no está logueado, fuera
            return;
        }

        // 2. RECOGER DATOS DEL FORMULARIO
        String ciudad = request.getParameter("ciudad");
        String direccion = request.getParameter("direccion");
        String precioStr = request.getParameter("precio");
        String latStr = request.getParameter("latitud"); // Vendrán del formulario
        String lonStr = request.getParameter("longitud");
        
        // Para la imagen, por simplicidad ahora pediremos el nombre del archivo (ej: "img/hab3.jpg")
        // Más adelante podrías implementar subida de archivos real si te sobra tiempo.
        String imagen = request.getParameter("imagen"); 

        String error = null;
        String mensaje = null;

        try {
            // Conversiones de tipos (String a Int/Double)
            int precio = Integer.parseInt(precioStr);
            double lat = Double.parseDouble(latStr);
            double lon = Double.parseDouble(lonStr);

            // 3. INSERTAR EN BASE DE DATOS
            Connection con = BD.getInstancia().getConexion();
            String sql = "INSERT INTO habitacion (ciudad, direccion, emailPropietario, imagenHabitacion, latitudH, longitudH, precioMes) VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, ciudad);
            ps.setString(2, direccion);
            ps.setString(3, u.getEmail()); // El dueño es el usuario logueado
            ps.setString(4, imagen);
            ps.setDouble(5, lat);
            ps.setDouble(6, lon);
            ps.setInt(7, precio);
            
            int filas = ps.executeUpdate();
            
            if (filas > 0) {
                mensaje = "¡Habitación publicada correctamente!";
            } else {
                error = "No se pudo guardar la habitación.";
            }
            
            ps.close();

        } catch (NumberFormatException e) {
            error = "Error: El precio o las coordenadas deben ser números válidos.";
        } catch (Exception e) {
            e.printStackTrace();
            error = "Error en la base de datos: " + e.getMessage();
        }

        // 4. VOLVER AL DASHBOARD CON EL RESULTADO
        if (error != null) {
            request.setAttribute("errorAlta", error);
        } else {
            request.setAttribute("mensajeAlta", mensaje);
        }
        
        // Redirigimos internamente para mantener los datos en pantalla si quieres, 
        // o reenviamos al JSP.
        request.getRequestDispatcher("Dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response); // Por si alguien llama por GET
    }
}