<%-- 
    Document   : index
    Created on : 3 ene 2026, 1:16:29
    Author     : David
--%>

<%-- PÁGINA DE LANZAMIENTO --%>
<%-- Su única función es redirigir al Dashboard automáticamente --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    response.sendRedirect("Dashboard.jsp");
%>