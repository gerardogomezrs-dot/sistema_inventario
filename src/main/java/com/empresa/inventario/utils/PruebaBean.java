package com.empresa.inventario.utils;

import javax.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

@Named
@ViewScoped
public class PruebaBean implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String codigoTemp;
    private List<String> listaEscaneados = new ArrayList<>();

    public void procesarEscaneo() {
        if (this.codigoTemp != null && !this.codigoTemp.trim().isEmpty()) {
            // Añadimos a la lista para mostrar que funcionó
            listaEscaneados.add(0, codigoTemp); // El más reciente arriba
            
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Escaneado", "Código: " + codigoTemp));
            
            // Limpiamos el campo para la siguiente lectura
            this.codigoTemp = "";
        }
    }

    // Getters y Setters
    public String getCodigoTemp() { return codigoTemp; }
    public void setCodigoTemp(String codigoTemp) { this.codigoTemp = codigoTemp; }
    public List<String> getListaEscaneados() { return listaEscaneados; }
}