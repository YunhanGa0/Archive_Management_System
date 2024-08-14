package com.example.Project2.controller;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AsyncTaskProcessor {

    @Autowired
    private ExcelDatabase excelDatabase;

    @RabbitListener(queues = "excelQueue")
    public void processExcelTask(String taskType) throws Exception {
        if ("readDB".equals(taskType)) {
            excelDatabase.readDB();
        } else if ("writeDB".equals(taskType)) {
            excelDatabase.writeDB();
        }
    }
}
