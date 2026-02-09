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
import modelo.Puntuacion;
import modelo.PuntuacionDAO;
import modelo.Usuario;

public class AltaPuntuacionServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
        
        String idHabStr = request.getParameter("codHabi");
        String puntosStr = request.getParameter("puntos");
        
        String mensaje = null;
        String error = null;

        try {
            int codHabi = Integer.parseInt(idHabStr);
            int puntos = Integer.parseInt(puntosStr);
            
            // 1. Comprobamos REQUISITO : ¿Ya ha votado antes?
            if (PuntuacionDAO.yaHaVotado(codHabi, u.getEmail())) {
                error = "Ya has valorado esta habitación anteriormente. Solo se permite un voto.";
            } else {
                // 2. Si no ha votado, guardamos
                Puntuacion p = new Puntuacion(codHabi, u.getEmail(), puntos);
                boolean ok = PuntuacionDAO.insertarPuntuacion(p);
                
                if (ok) mensaje = "¡Gracias por tu valoración! ⭐";
                else error = "Error al guardar la valoración.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            error = "Error técnico al puntuar.";
        }
        
        // Volver a la pestaña de alquileres
        if (mensaje != null) request.setAttribute("mensajeAlta", mensaje);
        if (error != null) request.setAttribute("errorAlta", error);
        request.setAttribute("pestañaActiva", "alquileres");
        
        request.getRequestDispatcher("/Dashboard.jsp").forward(request, response);
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