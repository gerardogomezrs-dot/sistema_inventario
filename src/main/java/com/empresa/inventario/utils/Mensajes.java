package com.empresa.inventario.utils;

public enum Mensajes {

	ERROR("ERROR"),
	INFO("INFO"),
	WARN("WARN"),
	NAVEGACION("Navegacion"),
	USUARIO("El usuario: "),
	MODULO_PRODUCTOS("Modulo Productos"),
	MODULO_PROVEEDORES("Modulo Proveedores"),
	MODULO_USUARIOS("Modulo Usuarios"),
	MODULO_REPORTES("MOdulo Reportes"),
	MODULO_MOVIMIENTOS("Modulo movimientos"),
	MODULO_CATEGORIAS("Modulo Categorias"),
	MODULO_MERMAS_DEVOLUCIONES("Modulo Mermas Devoluciones"),
	EXPORTAR_REPORTE("Exportar Reporte"),
	GESTION_AUDITORIAS("Gestion Auditoria"),
	PERFIL_USUARIO("Perfil Usuario"),
	GUARDAR("Guardar"),
	ELIMINAR("Eliminar"),
	ACTUALIZAR("Actualizar"),
	CONSULTA("Consulta"),
	GUARDAR_REGISTRO_TABLA("Guardar Registro en Tabla"),
	CARGA_MASIVA_REGISTROS("Carga Masiva de Registros"),
	NUEVO_REGISTRO("Nuevo registro");

	
	private String texto = "";
	
	Mensajes(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }
	
	
}
