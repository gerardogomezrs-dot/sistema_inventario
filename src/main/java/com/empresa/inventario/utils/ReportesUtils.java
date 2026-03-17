package com.empresa.inventario.utils;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;

public class ReportesUtils {

	public static void postProcessPDF(Object document, String tituloReporte) {
		Document pdf = (Document) document;

		pdf.setPageSize(PageSize.A4);

		if (!pdf.isOpen()) {
			pdf.open();
		}

		try {
			Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);

			Paragraph title = new Paragraph(tituloReporte, fuenteTitulo);
			title.setAlignment(Element.ALIGN_CENTER);

			title.setSpacingAfter(30);

			pdf.add(title);

		} catch (Exception e) {
			e.getMessage();
		}
	}

	public static void exportarReporteExcel(Object document) {
		// PrimeFaces envía un XSSFWorkbook cuando usas type="xlsx"
		Workbook wb = (Workbook) document;
		Sheet sheet = wb.getSheetAt(0);
		Row header = sheet.getRow(0);

		// Creamos el estilo usando la interfaz genérica 'CellStyle' para mayor
		// compatibilidad
		CellStyle headerStyle = wb.createCellStyle();

		// Color de fondo (Gris claro)
		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		// Bordes delgados
		headerStyle.setBorderBottom(BorderStyle.THIN);
		headerStyle.setBorderTop(BorderStyle.THIN);
		headerStyle.setBorderLeft(BorderStyle.THIN);
		headerStyle.setBorderRight(BorderStyle.THIN);

		org.apache.poi.ss.usermodel.Font font = wb.createFont();
		font.setBold(true);
		headerStyle.setFont(font);

		if (header != null) {
			for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
				Cell cell = header.getCell(i);
				cell.setCellStyle(headerStyle);

				sheet.autoSizeColumn(i);
			}
		}
	}
}
