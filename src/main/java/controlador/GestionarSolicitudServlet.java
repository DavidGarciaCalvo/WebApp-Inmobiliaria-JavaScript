/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import modelo.SolicitudDAO;

public class GestionarSolicitudServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("idSolicitud");
        String accion = request.getParameter("accion"); 
        
        String mensaje = null;
        String error = null;
        
        try {
            int idSolicitud = Integer.parseInt(idStr);
            
            if ("aceptar".equals(accion)) {
                // REQUISITO: Al aceptar, hay que insertar en la tabla Alquiler 
                
                // 1. Primero recuperamos los datos completos de la solicitud (para saber fechas y habitacion)
                // (Necesitamos un método auxiliar en el DAO o buscarla de nuevo, 
                // pero para simplificar, usaremos un truco: recuperar por ID si implementamos ese método,
                // O mejor aún: para este sprint, asumimos que el DAO de actualizar estado funciona y añadimos la lógica de inserción).
                
                // NOTA: Para hacerlo bien, necesitamos saber las fechas. 
                // Vamos a suponer que SolicitudDAO tiene un método "obtenerPorId".
                // Como no lo tenemos creado aún, vamos a crear ese método rápido en el DAO o hacer una "trampa" limpia.
                
                // Vamos a llamar al DAO para que haga TODO: "aceptarSolicitudYCrearAlquiler".
                // Es más profesional encapsularlo.
                
                boolean ok = modelo.SolicitudDAO.aceptarSolicitud(idSolicitud);
                
                if (ok) {
                    mensaje = "¡Alquiler formalizado! Solicitud aceptada y contrato creado.";
                } else {
                    error = "No se pudo formalizar el alquiler (quizás error en fechas).";
                }

            } else {
                // RECHAZAR
                boolean exito = modelo.SolicitudDAO.actualizarEstado(idSolicitud, "rechazada");
                if (exito) mensaje = "Solicitud rechazada correctamente.";
                else error = "Error al rechazar.";
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            error = "Error técnico: " + e.getMessage();
        }
        
        // Volver al Dashboard
        if (mensaje != null) request.setAttribute("mensajeAlta", mensaje);
        if (error != null) request.setAttribute("errorAlta", error);
        request.setAttribute("pestañaActiva", "solicitudes");
        
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