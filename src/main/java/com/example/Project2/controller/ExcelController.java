package com.example.Project2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExcelController {

    private final ExcelDatabase excelDatabase;

    @Autowired
    public ExcelController(ExcelDatabase excelDatabase) {
        this.excelDatabase = excelDatabase;
    }

    @GetMapping("/readDb")
    public String readDatabaseToExcel() {
        try {
            excelDatabase.readDB();
            return "数据库成功导出到 Excel";
        } catch (Exception e) {
            e.printStackTrace();
            return "导出数据库到 Excel 发生错误: " + e.getMessage();
        }
    }

    @GetMapping("/writeDb")
    public String writeExcelToDatabase() {
        try {
            excelDatabase.writeDB();
            return "Excel 成功导入到数据库";
        } catch (Exception e) {
            e.printStackTrace();
            return "导入 Excel 到数据库发生错误: " + e.getMessage();
        }
    }
}
