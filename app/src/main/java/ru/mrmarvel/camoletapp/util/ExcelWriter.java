package ru.mrmarvel.camoletapp.util;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;


import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelWriter {

    public XSSFWorkbook workbook;
    public XSSFWorkbook report;

    //    private String nameTemplate = "Готовности по типу";
    public HashMap<Percentage, String> PercentageEnumToString = new HashMap<Percentage, String>(){{
        put(Percentage.FLOOR_ROUGH, "% Пола без отделки");
        put(Percentage.FLOOR_PLASTER, "% Пола с черновой");
        put(Percentage.FLOOR_FINISH, "% Пола с чистовой");
        put(Percentage.CEILING_ROUGH, "% Потолка без отделки");
        put(Percentage.CEILING_PLASTER, "% Потолка с черновой");
        put(Percentage.CEILING_FINISH, "% Потолка с чистовой");
        put(Percentage.WALL_ROUGH, "% Стен без отделки");
        put(Percentage.WALL_PLASTER, "% Стен с черновой");
        put(Percentage.WALL_FINISH, "% Стен с чистовой");

        put(Percentage.MOP_FLOOR_ROUGH, "% Пола без отделки МОП");
        put(Percentage.MOP_FLOOR_PLASTER, "% Пола с черновой МОП");
        put(Percentage.MOP_FLOOR_FINISH, "% Пола с чистовой МОП");
        put(Percentage.MOP_CEILING_ROUGH, "% Потолка без отделки МОП");
        put(Percentage.MOP_CEILING_PLASTER, "% Потолка с черновой МОП");
        put(Percentage.MOP_CEILING_FINISH, "% Потолка с чистовой МОП");
        put(Percentage.MOP_WALL_ROUGH, "% Стен без отделки МОП");
        put(Percentage.MOP_WALL_PLASTER, "% Стен с черновой МОП");
        put(Percentage.MOP_WALL_FINISH, "% Стен с чистовой МОП");

        put(Percentage.DOORS, "% Дверей");
        put(Percentage.TRASH, "% Мусора");
        put(Percentage.SOCKET_SWITCH, "% Розеток и выключателей");
        put(Percentage.WINDOW, "% Отделки окон");
        put(Percentage.RADIATOR, "% Установки батарей");
        put(Percentage.KITCHEN_STUFF, "% Установки кухонь");
        put(Percentage.TOILET, "% Установки унитазов");
        put(Percentage.BATH, "% Установки ванн");
        put(Percentage.SINK, "% Установки раковин");
    }};

    public ExcelWriter() {
        this.workbook = new XSSFWorkbook();
    }

    public void readWorkbook(Context context){
//        BufferedReader reader = null;
        try{
//            reader = new BufferedReader(
//                    new InputStreamReader(context.getAssets().open("filename.txt")));
            InputStream file = context.getAssets().open("reportTemplate.xlsx");
            this.report = new XSSFWorkbook(file);
        } catch (Exception e){
            Log.d("READ ERROR", e.toString());
        }
    }

    public void fillReport(float[] data){
        XSSFSheet sheet = this.report.getSheet("ScoreMap");
        for(int i = 0; i < data.length; i++){
            Row row = sheet.getRow(i + 1);
            Cell cell = row.getCell(4);
            cell.setCellValue(data[i]);
        }
        saveWorkbook("report.xlsx", this.report);
    }

    public void fillSheet(Percentage sheetName, int numFloor, int numSection, int maxFloor, float percent) {
        numFloor = maxFloor - numFloor;
        numFloor++;
        numSection++;
        // CHECK IF SHEET EXISTS
        String sheetN = PercentageEnumToString.get(sheetName);
        XSSFSheet currentSheet = this.workbook.getSheet(sheetN);
        Log.d("MYDEBUG", "START");
        if (currentSheet == null) {
            currentSheet = this.workbook.createSheet(sheetN);
            Row sheetNameRow = currentSheet.createRow(0);
            Cell sheetNameCell = sheetNameRow.createCell(0);
            sheetNameCell.setCellValue(sheetN);
        }
        Log.d("MYDEBUG", "FIRST IF");
        // FILL CURRENT
        Row row = currentSheet.getRow(numFloor);
        if (row == null){
            row = currentSheet.createRow(numFloor);
        }
        Log.d("MYDEBUG", "SECOND IF");
        Cell floorCell =  row.createCell(0);
        floorCell.setCellValue(numFloor);

        Cell cell = row.createCell(numSection);
        cell.setCellValue(percent);

        Row sectionRow = currentSheet.getRow(maxFloor + 2);
        if (sectionRow == null){
            sectionRow = currentSheet.createRow(maxFloor + 2);
        }
        Log.d("MYDEBUG", "3 IF");
        Cell sectionCell =  sectionRow.createCell(numSection);
        sectionCell.setCellValue(numSection);
        Log.d("MYDEBUG", "END");
    }

    public void saveWorkbook(String filename, XSSFWorkbook workbook) {
        try {
            Log.d("MYDEBUG", "SAVING");
            File filePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + filename);
            //Write the workbook in file system
            FileOutputStream out = new FileOutputStream(filePath);
            workbook.write(out);
            out.close();
            Log.d("MYDEBUG", "SAVED");
            System.out.println("howtodoinjava_demo.xlsx written successfully on disk.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void createSheet() {
//        HSSFWorkbook workbook = new HSSFWorkbook();
//
//        //Create a blank sheet
//        HSSFSheet sheet = workbook.createSheet("Employee Data");
//
//        //This data needs to be written (Object[])
//        Map<String, Object[]> data = new TreeMap<String, Object[]>();
//        data.put("1", new Object[]{"ID", "NAME", "LASTNAME"});
//        data.put("2", new Object[]{1, "Amit", "Shukla"});
//        data.put("3", new Object[]{2, "Lokesh", "Gupta"});
//        data.put("4", new Object[]{3, "John", "Adwards"});
//        data.put("5", new Object[]{4, "Brian", "Schultz"});
//
//        //Iterate over data and write to sheet
//        Set<String> keyset = data.keySet();
//        int rownum = 0;
//        for (String key : keyset) {
//            Row row = sheet.createRow(rownum++);
//            Object[] objArr = data.get(key);
//            int cellnum = 0;
//            for (Object obj : objArr) {
//                Cell cell = row.createCell(cellnum++);
//                if (obj instanceof String)
//                    cell.setCellValue((String) obj);
//                else if (obj instanceof Integer)
//                    cell.setCellValue((Integer) obj);
//            }
//        }
//        try {
//            File filePath = new File(Environment.getExternalStorageDirectory() + "/Demo.xls");
//            //Write the workbook in file system
//            FileOutputStream out = new FileOutputStream(filePath);
//            workbook.write(out);
//            out.close();
//            System.out.println("howtodoinjava_demo.xlsx written successfully on disk.");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}

