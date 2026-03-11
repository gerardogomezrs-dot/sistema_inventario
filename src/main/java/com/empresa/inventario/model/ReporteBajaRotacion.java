package com.empresa.inventario.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor 
@AllArgsConstructor 
public class ReporteBajaRotacion implements Serializable {
    private static final long serialVersionUID = 1L;
    private String codigoBarras;
    private String nombre;
    private Integer stockActual;
    private Date ultimaSalida;
}