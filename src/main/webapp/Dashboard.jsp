<%-- 
    Document   : Dashboard.jsp
    Created on : 31 dic 2025, 13:31:12
    Author     : David
--%>
<%@page import="java.util.ArrayList"%>
<%@page import="modelo.Habitacion"%>
<%@page import="modelo.HabitacionDAO"%>
<%@page import="modelo.Usuario"%>
<%@page import="modelo.Solicitud"%> 
<%@page import="modelo.SolicitudDAO"%>
<%@page import="modelo.Alquiler"%>
<%@page import="modelo.AlquilerDAO"%>
<%@page import="modelo.Puntuacion"%>
<%@page import="modelo.PuntuacionDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    // --- 1. BLOQUE DE SEGURIDAD FLEXIBLE ---
    // Usamos 'session' (que es un objeto implícito en JSP, no hace falta declararlo)
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
    
    // Variable para saber en el resto de la página si es Invitado o Usuario
    boolean logueado = (usuario != null);

    // Preparamos variables para evitar errores (NullPointerException) abajo
    String emailUsuario = "";
    String nombreUsuario = "Invitado";
    String fotoUsuario = "img/user_default.png"; // Una foto genérica por defecto

    if (logueado) {
        emailUsuario = usuario.getEmail();
        nombreUsuario = usuario.getNombre();
        // Si el usuario tiene foto, la usamos. Si no, dejamos la default.
        if(usuario.getImagenUsuario() != null) {
            fotoUsuario = usuario.getImagenUsuario();
        }
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mi Panel - VitoBadi16</title>
    <link href="https://fonts.googleapis.com/css2?family=Mulish:wght@400;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/estilos.css">
</head>
<body class="dashboard-page">

    <header class="main-header" style="background-color: #2c3e50; padding: 10px 20px; display: flex; justify-content: space-between; align-items: center; color: white;">
        <div class="logo-container" style="font-size: 1.5rem; font-weight: bold;">VitoBadi16</div>
        
        <div class="user-info" style="display: flex; align-items: center;">
            <% if (logueado) { %>
                <img src="<%= fotoUsuario %>" alt="Perfil" class="avatar-small" style="width:40px; height:40px; border-radius:50%; margin-right:10px; object-fit:cover;">
                <span id="saludo-usuario" style="margin-right: 15px;">Hola, <%= nombreUsuario %></span>
                <a href="LogoutServlet" class="btn-nav" style="color: white; text-decoration: none; border: 1px solid white; padding: 5px 10px; border-radius: 4px;">Cerrar Sesión</a>
            <% } else { %>
                <span style="margin-right: 15px; opacity: 0.8;">Modo Invitado</span>
                <a href="Login.jsp" class="btn-nav" style="background-color: #3498db; color: white; text-decoration: none; border: none; padding: 8px 15px; border-radius: 4px; font-weight: bold;">Iniciar Sesión</a>
            <% } %>
        </div>
    </header>

    <main class="container dashboard-container">

        <% if(request.getAttribute("mensajeAlta") != null) { %>
            <div style="background-color: #d4edda; color: #155724; padding: 15px; border-radius: 5px; margin-bottom: 20px; border: 1px solid #c3e6cb;">
                <strong>✅ ¡Éxito!</strong> <%= request.getAttribute("mensajeAlta") %>
            </div>
        <% } %>
        
        <% if(request.getAttribute("errorAlta") != null) { %>
            <div style="background-color: #f8d7da; color: #721c24; padding: 15px; border-radius: 5px; margin-bottom: 20px; border: 1px solid #f5c6cb;">
                <strong>⚠️ Atención:</strong> <%= request.getAttribute("errorAlta") %>
            </div>
        <% } %>

        <nav class="dashboard-nav" style="border-bottom: 1px solid #ccc; padding-bottom: 10px; margin-bottom: 20px;">
            <div style="display: flex; gap: 10px;">
                <button class="tab-btn active" onclick="mostrarSeccion('buscar')">🔍 Buscar</button>
                
                <% if (logueado) { %>
                    <button class="tab-btn" onclick="mostrarSeccion('mis-habitaciones')">🔑 Mis Habitaciones</button>
                    <button class="tab-btn" onclick="mostrarSeccion('alquileres')">🏠 Mis Alquileres</button>
                    <button class="tab-btn" onclick="mostrarSeccion('solicitudes')">📩 Solicitudes</button>
                <% } %>
            </div>
        </nav>

        <hr class="divider">

        <section id="sec-buscar" class="panel-section active">
            
            <div style="background: white; padding: 20px; border-radius: 10px; box-shadow: 0 4px 6px rgba(0,0,0,0.05); margin-bottom: 30px;">
                <h2 style="margin-top: 0;">🔍 Encuentra tu hogar ideal</h2>
                
                <form action="Dashboard.jsp" method="GET" style="display: flex; gap: 15px; flex-wrap: wrap; align-items: end;">
                    <div style="flex: 1; min-width: 200px;">
                        <label style="font-weight: bold; display: block; margin-bottom: 5px;">Ciudad:</label>
                        <select name="busqCiudad" class="form-input" style="width: 100%; padding: 10px;">
                            <option value="Vitoria">Vitoria-Gasteiz</option>
                            <option value="Bilbao">Bilbao</option>
                            <option value="Donostia">Donostia-San Sebastián</option>
                        </select>
                    </div>
                    
                    <div style="flex: 1; min-width: 150px;">
                        <label style="font-weight: bold; display: block; margin-bottom: 5px;">Desde:</label>
                        <input type="date" name="busqFechaIni" required class="form-input" style="width: 100%; padding: 10px;">
                    </div>
                    
                    <div style="flex: 1; min-width: 150px;">
                        <label style="font-weight: bold; display: block; margin-bottom: 5px;">Hasta:</label>
                        <input type="date" name="busqFechaFin" required class="form-input" style="width: 100%; padding: 10px;">
                    </div>
                    
                    <div style="min-width: 120px;">
                         <button type="submit" class="btn-login" style="padding: 12px; height: 100%;">Buscar 🔎</button>
                    </div>
                </form>
            </div>
            
            <% if (request.getParameter("busqCiudad") != null) { %>
                <div id="mapaBusqueda" style="width: 100%; height: 400px; background-color: #eee; margin-bottom: 30px; border-radius: 10px; border: 1px solid #ddd; position: relative;">
                    <p style="text-align:center; padding-top: 180px; color: #777;">Cargando mapa...</p>
                </div>
            <% } %>

            <div class="grid-habitaciones" style="display: grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap: 20px;">
                <%
                    // LÓGICA DE BÚSQUEDA
                    java.util.ArrayList<Habitacion> resultados = new java.util.ArrayList<>();
                    
                    String bCiudad = request.getParameter("busqCiudad");
                    String bIni = request.getParameter("busqFechaIni");
                    String bFin = request.getParameter("busqFechaFin");
                    
                    // Email para filtrar (si es anónimo enviamos "", si es usuario su email)
                    String filtroEmail = (logueado) ? emailUsuario : "";

                    if (bCiudad != null && bIni != null && bFin != null) {
                        try {
                            java.sql.Date sqlFechaIni = java.sql.Date.valueOf(bIni);
                            java.sql.Date sqlFechaFin = java.sql.Date.valueOf(bFin);
                            resultados = HabitacionDAO.buscarDisponibles(bCiudad, sqlFechaIni, sqlFechaFin, filtroEmail);
                        } catch (Exception e) {
                            out.print("<p style='color:red'>Error en el formato de las fechas.</p>");
                        }
                    } else {
                        resultados = HabitacionDAO.obtenerTodas(); 
                    }
                    
                    // PINTAR RESULTADOS
                    if (resultados.isEmpty()) {
                %>
                    <p style="grid-column: 1/-1; text-align: center; padding: 20px; font-size: 1.2rem; color: #777;">
                        No se encontraron habitaciones disponibles con esos criterios. 🏜️
                    </p>
                <%
                    } else {
                        for (Habitacion h : resultados) {
                            if (logueado && h.getEmailPropietario().equals(emailUsuario)) continue;
                %>
                    <div class="habitacion-card" style="background: white; border-radius: 12px; box-shadow: 0 4px 10px rgba(0,0,0,0.1); overflow: hidden; display: flex; flex-direction: column;">
                        
                        <div style="height: 180px; position: relative; background-color: #eee;">
                            <% if (logueado) { %>
                                <img src="<%= h.getImagenHabitacion() %>" style="width:100%; height:100%; object-fit:cover;" onerror="this.style.display='none';"">
                            <% } else { %>
                                <div style="width:100%; height:100%; display: flex; align-items: center; justify-content: center; color: #777; flex-direction: column;">
                                    <span style="font-size: 3rem;">🔒</span>
                                    <p style="margin:0;">Inicia sesión para ver fotos</p>
                                </div>
                            <% } %>
                            
                            <span style="position: absolute; bottom: 10px; right: 10px; background: rgba(0,0,0,0.7); color: white; padding: 5px 10px; border-radius: 20px; font-weight: bold;">
                                <%= h.getPrecioMes() %>€
                            </span>
                        </div>
                        
                        <div style="padding: 15px; flex-grow: 1; display: flex; flex-direction: column; justify-content: space-between;">
                            <div>
                                <h3 style="margin: 0; font-size: 1.1rem;"><%= h.getCiudad() %></h3>
                                <p style="color: #666; font-size: 0.9rem;"><%= h.getDireccion() %></p>
                                
                                <% if (logueado) { %>
                                    <p style="font-size: 0.8rem; color: #2980b9; margin-top: 5px;">
                                        👤 Propietario: <%= h.getEmailPropietario() %>
                                    </p>

                                    <% 
                                        double media = PuntuacionDAO.obtenerMedia(h.getCodHabi());
                                        int estrellas = (int) Math.round(media);
                                    %>
                                    <div style="color: #f1c40f; font-size: 1rem; margin-top: 5px;">
                                        <% for(int i=0; i<estrellas; i++) { %>⭐<% } %>
                                        
                                        <% if(media == 0) { %>
                                            <span style="color: #999; font-size: 0.8rem;">(Sin votos)</span>
                                        <% } else { %>
                                            <span style="color: #666; font-size: 0.8rem; font-weight: bold;">
                                                <%= String.format("%.1f", media) %> / 5
                                            </span>
                                        <% } %>
                                    </div>
                                    <% } %>
                            </div>
                            
                            <div style="margin-top: 15px;">
                                <% if (logueado) { %>
                                    <form action="SolicitarAlquilerServlet" method="POST">
                                        <input type="hidden" name="idHabitacion" value="<%= h.getCodHabi() %>">
                                        
                                        <label style="font-size: 0.8rem; display:block;">Desde:</label>
                                        <input type="date" name="fechaInicio" value="<%= (bIni!=null)?bIni:"" %>" required style="width: 100%; margin-bottom: 5px;">
                                        
                                        <label style="font-size: 0.8rem; display:block;">Hasta:</label>
                                        <input type="date" name="fechaFin" value="<%= (bFin!=null)?bFin:"" %>" required style="width: 100%; margin-bottom: 10px;">

                                        <button type="submit" class="btn-login" style="padding: 8px 15px; width: 100%; background-color: #e74c3c;">Solicitar Visita</button>
                                    </form>
                                <% } else { %>
                                    <p style="font-size: 0.8rem; color: #f39c12; margin-bottom: 5px;">⚠ Identifícate para ver detalles y alquilar.</p>
                                    <a href="Login.jsp" class="btn-login" style="display: block; text-align: center; padding: 10px; text-decoration: none; background-color: #3498db;">
                                        🔑 Iniciar Sesión
                                    </a>
                                <% } %>
                            </div>
                        </div>
                    </div>
                <%
                        } // Fin for
                    } // Fin else
                %>
            </div>
            <div id="seccion-mapa" style="margin-top: 30px; padding: 20px; background: white; border-radius: 10px;">
                <h2>🌍 Búsqueda por Cercanía</h2>
                <p>Haz clic en el mapa para marcar tu ubicación y ajusta el radio de búsqueda.</p>

                <div style="margin-bottom: 15px; display: flex; align-items: center; gap: 10px;">
                    <label for="geo-radio">Radio de búsqueda:</label>
                    <input type="range" id="geo-radio" min="1" max="10" value="2" step="0.5" style="width: 200px;">
                    <span id="val-radio" style="font-weight: bold; color: #3498db;">2</span> km
                </div>

                <input type="hidden" id="geo-lat">
                <input type="hidden" id="geo-lng">

                <div id="map" style="width: 100%; height: 500px; border-radius: 10px; border: 2px solid #ddd;"></div>
            </div>
        </section>
        
        <section id="sec-mis-habitaciones" class="panel-section" style="display:none;">
            <div class="container-interior" style="padding: 20px;">

                <h2>➕ Publicar nueva habitación</h2>

                    <form action="AltaHabitacionServlet" method="POST" style="background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); max-width: 600px;">

                        <div class="form-group" style="margin-bottom: 15px;">
                            <label>Ciudad:</label>
                            <select name="ciudad" class="form-input" style="width: 100%; padding: 8px;" required>
                                <option value="Vitoria">Vitoria-Gasteiz</option>
                                <option value="Bilbao">Bilbao</option>
                                <option value="Donostia">Donostia-San Sebastián</option>
                            </select>
                        </div>

                        <div class="form-group" style="margin-bottom: 15px;">
                            <label>Dirección:</label>
                            <input type="text" name="direccion" class="form-input" placeholder="Ej: Calle Dato, 1" style="width: 100%; padding: 8px;" required>
                        </div>

                        <div class="form-group" style="margin-bottom: 15px;">
                            <label>Precio (€/mes):</label>
                            <input type="number" name="precio" class="form-input" min="1" step="1" value="300" style="width: 100%; padding: 8px;" required>
                        </div>

                        <div class="form-group" style="margin-bottom: 15px;">
                            <label style="color: #3498db; font-weight: bold;">📍 Ubicación (Haz clic en el mapa):</label>
                            <div id="mapAlta" style="width: 100%; height: 300px; border: 1px solid #ccc; border-radius: 4px; margin-top: 5px;"></div>
                        </div>

                        <div style="display: flex; gap: 10px; margin-bottom: 15px;">
                            <div style="flex: 1;">
                                <label>Latitud:</label>
                                <input type="text" name="latitud" class="form-input" style="width: 100%; padding: 8px; background: #eee;" required>
                            </div>
                            <div style="flex: 1;">
                                <label>Longitud:</label>
                                <input type="text" name="longitud" class="form-input" style="width: 100%; padding: 8px; background: #eee;" required>
                            </div>
                        </div>

                        <div class="form-group" style="margin-bottom: 15px;">
                            <label>Imagen (Ruta):</label>
                            <input type="text" name="imagen" value="img/hab3.jpg" class="form-input" style="width: 100%; padding: 8px;" required>
                        </div>

                        <button type="submit" class="btn-login" style="width: auto; padding: 10px 20px;">Publicar Anuncio</button>
                    </form>

                <h2>🔑 Mis Habitaciones Publicadas</h2>
                <div class="grid-habitaciones" style="display: grid; grid-template-columns: repeat(auto-fill, minmax(250px, 1fr)); gap: 20px; margin-top: 20px;">
                    
                    <% 
                        // --- CORRECCIÓN AQUÍ ---
                        java.util.ArrayList<Habitacion> misHabs = new java.util.ArrayList<>();
                        
                        // Solo buscamos si hay usuario logueado
                        if (usuario != null) {
                            misHabs = HabitacionDAO.obtenerMisHabitaciones(usuario.getEmail());
                        }
                        
                        if (misHabs.isEmpty()) { 
                    %>
                        <p style="grid-column: 1/-1; color: #666; font-style: italic;">
                            No tienes habitaciones publicadas todavía. ¡Anímate a subir una arriba!
                        </p>
                    <% 
                        } else { 
                            for (Habitacion h : misHabs) {
                    %>
                        <div class="habitacion-card" style="background: white; border: 1px solid #ddd; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 5px rgba(0,0,0,0.1);">
                            <div style="height: 150px; background-color: #eee;">
                                <img src="<%= h.getImagenHabitacion() %>" alt="Foto" style="width: 100%; height: 100%; object-fit: cover;" onerror="this.src='https://via.placeholder.com/300x200?text=Sin+Foto'"> 
                            </div>
                            <div style="padding: 15px;">
                                <h3 style="margin: 0 0 5px 0; color: #333;"><%= h.getCiudad() %></h3>
                                <p style="margin: 0; color: #666; font-size: 0.9em;"><%= h.getDireccion() %></p>
                                <p style="margin: 10px 0 0 0; font-weight: bold; color: #27ae60; font-size: 1.2em;">
                                    <%= h.getPrecioMes() %> €/mes
                                </p>
                            </div>
                        </div>
                    <% 
                            } 
                        } 
                    %>
                </div>
            </div>
        </section>

        <section id="sec-alquileres" class="panel-section" style="display:none;">
    
            <% 
                java.util.ArrayList<Alquiler> soyInquilino = new java.util.ArrayList<>();
                if (usuario != null) {
                    soyInquilino = AlquilerDAO.obtenerAlquileresDeInquilino(usuario.getEmail());
                }

                if (!soyInquilino.isEmpty()) { 
            %>
                <div style="background: white; padding: 20px; border-radius: 10px; border-top: 5px solid #27ae60; margin-bottom: 40px;">
                    <h2 style="color: #27ae60;">🏠 Casas que he alquilado</h2>
                    <p>Historial de tus estancias y contratos vigentes.</p>

                    <div class="grid-habitaciones" style="display: grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap: 20px; margin-top: 20px;">
                        <% 
                            java.util.Date fechaHoy = new java.util.Date();
                            for (Alquiler a : soyInquilino) { 
                                boolean haTerminado = a.getFechaFinAlqui().before(fechaHoy);
                                boolean yaVoto = PuntuacionDAO.yaHaVotado(a.getCodHabi(), usuario.getEmail());

                                String colorBorde = haTerminado ? "#95a5a6" : "#27ae60"; 
                                String estadoTexto = haTerminado ? "FINALIZADO" : "EN CURSO";
                        %>
                            <div style="background: white; border: 2px solid <%= colorBorde %>; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.1); position: relative;">

                                <div style="height: 160px; background: #eee; position: relative;">
                                    <img src="<%= a.getImagenHabitacion() %>" style="width:100%; height:100%; object-fit: cover;" onerror="this.src='img/no-foto.jpg'">
                                    <span style="position: absolute; top: 10px; right: 10px; background: <%= colorBorde %>; color: white; padding: 4px 10px; border-radius: 15px; font-weight: bold; font-size: 0.8rem;">
                                        <%= estadoTexto %>
                                    </span>
                                </div>

                                <div style="padding: 15px;">
                                    <h3 style="margin: 0 0 5px 0; font-size: 1.1rem; color: #2c3e50;">
                                        <%= a.getTituloHabitacion() %>
                                    </h3>

                                    <p style="margin: 0 0 10px 0; font-size: 0.85rem; color: #555; background-color: #f0f0f0; display: inline-block; padding: 2px 8px; border-radius: 4px;">
                                        🆔 ID Habitación: <strong><%= a.getCodHabi() %></strong>
                                    </p>

                                    <p style="margin: 0; font-size: 0.9rem; color: #666;">
                                        📅 <%= a.getFechaInicioAlqui() %> <span style="color:#aaa">➡</span> <%= a.getFechaFinAlqui() %>
                                    </p>

                                    <div style="margin-top: 15px; border-top: 1px solid #eee; padding-top: 10px;">
                                        <% if (!haTerminado) { %>
                                            <p style="color: #f39c12; font-size: 0.85rem; margin: 0;">
                                                ⏳ Podrás valorar al finalizar la estancia.
                                            </p>
                                        <% } else if (yaVoto) { %>
                                            <p style="color: #27ae60; font-weight: bold; margin: 0; font-size: 0.9rem;">
                                                ✅ ¡Gracias por tu valoración!
                                            </p>
                                        <% } else { %>
                                            <form action="AltaPuntuacionServlet" method="POST" style="background: #f9f9f9; padding: 10px; border-radius: 5px;">
                                                <input type="hidden" name="codHabi" value="<%= a.getCodHabi() %>">
                                                <label style="display:block; font-size: 0.8rem; margin-bottom: 5px;">Deja tu puntuación:</label>
                                                <div style="display: flex; gap: 5px;">
                                                    <select name="puntos" style="flex: 1; padding: 5px;">
                                                        <option value="5">⭐⭐⭐⭐⭐</option>
                                                        <option value="4">⭐⭐⭐⭐</option>
                                                        <option value="3">⭐⭐⭐</option>
                                                        <option value="2">⭐⭐</option>
                                                        <option value="1">⭐</option>
                                                    </select>
                                                    <button type="submit" style="background: #f1c40f; border: none; padding: 5px 10px; cursor: pointer; border-radius: 3px;">Enviar</button>
                                                </div>
                                            </form>
                                        <% } %>
                                    </div>
                                </div>
                            </div>
                        <% } %>
                    </div>
                </div>
            <% } %>

            <% 
                java.util.ArrayList<Alquiler> soyPropietario = new java.util.ArrayList<>();
                if (usuario != null) {
                    soyPropietario = AlquilerDAO.obtenerAlquileresDePropietario(usuario.getEmail());
                }

                if (!soyPropietario.isEmpty()) { 
            %>
                <div style="background: white; padding: 20px; border-radius: 10px; border-top: 5px solid #2980b9;">
                    <h2 style="color: #2980b9;">🔑 Mis pisos alquilados (Propietario)</h2>
                    <div class="grid-habitaciones" style="display: grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap: 20px; margin-top: 20px;">
                        <% for (Alquiler a : soyPropietario) { %>
                            <div style="background: white; border: 2px solid #2980b9; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.1);">
                                <div style="height: 150px; background: #eee;">
                                    <img src="<%= a.getHabitacion().getImagenHabitacion() %>" style="width:100%; height:100%; object-fit:cover;" onerror="this.style.display='none';">
                                </div>
                                <div style="padding: 15px;">
                                    <h3 style="margin: 0; color: #2c3e50;"><%= a.getHabitacion().getCiudad() %></h3>

                                    <p style="font-size: 0.8rem; color: #555; background: #eee; display: inline-block; padding: 2px 6px; border-radius: 4px; margin: 5px 0;">
                                        ID: <strong><%= a.getHabitacion().getCodHabi() %></strong>
                                    </p>

                                    <p style="font-size: 0.9rem; color: #666;"><%= a.getHabitacion().getDireccion() %></p>
                                    <hr style="margin: 10px 0; border: 0; border-top: 1px solid #eee;">
                                    <p style="margin-bottom: 5px; color: #2980b9;"><strong>👤 Inquilino:</strong><br> <%= a.getEmailInquilino() %></p>
                                    <p style="margin: 0; font-size: 0.9rem;">📅 <%= a.getFechaInicioAlqui() %> al <%= a.getFechaFinAlqui() %></p>
                                </div>
                            </div>
                        <% } %>
                    </div>
                </div>
            <% } %>

            <% if (soyInquilino.isEmpty() && soyPropietario.isEmpty()) { %>
                <div style="text-align: center; padding: 50px; background: #fdfdfd; border-radius: 10px; border: 1px dashed #ccc;">
                    <h3 style="color: #999;">📭 Sin actividad</h3>
                    <p style="color: #aaa;">No tienes alquileres activos.</p>
                </div>
            <% } %>
        </section>
        
        <section id="sec-solicitudes" class="panel-section" style="display:none;">
    
            <% 
                // 1. PREPARAMOS LAS DOS LISTAS (Recibidas y Enviadas)
                java.util.ArrayList<Solicitud> recibidas = new java.util.ArrayList<>();
                java.util.ArrayList<Solicitud> enviadas = new java.util.ArrayList<>();

                if (usuario != null) {
                    // CARGA 1: Lo que me han pedido a mí (Soy Propietario)
                    recibidas = SolicitudDAO.obtenerSolicitudesDeMisHabitaciones(usuario.getEmail());

                    // CARGA 2: Lo que yo he pedido (Soy Inquilino)  <-- ESTO ES LO QUE TE FALTABA
                    enviadas = SolicitudDAO.obtenerMisSolicitudes(usuario.getEmail());
                }
            %>

            <% if (!recibidas.isEmpty()) { %>
                <h2 style="color: #2980b9;">📩 Solicitudes Recibidas (Eres Propietario)</h2>
                <div style="margin-top: 20px; margin-bottom: 40px;">
                <% for (Solicitud s : recibidas) { %>
                    <div style="background: white; border-left: 5px solid #3498db; border-radius: 5px; padding: 20px; margin-bottom: 15px; box-shadow: 0 2px 5px rgba(0,0,0,0.05); display: flex; justify-content: space-between; align-items: center;">
                        <div>
                            <h3 style="margin: 0; color: #2c3e50;">Solicitud #<%= s.getIdSolicitud() %></h3>
                            <p style="margin: 5px 0; color: #2980b9; font-size: 1.1em;">
                                🏠 <strong><%= (s.getTituloHabitacion() != null) ? s.getTituloHabitacion() : "Habitación #" + s.getCodHabi() %></strong>
                            </p>
                            <p style="margin: 5px 0;"><strong>Inquilino:</strong> <%= s.getEmailInquilino() %></p>
                            <p style="margin: 0; color: #666;">
                                📅 Del <%= s.getFechaInicioPosibleAlquiler() %> al <%= s.getFechaFinPosibleAlquiler() %>
                            </p>
                            <p style="margin-top: 5px;">Estado: <strong><%= s.getEstado() %></strong></p>
                        </div>

                        <% if(s.getEstado().equals("pendiente")) { %>
                        <div style="display: flex; gap: 10px;">
                            <form action="GestionarSolicitudServlet" method="POST">
                                <input type="hidden" name="idSolicitud" value="<%= s.getIdSolicitud() %>">
                                <input type="hidden" name="accion" value="aceptar">
                                <button type="submit" style="background: #27ae60; color: white; border: none; padding: 8px 15px; border-radius: 4px; cursor: pointer;">✅ Aceptar</button>
                            </form>
                            <form action="GestionarSolicitudServlet" method="POST">
                                <input type="hidden" name="idSolicitud" value="<%= s.getIdSolicitud() %>">
                                <input type="hidden" name="accion" value="rechazar">
                                <button type="submit" style="background: #e74c3c; color: white; border: none; padding: 8px 15px; border-radius: 4px; cursor: pointer;">❌ Rechazar</button>
                            </form>
                        </div>
                        <% } %>
                    </div>
                <% } %>
                </div>
            <% } %>

            <% if (!enviadas.isEmpty()) { %>
                <h2 style="color: #27ae60; border-top: 1px solid #eee; padding-top: 20px;">📤 Mis Solicitudes Enviadas (Eres Inquilino)</h2>
                <div style="margin-top: 20px;">
                <% for (Solicitud s : enviadas) { 
                       String colorEstado = "#f39c12"; // Naranja
                       if("aceptada".equals(s.getEstado())) colorEstado = "#27ae60"; // Verde
                       if("rechazada".equals(s.getEstado())) colorEstado = "#c0392b"; // Rojo
                %>
                    <div style="background: white; border-left: 5px solid <%= colorEstado %>; border-radius: 5px; padding: 20px; margin-bottom: 15px; box-shadow: 0 2px 5px rgba(0,0,0,0.05); display: flex; justify-content: space-between; align-items: center;">
                        <div style="display: flex; gap: 15px; align-items: center;">
                            <img src="<%= s.getImagenHabitacion() %>" style="width: 80px; height: 80px; object-fit: cover; border-radius: 5px;" onerror="this.src='img/sin_foto.jpg'">
                            <div>
                                <h3 style="margin: 0; color: #2c3e50;"><%= (s.getTituloHabitacion() != null) ? s.getTituloHabitacion() : "Habitación #" + s.getCodHabi() %></h3>
                                <p style="margin: 5px 0; color: #666;">
                                    📅 Fechas: <%= s.getFechaInicioPosibleAlquiler() %> a <%= s.getFechaFinPosibleAlquiler() %>
                                </p>
                                <p style="margin: 0;">
                                    Estado: <span style="color: white; background-color: <%= colorEstado %>; padding: 2px 8px; border-radius: 10px; font-size: 0.9em;"><%= s.getEstado().toUpperCase() %></span>
                                </p>
                            </div>
                        </div>
                        <div style="text-align: right; color: #7f8c8d; font-size: 0.9em;">
                            Solicitud #<%= s.getIdSolicitud() %>
                        </div>
                    </div>
                <% } %>
                </div>
            <% } %>

            <% if (recibidas.isEmpty() && enviadas.isEmpty()) { %>
                <div style="text-align: center; padding: 50px; background: white; border-radius: 8px; margin-top: 20px;">
                    <p style="color: #999; font-size: 1.2rem; margin-bottom: 10px;">📭 No tienes solicitudes activas.</p>
                    <p>Ni has recibido solicitudes de alquiler, ni has enviado ninguna todavía.</p>
                </div>
            <% } %>

        </section>

        

    </main>

    <script>
        function mostrarSeccion(id) {
            // Ocultar todas
            document.querySelectorAll('.panel-section').forEach(s => s.style.display = 'none');
            document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
            
            // Mostrar la elegida
            document.getElementById('sec-' + id).style.display = 'block';
            
            // Buscar el botón que se ha pulsado para ponerlo active (truco simple)
            event.target.classList.add('active');
        }
    </script>
    <script>
        <% 
           String pestana = (String) request.getAttribute("pestañaActiva");
           if (pestana != null) { 
        %>
            // Si el servidor nos dice qué pestaña abrir, la abrimos
            mostrarSeccion('<%= pestana %>');
        <% } %>
    </script>
   <form id="formOcultoMap" action="SolicitarAlquilerServlet" method="POST" style="display:none;">
    <input type="hidden" name="idHabitacion" id="inputHiddenId">
    <input type="hidden" name="fechaInicio" id="inputHiddenIni">
    <input type="hidden" name="fechaFin" id="inputHiddenFin">
</form>

<script>
    const habitacionesBD = [
    <% 
        ArrayList<Habitacion> listaMap = HabitacionDAO.obtenerTodas(); 
        String miEmailMap = (usuario != null) ? usuario.getEmail() : ""; 

        for (Habitacion h : listaMap) {
            // No mostrar mis propias casas
            if (usuario != null && h.getEmailPropietario().equals(miEmailMap)) continue; 
            
            // Solo las que tienen coordenadas válidas
            if (h.getLatitudH() != 0.0 && h.getLongitudH() != 0.0) {
    %>
        {
            "id": "<%= h.getCodHabi() %>",
            "ciudad": "<%= h.getCiudad() %>",
            "direccion": "<%= h.getDireccion() %>",
            "precio": "<%= h.getPrecioMes() %>",
            "lat": <%= h.getLatitudH() %>,
            "lng": <%= h.getLongitudH() %>,
            "img": "<%= h.getImagenHabitacion() %>",
            "propietario": "<%= h.getEmailPropietario() %>"
        },
    <% 
            }
        } 
    %>
    ];
</script>

<script src="js/mapa.js"></script>

<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyD8D2oQ_E8HFNuOXWteFWnH8TYPY7rX-ig&callback=initMap"></script>

</body>
</html>