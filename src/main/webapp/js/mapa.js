/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

/* * Lógica de Google Maps para VitoBadi16 */
// Variables globales para el mapa de BÚSQUEDA
let mapBusqueda;
let userMarker;
let searchCircle;
let roomMarkers = [];
let activeInfoWindow = null;

// Variables globales para el mapa de ALTA (Nueva habitación)
let mapAlta;
let markerAlta;

// --- FUNCIÓN PRINCIPAL DE INICIO (CALLBACK) ---
function initMap() {
    // 1. INICIALIZAR MAPA DE BÚSQUEDA (Si existe el div en la página)
    if (document.getElementById("map")) {
        iniciarMapaBusqueda();
    }

    // 2. INICIALIZAR MAPA DE ALTA (Si existe el div en la página)
    if (document.getElementById("mapAlta")) {
        iniciarMapaAlta();
    }
}

// -----------------------------------------------------------------------------
// LÓGICA A: MAPA DEL BUSCADOR (El que muestra habitaciones disponibles)
// -----------------------------------------------------------------------------
function iniciarMapaBusqueda() {
    const centroVitoria = { lat: 42.8467, lng: -2.6716 }; 

    mapBusqueda = new google.maps.Map(document.getElementById("map"), {
        zoom: 13,
        center: centroVitoria
    });

    mapBusqueda.addListener("click", function(event) {
        actualizarPosicionUsuario(event.latLng);
    });

    // Slider de radio
    const slider = document.getElementById('geo-radio');
    if(slider) {
        slider.addEventListener('input', function() {
            document.getElementById('val-radio').textContent = this.value;
            if (userMarker) actualizarRadio(this.value);
        });
    }
}

function actualizarPosicionUsuario(latLng) {
    if (userMarker) {
        userMarker.setPosition(latLng);
    } else {
        userMarker = new google.maps.Marker({
            position: latLng,
            map: mapBusqueda,
            title: "Tu ubicación",
            icon: "http://maps.google.com/mapfiles/ms/icons/blue-dot.png" // Punto azul según requisito [cite: 38]
        });
    }
    const radioKm = parseFloat(document.getElementById('geo-radio').value);
    actualizarRadio(radioKm, latLng);
}

function actualizarRadio(km, latLng) {
    if (!latLng && userMarker) latLng = userMarker.getPosition();
    const radioMetros = km * 1000;

    if (searchCircle) {
        searchCircle.setCenter(latLng);
        searchCircle.setRadius(radioMetros);
    } else {
        searchCircle = new google.maps.Circle({
            strokeColor: "#3498db", strokeOpacity: 0.8, strokeWeight: 2,
            fillColor: "#3498db", fillOpacity: 0.20,
            map: mapBusqueda, center: latLng, radius: radioMetros
        });
    }
    filtrarYMostrarHabitaciones(latLng.lat(), latLng.lng(), km);
}

function filtrarYMostrarHabitaciones(latUser, lngUser, radioKm) {
    // Limpiar marcadores anteriores
    roomMarkers.forEach(m => m.setMap(null));
    roomMarkers = [];

    // 'habitacionesBD' se define en el JSP (Variables globales)
    if (typeof habitacionesBD !== 'undefined') {
        habitacionesBD.forEach(h => {
            const dist = getDistanceFromLatLonInKm(latUser, lngUser, h.lat, h.lng);
            if (dist <= radioKm) {
                crearMarcadorHabitacion(h);
            }
        });
    }
}

function crearMarcadorHabitacion(h) {
    // Recuperar ID de forma segura
    var idSeguro = h.id || h.idHabitacion || h.ID || "";
    
    var marker = new google.maps.Marker({
        position: { lat: parseFloat(h.lat), lng: parseFloat(h.lng) },
        map: mapBusqueda,
        title: h.ciudad || "Habitación",
        animation: google.maps.Animation.DROP
    });

    // Validar imagen por si viene vacía
    var rutaImagen = (h.img && h.img.trim() !== "") ? h.img : "img/no-foto.jpg";

    var contenidoInfo = 
        '<div style="background-color: white; padding: 5px; min-width: 220px; max-width: 250px;">' +
            
            // --- AQUI ESTÁ EL CAMBIO: LA FOTO ---
            '<div style="height: 120px; width: 100%; margin-bottom: 8px; border-radius: 4px; overflow: hidden; background-color: #eee;">' +
                '<img src="' + rutaImagen + '" ' +
                     'style="width: 100%; height: 100%; object-fit: cover;" ' +
                     'onerror="this.src=\'img/no-foto.jpg\'">' + // Si falla la carga, pone una por defecto
            '</div>' +
            // ------------------------------------

            '<h3 style="color: #2c3e50; margin: 0 0 5px 0; font-size: 16px; font-weight: bold;">' + (h.ciudad || "") + '</h3>' +
            '<p style="color: #555; margin: 2px 0; font-size: 13px;">' + (h.direccion || "") + '</p>' +
            '<p style="color: #27ae60; font-weight: bold; font-size: 14px; margin: 5px 0;">' + (h.precio || 0) + ' €/mes</p>' +
            
            '<hr style="border: 0; border-top: 1px solid #eee; margin: 8px 0;">' +
            
            '<button type="button" onclick="prepararSolicitud(\'' + idSeguro + '\')" ' +
                'style="background-color: #e74c3c; color: white; border: none; padding: 8px 15px; cursor: pointer; width: 100%; border-radius: 4px; font-weight: bold;">' +
                'Solicitar Visita' +
            '</button>' +
        '</div>';

    var infoWindow = new google.maps.InfoWindow({ content: contenidoInfo });

    marker.addListener("click", function() {
        if (activeInfoWindow) activeInfoWindow.close();
        infoWindow.open(mapBusqueda, marker);
        activeInfoWindow = infoWindow;
    });

    roomMarkers.push(marker);
}

