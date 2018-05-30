package com.demo.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Date;

public class ExcleUtil {
    public static final String DEST = "D://";

    /**
     * 创建Excle文件
     */
    public static void createExcles(){
        Workbook[] workbooks = new Workbook[]{new HSSFWorkbook(), new XSSFWorkbook()};
        for(int i=0; i<workbooks.length; i++){
            Workbook workbook = workbooks[i];
            // 获取一个POI的工具类
            CreationHelper creationHelper = workbook.getCreationHelper();
            // 创建sheet
            Sheet sheet = workbook.createSheet("test" + i);
            // 格式化单元格数据
            DataFormat dataFormat = workbook.createDataFormat();
            // 设置字体
            Font font = workbook.createFont();
            font.setFontHeightInPoints((short) 20); // 字体高度
            font.setColor(Font.COLOR_RED); // 字体颜色
            font.setFontName("黑体"); // 字体
            font.setBoldweight(Font.BOLDWEIGHT_BOLD); // 宽度
            font.setItalic(true); // 是否斜体
            font.setStrikeout(false); // 是否使用下划线

            // 设置单元格类型
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(font);
            cellStyle.setAlignment(CellStyle.ALIGN_CENTER); // 布局：水平居中
            cellStyle.setWrapText(true); // 自动换行

            CellStyle cellStyle2 = workbook.createCellStyle();
            cellStyle2.setDataFormat(dataFormat.getFormat("＃, ## 0.0"));

            CellStyle cellStyle3 = workbook.createCellStyle();
            cellStyle3.setDataFormat(dataFormat.getFormat("yyyy-MM-dd HH:mm:ss"));

            // 添加单元格注释
            // 创建Drawing对象,Drawing是所有注释的容器
            Drawing drawing = sheet.createDrawingPatriarch();
            // ClientAnchor是附属在WorkSheet上的一个对象， 其固定在一个单元格的左上角和右下角
            ClientAnchor clientAnchor = creationHelper.createClientAnchor();

            // 设置注释位置
            clientAnchor.setRow1(0);
            clientAnchor.setRow2(2);
            clientAnchor.setCol1(0);
            clientAnchor.setCol2(2);
            // 定义注释的大小和位置
            Comment comment = drawing.createCellComment(clientAnchor);
            // 设置注释内容
            RichTextString str = creationHelper.createRichTextString("Hello.");
            comment.setString(str);
            // 设置注释作者
            comment.setAuthor("DY");

            // 定义几行数据
            for(int rowNum = 0; rowNum < 30; rowNum++){
                // 创建行
                Row row = sheet.createRow(rowNum);
                // 创建单元格
                Cell cell = row.createCell(1);
                cell.setCellValue(creationHelper.createRichTextString("Test, NO" + rowNum));
                cell.setCellStyle(cellStyle); // 设置单元格格式
                cell.setCellType(Cell.CELL_TYPE_STRING);// 指定单元格格式：数字、字符串、公式等
                cell.setCellComment(comment);

                // 格式化数据
                Cell cell2 = row.createCell(2);
                cell2.setCellValue(1234.56);
                cell2.setCellStyle(cellStyle2);

                Cell cell3 = row.createCell(3);
                cell3.setCellValue(new Date());
                cell3.setCellStyle(cellStyle3);

                // 调整列宽
                sheet.autoSizeColumn(0);
                sheet.autoSizeColumn(1);
                sheet.autoSizeColumn(2);
                sheet.autoSizeColumn(3);
            }
            // 合并单元格
            sheet.addMergedRegion(new CellRangeAddress(
                    1,
                    2,
                    1,
                    2
            ));

            String fileName = DEST + "Excle" + i + ".xls";
            if(workbook instanceof XSSFWorkbook){
                fileName += "x";
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(fileName);
                workbook.write(fos);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(fos != null){
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读Excele文件
     */
    public static void readExcel(){
        String fileName = "D://workbook.xls";
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(fileName);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();
            for (Row row : sheet) {
                for (Cell cell : row) {
                    CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
                    //单元格名称
                    System.out.print(cellRef.formatAsString());
                    System.out.print(" - ");
                    //通过获取单元格值并应用任何数据格式（Date，0.00，1.23e9，$ 1.23等），获取单元格中显示的文本
                    String text = formatter.formatCellValue(cell);
                    System.out.println(text);
                    //获取值并自己格式化
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:// 字符串型
                            System.out.println(cell.getRichStringCellValue().getString());
                            break;
                        case Cell.CELL_TYPE_NUMERIC:// 数值型
                            if (DateUtil.isCellDateFormatted(cell)) { // 如果是date类型则 ，获取该cell的date值
                                System.out.println(cell.getDateCellValue());
                            } else {// 纯数字
                                System.out.println(cell.getNumericCellValue());
                            }
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:// 布尔
                            System.out.println(cell.getBooleanCellValue());
                            break;
                        case Cell.CELL_TYPE_FORMULA:// 公式型
                            System.out.println(cell.getCellFormula());
                            break;
                        case Cell.CELL_TYPE_BLANK:// 空值
                            System.out.println();
                            break;
                        case Cell.CELL_TYPE_ERROR: // 故障
                            System.out.println();
                            break;
                        default:
                            System.out.println();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 编辑excle文件
     */
    public static void editExcle(){
        String fileName = "D://workbook.xls";
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = new FileInputStream(fileName);
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(2);
            Cell cell = row.getCell(3);
            if(null == cell){
                cell = row.createCell(3);
            }
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue("test");
            fos = new FileOutputStream("D://out-workbook.xls");
            workbook.write(fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fos) {
                    fos.close();
                }
                if (null != is) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
