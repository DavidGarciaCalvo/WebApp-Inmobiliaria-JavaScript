<%-- 
    Document   : login
    Created on : 31 dic 2025, 13:17:49
    Author     : David
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar Sesión - VitoBadi16</title>
    <link href="https://fonts.googleapis.com/css2?family=Mulish:wght@400;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/estilos.css">
</head>
<body class="login-body">
    
    <div class="login-container">
        <h2 class="login-title">Acceso de Usuario</h2>
        
        <form action="LoginServlet" method="POST">
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" name="email" id="email" class="form-input" placeholder="ejemplo@gmail.com" required>
            </div>
            
            <div class="form-group">
                <label for="password">Contraseña:</label>
                <input type="password" name="password" id="password" class="form-input" placeholder="Tu contraseña" required>
            </div>
            
            <button type="submit" class="btn-login">Entrar</button>
            <a href="Dashboard.jsp" class="btn-secondary-outline" style="text-decoration:none; display:block; text-align:center;">Volver al inicio</a>        </form>
        
        <% if (request.getAttribute("error") != null) { %>
            <p class="error-msg" style="display:block;">
                <%= request.getAttribute("error") %>
            </p>
        <% } %>
    </div>

</body>
</html>