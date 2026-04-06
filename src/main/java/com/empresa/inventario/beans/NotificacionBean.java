package com.empresa.inventario.beans;

import java.io.Serializable;
import javax.faces.view.ViewScoped; 

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import com.empresa.inventario.model.Productos;
import com.empresa.inventario.service.IProductoService;

import lombok.Data;

@Named("notificacionBean")
@ViewScoped
@Data
public class NotificacionBean implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private transient IProductoService iProductoService;
    
    private transient List<Productos> listaProductosCriticos;
    
    private transient List<Productos> listaProductosAgotados;

    private int cantidadCriticos;
    
    private int cantidadAgotados; 
    
    private boolean mostrarAlerta = true; 
    
    private boolean mostrarAlertaSinStock = true; 

    @Inject
    public NotificacionBean(IProductoService iProductoService) {
        this.iProductoService = iProductoService;
    }
    
    @PostConstruct
    public void init() {
        cargarAlertas();
        this.mostrarAlerta = true;
        this.mostrarAlertaSinStock = true;
    }
    
    public void cargarAlertas() {
        this.listaProductosCriticos = iProductoService.getStockBajo();
        this.cantidadCriticos = listaProductosCriticos.size();
        
        this.listaProductosAgotados = iProductoService.sinExistencias();
        this.cantidadAgotados = (listaProductosAgotados != null) ? listaProductosAgotados.size() : 0;
    }
    
    public void ocultarAlerta() {
        this.mostrarAlerta = false;
    }

    public void ocultarAlertaSinStock() {
        this.mostrarAlertaSinStock = false;
    }
}