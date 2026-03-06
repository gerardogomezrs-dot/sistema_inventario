package com.empresa.inventario.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;

public class ReportesUtils {

	public static void postProcessPDF(Object document, String tituloReporte) {
		System.err.println("Vengo del bean a solicitar exportar el archivo - PreProcess");
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
			e.printStackTrace();
		}
	}

	public static void exportarReporteExcel(Object document) {

		HSSFWorkbook wb = (HSSFWorkbook) document;
		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow header = sheet.getRow(0);

		HSSFCellStyle headerStyle = wb.createCellStyle();

		headerStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		headerStyle.setBorderBottom(BorderStyle.THIN);
		headerStyle.setBorderTop(BorderStyle.THIN);
		headerStyle.setBorderLeft(BorderStyle.THIN);
		headerStyle.setBorderRight(BorderStyle.THIN);

		HSSFFont font = wb.createFont();
		font.setBold(true);
		headerStyle.setFont(font);

		for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
			HSSFCell cell = header.getCell(i);
			cell.setCellStyle(headerStyle);

			sheet.autoSizeColumn(i);
		}

	}

}
