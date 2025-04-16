package com.ecommerce.backendnpu;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;


@SpringBootApplication
public class BackendnpuApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendnpuApplication.class, args);
	}

	@Bean
	CommandLineRunner checkDatabase(DataSource dataSource, Environment env) {
		return args -> {
			try {
				System.out.println("🔍 URL de conexión: " + dataSource.getConnection().getMetaData().getURL());
				System.out.println("👤 Usuario de DB: " + dataSource.getConnection().getMetaData().getUserName());
				System.out.println("🌱 Perfil activo: " + String.join(", ", env.getActiveProfiles()));
			} catch (Exception e) {
				System.err.println("❌ Error al obtener conexión: " + e.getMessage());
			}
		};
	}




	@Bean
	CommandLineRunner measureStartupTime() {
		return args -> {
			long start = System.currentTimeMillis();
			// Simula tareas de inicialización...
			long end = System.currentTimeMillis();
			System.out.println("⏱️ Tiempo de arranque aproximado: " + (end - start) + " ms");
		};
	}




}
