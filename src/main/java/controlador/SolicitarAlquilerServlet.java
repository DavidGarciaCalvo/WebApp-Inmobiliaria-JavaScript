/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date; // OJO: Importamos java.sql.Date
import java.sql.PreparedStatement;
import modelo.Usuario;
import utils.BD;

public class SolicitarAlquilerServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Seguridad: ¿Quién eres?
        HttpSession session = request.getSession();
        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
        
        if (u == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        // 2. Recoger datos
        String idHabStr = request.getParameter("idHabitacion");
        String fechaIniStr = request.getParameter("fechaInicio");
        String fechaFinStr = request.getParameter("fechaFin");
        
        String mensaje = null;
        String error = null;

        try {
            int idHab = Integer.parseInt(idHabStr);
            
            // Convertir String (del formulario) a java.sql.Date (para la BD)
            Date fechaInicio = Date.valueOf(fechaIniStr);
            Date fechaFin = Date.valueOf(fechaFinStr);

            // 3. Insertar en BD
            Connection con = BD.getInstancia().getConexion();
            
            // Comprobamos si ya ha solicitado esta habitación antes (Opcional, pero recomendable)
            // String checkSql = "SELECT * FROM solicitud WHERE codHabi=? AND emailInquilino=?"; ...
            // (Nos saltamos la comprobación para ir rápido, pero en un proyecto final estaría bien)

            String sql = "INSERT INTO solicitud (codHabi, emailInquilino, estado, fechaInicioPosibleAlquiler, fechaFinPosibleAlquiler) VALUES (?, ?, 'pendiente', ?, ?)";
            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idHab);
            ps.setString(2, u.getEmail());
            ps.setDate(3, fechaInicio);
            ps.setDate(4, fechaFin);
            
            int filas = ps.executeUpdate();
            
            if (filas > 0) {
                mensaje = "¡Solicitud enviada correctamente al propietario!";
            } else {
                error = "No se pudo enviar la solicitud.";
            }
            
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
            // CAMBIA ESTA LÍNEA:
            error = "ERROR TÉCNICO: " + e.getMessage(); 
            // Así veremos en la pantalla la causa real (Foreign Key, Date format, etc.)
        }

        // 4. Volver al Dashboard
        if (mensaje != null) request.setAttribute("mensajeAlta", mensaje); // Reusamos la variable de mensaje
        if (error != null) request.setAttribute("errorAlta", error);
        
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
        processRequest(request, response);
    }
}