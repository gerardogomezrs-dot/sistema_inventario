package com.empresa.inventario.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.empresa.inventario.mapper.UbicacionMapper;
import com.empresa.inventario.model.Ubicacion;
import com.empresa.inventario.utils.Conexion;

public class UbicacionDAO {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UbicacionDAO.class);
	private UbicacionMapper mapper = new UbicacionMapper();

	public List<Ubicacion> getAll() {
		String sql = "SELECT u.* from ubicacion u";
		List<Ubicacion> lista = new ArrayList<>();
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Ubicacion p = new Ubicacion();
				p = mapper.getRow(rs);
				lista.add(p);
			}
		} catch (SQLException e) {
			logger.debug(e.getMessage());
		}
		return lista;
	}

	public void guardar(Ubicacion ubicacion) {

		String sql = "INSERT INTO inventarios.ubicacion\r\n"
				+ "(pasillo, estante, nivel, codigo_control, estado)\r\n" + "VALUES( ?, ?, ?, ?, ?);";
		try (Connection conexion = Conexion.getConexion(); PreparedStatement ps = conexion.prepareStatement(sql);) {

			ps.setString(1, ubicacion.getPasillo());
			ps.setString(2, ubicacion.getEstante());
			ps.setString(3, ubicacion.getNivel());
			ps.setString(4, ubicacion.getCodigoControl());
			ps.setString(5, ubicacion.getEstado());
			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		}
	}

	public void actualizar(Ubicacion e) {
		String sql = "UPDATE inventarios.ubicacion\r\n"
				+ "SET pasillo=?, estante=?, nivel=?, codigo_control=?, estado=?\r\n" + "WHERE id_ubicacion= ?;";
		try (Connection conexion = Conexion.getConexion(); PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setString(1, e.getPasillo());
			ps.setString(2, e.getEstante());
			ps.setString(3, e.getNivel());
			ps.setString(4, e.getCodigoControl());
			ps.setString(5, e.getEstado());
			ps.setInt(6, e.getIdUbicacion());
			ps.executeUpdate();
		} catch (SQLException ex) {
			logger.debug(ex.getMessage());
		}
	}

	public boolean eliminar(int id) {
	    String sql = "DELETE FROM ubicacion WHERE id_ubicacion = ?";
	    
	    try (Connection conexion = Conexion.getConexion(); 
	         PreparedStatement ps = conexion.prepareStatement(sql)) {
	        ps.setInt(1, id);
	        int filasAfectadas = ps.executeUpdate();
	        return filasAfectadas > 0;
	    } catch (SQLException ex) {
	        logger.error("Error al eliminar ubicación: " + ex.getMessage(), ex);
	        return false;
	    }
	}
}
