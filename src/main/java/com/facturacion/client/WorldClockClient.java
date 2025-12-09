package com.facturacion.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class WorldClockClient {

    private static final Logger logger = LoggerFactory.getLogger(WorldClockClient.class);
    private static final String WORLD_CLOCK_API = "http://worldclockapi.com/api/json/utc/now";

    private final RestTemplate restTemplate;

    public WorldClockClient() {
        this.restTemplate = new RestTemplate();
    }

    public LocalDateTime obtenerFechaActual() {
        try {
            logger.info("Intentando obtener fecha desde WorldClock API");
            WorldClockResponse response = restTemplate.getForObject(WORLD_CLOCK_API, WorldClockResponse.class);

            if (response != null && response.getCurrentDateTime() != null) {
                // Parsear la fecha en formato: "2024-12-08T12:30:00Z"
                String fechaStr = response.getCurrentDateTime();
                if (fechaStr.endsWith("Z")) {
                    fechaStr = fechaStr.substring(0, fechaStr.length() - 1);
                }
                LocalDateTime fecha = LocalDateTime.parse(fechaStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                logger.info("Fecha obtenida exitosamente desde WorldClock API: {}", fecha);
                return fecha;
            }
        } catch (Exception e) {
            logger.warn("Error al obtener fecha desde WorldClock API: {}. Usando fecha local.", e.getMessage());
        }

        // Si falla, usar fecha local del sistema
        LocalDateTime fechaLocal = convertirDateALocalDateTime(new Date());
        logger.info("Usando fecha local del sistema: {}", fechaLocal);
        return fechaLocal;
    }

    private LocalDateTime convertirDateALocalDateTime(Date date) {
        return new java.sql.Timestamp(date.getTime()).toLocalDateTime();
    }

    // Clase interna para mapear la respuesta de la API
    private static class WorldClockResponse {
        private String currentDateTime;

        public WorldClockResponse() {
        }

        public String getCurrentDateTime() {
            return currentDateTime;
        }

        public void setCurrentDateTime(String currentDateTime) {
            this.currentDateTime = currentDateTime;
        }
    }
}