// -----------------------------------------------------------------------------
// LÓGICA B: MAPA DE ALTA (Para seleccionar lat/long al crear habitación)
// -----------------------------------------------------------------------------
function iniciarMapaAlta() {
    const centroDefecto = { lat: 42.8467, lng: -2.6716 }; // Vitoria

    mapAlta = new google.maps.Map(document.getElementById("mapAlta"), {
        zoom: 13,
        center: centroDefecto,
        streetViewControl: false
    });

    // Crear un marcador inicial que se pueda mover
    markerAlta = new google.maps.Marker({
        position: centroDefecto,
        map: mapAlta,
        draggable: true, // Permitir arrastrar
        title: "Arrastrame o haz click para ubicar la casa"
    });

    // Evento 1: Al hacer click en el mapa
    mapAlta.addListener("click", function(event) {
        moverMarcadorAlta(event.latLng);
    });

    // Evento 2: Al terminar de arrastrar el marcador
    markerAlta.addListener("dragend", function(event) {
        actualizarInputsFormulario(event.latLng);
    });
    
    // Inicializar inputs con la posición por defecto
    actualizarInputsFormulario(centroDefecto);
}

function moverMarcadorAlta(latLng) {
    markerAlta.setPosition(latLng);
    actualizarInputsFormulario(latLng);
}

function actualizarInputsFormulario(latLng) {
    // Buscamos los inputs del formulario por su atributo 'name'
    const inputLat = document.querySelector('input[name="latitud"]');
    const inputLng = document.querySelector('input[name="longitud"]');

    // Usamos toFixed(4) según requisito  (Double con 4 decimales)
    // latLng puede venir como objeto Google o como objeto literal {lat:..., lng:...}
    let lat = (typeof latLng.lat === 'function') ? latLng.lat() : latLng.lat;
    let lng = (typeof latLng.lng === 'function') ? latLng.lng() : latLng.lng;

    if (inputLat) inputLat.value = lat.toFixed(4);
    if (inputLng) inputLng.value = lng.toFixed(4);
}

// -----------------------------------------------------------------------------
// UTILIDADES / AUXILIARES
// -----------------------------------------------------------------------------
function prepararSolicitud(idHabitacion) {
    if (!idHabitacion) return;
    
    const inputFechaIni = document.querySelector('input[name="busqFechaIni"]');
    const inputFechaFin = document.querySelector('input[name="busqFechaFin"]');
    
    if (!inputFechaIni || !inputFechaFin || !inputFechaIni.value || !inputFechaFin.value) {
        alert("⚠️ Por favor, introduce las fechas (Desde/Hasta) en el buscador de arriba para continuar.");
        window.scrollTo({ top: 0, behavior: 'smooth' });
        return;
    }

    document.getElementById('inputHiddenId').value = idHabitacion;
    document.getElementById('inputHiddenIni').value = inputFechaIni.value;
    document.getElementById('inputHiddenFin').value = inputFechaFin.value;
    document.getElementById('formOcultoMap').submit();
}

// Fórmula de Haversine
function getDistanceFromLatLonInKm(lat1, lon1, lat2, lon2) {
    const R = 6371; 
    const dLat = deg2rad(lat2 - lat1);
    const dLon = deg2rad(lon2 - lon1);
    const a = Math.sin(dLat/2) * Math.sin(dLat/2) +
              Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.sin(dLon/2) * Math.sin(dLon/2); 
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
    return R * c;
}
function deg2rad(deg) { return deg * (Math.PI/180); }
