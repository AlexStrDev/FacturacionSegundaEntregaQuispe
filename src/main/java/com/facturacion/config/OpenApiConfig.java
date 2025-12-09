package com.facturacion.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Facturación - Sistema de Comprobantes")
                        .version("1.0.0")
                        .description("API REST para gestión de facturas, clientes y productos con validaciones completas.\n\n" +
                                "Funcionalidades principales:\n" +
                                "- Gestión de clientes (CRUD completo)\n" +
                                "- Gestión de productos con control de stock\n" +
                                "- Creación de comprobantes con validaciones:\n" +
                                "  * Validación de cliente existente\n" +
                                "  * Validación de productos existentes\n" +
                                "  * Validación de stock disponible\n" +
                                "  * Reducción automática de stock\n" +
                                "  * Obtención de fecha desde servicio externo\n" +
                                "  * Cálculo automático de IGV y totales\n" +
                                "  * Preservación de precios históricos")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .email("contacto@facturacion.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desarrollo")
                ));
    }
}