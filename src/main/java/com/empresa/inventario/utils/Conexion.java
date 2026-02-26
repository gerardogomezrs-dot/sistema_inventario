package com.empresa.inventario.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.empresa.inventario.exceptions.ExceptionMessage;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class Conexion {

	private static final Properties properties = new Properties();

	static {
		try (InputStream input = Conexion.class.getClassLoader().getResourceAsStream("config.properties")) {
			if (input == null) {
				throw new ExceptionMessage("Lo siento, no se pudo encontrar el archivo config.properties");
			} else {
				properties.load(input);
			}
		} catch (Exception ex) {
			ex.getMessage();
		}
	}

	public static Connection getConexion() {
		String url = properties.getProperty("db.url");
		String user = properties.getProperty("db.user");
		String pass = properties.getProperty("db.pass");

		try {

			Class.forName("com.mysql.cj.jdbc.Driver");

			return DriverManager.getConnection(url, user, pass);

		} catch (ClassNotFoundException | SQLException e) {

			throw new ExceptionMessage("Error al conectar con la base de datos" + e);
		}
	}
}
