package com.example.Project2.controller;

import com.example.Project2.bean.Archive;
import com.example.Project2.dao.ArchiveRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;


@Component
public class ExcelDatabase {

    private ArchiveRepository repository;

    @Autowired
    public ExcelDatabase(ArchiveRepository repository) {
        this.repository = repository;
    }

    public void readDB() throws Exception { // 读取数据库内容输出Excel
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connect = DriverManager.getConnection(
                "jdbc:mysql://172.25.67.174:3306/gyh_test?useUnicode=true&characterEncoding=UTF-8",
                "gyh" ,
                "Gyh@gyh123,"
        );

        Statement statement = connect.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from archive_management");
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet("archive db");

        XSSFRow row = spreadsheet.createRow(0);
        row.createCell(0).setCellValue("name");
        row.createCell(1).setCellValue("code");
        row.createCell(2).setCellValue("parentCode");

        int i = 1; // 从第二行开始写入数据

        while (resultSet.next()) {
            row = spreadsheet.createRow(i);
            row.createCell(0).setCellValue(resultSet.getString("name"));
            row.createCell(1).setCellValue(resultSet.getInt("code"));
            row.createCell(2).setCellValue(resultSet.getInt("parentCode"));
            i++;
        }

        FileOutputStream out = new FileOutputStream(new File("exceldatabase.xlsx"));
        workbook.write(out);
        out.close();
        System.out.println("exceldatabase.xlsx written successfully");
    }

    public void writeDB() throws IOException { // 读取Excel内容写入数据库
        FileSystemView fsv = FileSystemView.getFileSystemView();
        String desktop = fsv.getHomeDirectory().getPath();
        String filePath = desktop + "/archives.xlsx";

        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);

        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        while (rows.hasNext()) {
            Row currentRow = rows.next();
            if (currentRow.getRowNum() == 0) {
                continue; // Skip header row
            }

            Archive archiveManagement = new Archive();
            archiveManagement.setName(currentRow.getCell(0).getStringCellValue());
            archiveManagement.setCode((int) currentRow.getCell(1).getNumericCellValue());
            if (currentRow.getCell(2) != null) {
                archiveManagement.setParentCode((int) currentRow.getCell(2).getNumericCellValue());
            } else {
                archiveManagement.setParentCode(null);
            }

            repository.save(archiveManagement);
        }

        workbook.close();
        System.out.println("Excel file imported successfully");
    }
}