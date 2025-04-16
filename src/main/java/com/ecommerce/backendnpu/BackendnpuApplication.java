package com.ecommerce.backendnpu;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;


@SpringBootApplication
public class BackendnpuApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendnpuApplication.class, args);
	}

	@Bean
	CommandLineRunner checkDatabase(DataSource dataSource) {
		return args -> {
			try {
				System.out.println("🔍 URL de conexión REAL: " + dataSource.getConnection().getMetaData().getURL());
				System.out.println("🔍 Usuario REAL: " + dataSource.getConnection().getMetaData().getUserName());
			} catch (Exception e) {
				System.err.println("❌ Error al obtener conexión: " + e.getMessage());
			}
		};
	}

}
