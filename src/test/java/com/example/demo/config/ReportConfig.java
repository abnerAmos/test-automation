package com.example.demo.config;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.configuration.ViewName;

import java.io.File;

// Classe gera um Report dos testes executados.
public class ReportConfig {

    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            createInstance("report.html");
        }
        return extent;
    }

    // Cria a instância do Report ao qual configure o HTML, ao qual cria o arquivo HTML.
    public static ExtentReports createInstance(String fileName) {
        ExtentSparkReporter htmlReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "/reports/" + fileName);
        htmlReporter.viewConfigurer().viewOrder().as(new ViewName[] {ViewName.DASHBOARD, ViewName.TEST, ViewName.CATEGORY}).apply();
        htmlReporter.config().setTheme(Theme.DARK);
        htmlReporter.config().setReportName(pathImage());
        htmlReporter.config().setDocumentTitle("Reports Rastro - MT");
        htmlReporter.config().setTimeStampFormat("EEEE - dd, MMMM, yyyy - HH:mm '('zzz')'");

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);

        return extent;
    }

    // Configura uma imagem para aparecer no header do Report
    private static String pathImage() {
        File file = new File("./files-test/img.jpg");
        String image = file.getAbsolutePath();
        String path = String.format("<img src='%s' alt='Logo' height='30' width='5'>", image);
        return path;
    }
}
