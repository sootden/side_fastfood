package com.ponyo.fastfood.util;

import lombok.Cleanup;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

@Slf4j
@UtilityClass
public class ExcelHelper {
    public static String filePath = "/Users/sueun/side/fastfood_file";
    public void crtExcel(List<LinkedHashMap<String, String>> data, HashMap excelInfo){
        try{
            XSSFWorkbook workbook= new XSSFWorkbook();

            String sheetName = (String) excelInfo.get("sheet");
            @Cleanup FileOutputStream out = new FileOutputStream(new File(filePath,sheetName));
            XSSFSheet sheet = workbook.createSheet();
            workbook.setSheetName(0,sheetName);
            XSSFRow row;
            XSSFCell cell;

            //Header
            row = sheet.createRow(0);
            CellStyle cellHeaderStyle = getCellStyle(sheet);
            cellHeaderStyle.setBorderBottom(BorderStyle.THICK);
            setBorder(cellHeaderStyle, BorderStyle.THIN);
            cellHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellHeaderStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
            Font font = getFont(workbook);
            font.setBold(true);
            cellHeaderStyle.setFont(font);

            CellStyle cellHyperlinkStyle =  workbook.createCellStyle();
            Font hyperlinkFont = workbook.createFont();
            hyperlinkFont.setUnderline(Font.U_SINGLE);
            hyperlinkFont.setColor(IndexedColors.BLUE.index);
            cellHyperlinkStyle.setFont(hyperlinkFont);

            String[] headers = (String[])excelInfo.get("headers");
            for(int i = 0; i< headers.length;i++){
                cell = row.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(cellHeaderStyle);
            }

            int rowIdx = 1;
            for (LinkedHashMap<String,String> st : data) {
                row = sheet.createRow(rowIdx++);
                int idx = 0;
                for(String key : st.keySet()){
                    row.createCell(idx++).setCellValue(st.get(key));
                }
            }

            for(int i =0; i<headers.length; i++){
                sheet.autoSizeColumn(i,true);
            }

            workbook.write(out);
        }catch (IOException e){
            log.error("fail to import data to Excel file: {}",e.getMessage());
        }
    }

    public void writeExcel(List<LinkedHashMap<String, String>> data, HashMap excelInfo){
        try {
            String fileNm = (String) excelInfo.get("sheet");
            String[] headers = (String[])excelInfo.get("headers");
            File storeFile = new File(filePath, fileNm);

            if(storeFile.exists()){
                FileInputStream file = new FileInputStream(storeFile);
                XSSFWorkbook workbook = new XSSFWorkbook(file);
                XSSFSheet sheet = workbook.getSheetAt(0);
                int rowIdx = sheet.getLastRowNum() + 1;
                XSSFCell cell;

                for (LinkedHashMap<String,String> st : data) {
                    XSSFRow row = sheet.createRow(rowIdx++);
                    int idx = 0;
                    for(String key : st.keySet()){
                        row.createCell(idx++).setCellValue(st.get(key));
                    }
                }

                for(int i =0; i<headers.length; i++){
                    sheet.autoSizeColumn(i,true);
                }
                file.close();
                FileOutputStream out = new FileOutputStream(storeFile);
                workbook.write(out);
                out.close();
                workbook.close();
            }else{
                crtExcel(data,excelInfo);
            }
        } catch (IOException e) {
            log.error("fail to import data to Excel file: {}",e.getMessage());
        }
    }
    private CellStyle getCellStyle(XSSFSheet sheet){
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        return cellStyle;
    }
    private Font getFont(XSSFWorkbook workbook) {
        Font font = workbook.createFont();
        return font;
    }
    private void setBorder(CellStyle style, BorderStyle border) {
        style.setBorderBottom(border);
        style.setBorderTop(border);
        style.setBorderLeft(border);
        style.setBorderRight(border);
    }
}
