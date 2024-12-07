package com.mna.springbootsecurity.file.util;

import com.mna.springbootsecurity.file.enums.ExcelType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.util.Pair;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    private ExcelUtil() {
    }

    // Method to read data from Excel file
    public static List<List<String>> readExcel(InputStream inputStream) throws IOException {
        List<List<String>> data = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0); // Get the first sheet
            for (Row row : sheet) {
                List<String> rowData = new ArrayList<>();
                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        case STRING:
                            rowData.add(cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            rowData.add(String.valueOf(cell.getNumericCellValue()));
                            break;
                        case BOOLEAN:
                            rowData.add(String.valueOf(cell.getBooleanCellValue()));
                            break;
                        case FORMULA:
                            rowData.add(cell.getCellFormula());
                            break;
                        default:
                            rowData.add("");
                            break;
                    }
                }
                data.add(rowData);
            }
        }
        return data;
    }

    public static <T> List<T> mapExcelToVO(InputStream inputStream, Class<T> dtoClass) throws Exception {
        List<T> dtoList = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0); // Get the first sheet
            Field[] fields = dtoClass.getDeclaredFields(); // Get all fields of the DTO class

            for (int i = 1; i <= sheet.getPhysicalNumberOfRows(); i++) { // Skip header row
                Row row = sheet.getRow(i);
                if (row != null) {
                    T dto = dtoClass.getDeclaredConstructor().newInstance(); // Create a new instance of DTO

                    for (int j = 0; j < fields.length; j++) {
                        Cell cell = row.getCell(j);
                        if (cell != null) {
                            Field field = fields[j];
                            field.setAccessible(true);

                            // Set field value based on cell type
                            switch (cell.getCellType()) {
                                case STRING:
                                    field.set(dto, cell.getStringCellValue());
                                    break;
                                case NUMERIC:
                                    if (field.getType() == int.class) {
                                        field.set(dto, (int) cell.getNumericCellValue());
                                    } else if (field.getType() == double.class) {
                                        field.set(dto, cell.getNumericCellValue());
                                    }
                                    break;
                                case BOOLEAN:
                                    field.set(dto, cell.getBooleanCellValue());
                                    break;
                                default:
                                    field.set(dto, null);
                            }
                        }
                    }

                    dtoList.add(dto);
                }
            }
        }
        return dtoList;
    }

    // Method to write data to Excel file
    public static void writeExcel(List<List<String>> data, OutputStream outputStream) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Sheet1");
            for (int rowIndex = 0; rowIndex < data.size(); rowIndex++) {
                Row row = sheet.createRow(rowIndex);
                List<String> rowData = data.get(rowIndex);
                for (int colIndex = 0; colIndex < rowData.size(); colIndex++) {
                    Cell cell = row.createCell(colIndex);
                    cell.setCellValue(rowData.get(colIndex));
                }
            }
            workbook.write(outputStream);
        }
    }

    // Method to create a sample Excel file
    public static void createSampleExcel(OutputStream outputStream) throws IOException {
        List<List<String>> sampleData = new ArrayList<>();
        List<String> header = List.of("Name", "Age", "Email");
        List<String> row1 = List.of("Alice", "30", "alice@example.com");
        List<String> row2 = List.of("Bob", "25", "bob@example.com");
        sampleData.add(header);
        sampleData.add(row1);
        sampleData.add(row2);

        writeExcel(sampleData, outputStream);
    }

    // Method to convert Excel data to a List of Strings (e.g., first column)
    public static List<String> extractColumn(List<List<String>> data, int columnIndex) {
        List<String> columnData = new ArrayList<>();
        for (List<String> row : data) {
            if (row.size() > columnIndex) {
                columnData.add(row.get(columnIndex));
            }
        }
        return columnData;
    }

    // Create a Workbook from a File
    public static Workbook createWorkbook(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return new XSSFWorkbook(fis);
        }
    }

    // Validate the Excel file
    public static String validate(String fileName, Workbook workbook, int templateColumnCount) {
        if (fileName == null || !fileName.endsWith("." + ExcelType.XLSX.name())) {
            return "Invalid Excel file";
        }

        Pair<Integer, Integer> excelMetaData = getExcelMetaData(workbook);
        if (excelMetaData.getFirst() <= 0) {
            return "Empty Excel file";
        } else if (excelMetaData.getSecond() != templateColumnCount) {
            return "Column count doesn't match";
        }

        return "VALID";
    }

    // Get metadata from the Excel Workbook
    public static Pair<Integer, Integer> getExcelMetaData(Workbook workbook) {
        int totalRecords = 0;
        int totalColumns = 0;

        if (workbook.getNumberOfSheets() > 0) {
            XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
            totalRecords = sheet.getPhysicalNumberOfRows() - 1; // Exclude header row
            totalColumns = sheet.getRow(0) != null ? sheet.getRow(0).getPhysicalNumberOfCells() : 0;
        }

        return Pair.of(totalRecords, totalColumns);
    }
}
