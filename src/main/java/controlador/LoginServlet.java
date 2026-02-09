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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import modelo.Usuario;
import utils.BD;

public class LoginServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Recogemos los datos que vienen del formulario Login.jsp
        String email = request.getParameter("email");
        String pass = request.getParameter("password");
        
        String error = null;
        
        // 2. Validamos que no estén vacíos
        if(email == null || email.trim().isEmpty() || pass == null || pass.trim().isEmpty()){
            error = "Debes rellenar email y contraseña.";
        } else {
            // 3. Lógica de negocio: Consultar a la BD
            try {
                // USAMOS EL SINGLETON (Requisito obligatorio)
                Connection con = BD.getInstancia().getConexion();
                
                String sql = "SELECT * FROM usuario WHERE email = ? AND contrasena = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, email);
                ps.setString(2, pass);
                
                ResultSet rs = ps.executeQuery();
                
                if (rs.next()) {
                    // --- LOGIN CORRECTO ---
                    
                    // a) Creamos el objeto Usuario (Modelo)
                    Usuario u = new Usuario();
                    u.setEmail(rs.getString("email"));
                    u.setNombre(rs.getString("nombre"));
                    u.setImagenUsuario(rs.getString("imagenUsuario"));
                    // No guardamos la contraseña en el objeto sesión por seguridad
                    
                    // b) Guardamos al usuario en la SESIÓN (HttpSession)
                    // Esto es lo que mantiene al usuario "logueado" mientras navega
                    HttpSession session = request.getSession();
                    session.setAttribute("usuarioLogueado", u);
                    
                    // c) Redirigimos al Dashboard (Panel de control)
                    response.sendRedirect("Dashboard.jsp");
                    return; // Cortamos aquí para que no siga leyendo código
                    
                } else {
                    // --- LOGIN INCORRECTO ---
                    error = "Usuario no encontrado o contraseña incorrecta.";
                }
                
                rs.close();
                ps.close();
                
            } catch (Exception e) {
                e.printStackTrace();
                error = "Error técnico en la base de datos: " + e.getMessage();
            }
        }
        
        // 4. Si ha habido error, volvemos al Login y mostramos el mensaje
        request.setAttribute("error", error);
        request.getRequestDispatcher("Login.jsp").forward(request, response);
    }

    // --- MÉTODOS ESTÁNDAR DEL SERVLET (No hace falta tocarlos) ---
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}