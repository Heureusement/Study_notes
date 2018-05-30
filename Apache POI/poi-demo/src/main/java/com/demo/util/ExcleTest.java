package com.demo.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcleTest {
    public static final String DEST = "D://testExcle.xls";
    public static void createExcle(){
        Workbook workbook = new HSSFWorkbook();
        workbook.createSheet("test");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(DEST);
            workbook.write(fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
