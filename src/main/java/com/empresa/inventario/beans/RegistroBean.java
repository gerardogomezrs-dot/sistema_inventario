package com.empresa.inventario.beans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class RegistroBean implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nombre;
    private List<String> usuarios = new ArrayList<>();

    public void guardar() {
        if (nombre != null && !nombre.trim().isEmpty()) {
            usuarios.add(nombre);
            nombre = ""; // Limpia el campo después de guardar
        }
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public List<String> getUsuarios() { return usuarios; }
}