package com.empresa.inventario.beans;

import java.io.Serializable;

import javax.inject.Named;

import com.empresa.inventario.model.Categorias;

import lombok.Data;

@Named("categoriasAlmacenBean")
@javax.faces.view.ViewScoped
@Data
public class CategoriasAlmacenBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Categorias categorias;
	
}
