package com.empresa.inventario.utils;

public enum Mensajes {

	ERROR("ERROR"),
	INFO("INFO"),
	NAVEGACION("Navegacion"),
	USUARIO("El usuario: "),
	MODULO_PRODUCTOS("Modulo Productos"),
	MODULO_PROVEEDORES("Modulo Proveedores"),
	MODULO_USUARIOS("Modulo Usuarios"),
	MODULO_REPORTES("MOdulo Reportes"),
	MODULO_MOVIMIENTOS("Modulo movimientos"),
	MODULO_CATEGORIAS("Modulo Categorias"),
	EXPORTAR_REPORTE("Exportar Reporte"),
	GESTION_AUDITORIAS("Gestion Auditoria"),
	PERFIL_USUARIO("Perfil Usuario");

	
	private String texto = "";
	
	Mensajes(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }
	
	
}
