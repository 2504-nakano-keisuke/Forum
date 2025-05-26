package com.example.forum.service;

import com.example.forum.controller.form.ReportForm;
import com.example.forum.repository.ReportRepository;
import com.example.forum.repository.entity.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    ReportRepository reportRepository;

    /*
     * レコード全件取得処理
     */
    public List<ReportForm> findAllReport() {
        List<Report> results = reportRepository.findAllByOrderByIdDesc();
        List<ReportForm> reports = setReportForm(results);
        return reports;
    }

    /*
     * レコード日付範囲取得処理
     */
    public List<ReportForm> findByCreated_dateReport(String start, String end) throws ParseException {

        if (start == null || start.isEmpty()) {
            start = "2020-01-01 00:00:00";
        } else {
            start += " 00:00:00";
        }
        if (end == null || end.isEmpty()) {
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTimestamp);
        } else {
            end += " 23:59:59";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Timestamp startDate = Timestamp.valueOf(start);
        Timestamp endDate = Timestamp.valueOf(end);
        List<Report> results = reportRepository.findByCreatedDateBetween(startDate, endDate);
        List<ReportForm> reports = setReportForm(results);
        return reports;
    }

    /*
     * DBから取得したデータをFormに設定
     */
    private List<ReportForm> setReportForm(List<Report> results) {
        List<ReportForm> reports = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            ReportForm report = new ReportForm();
            Report result = results.get(i);
            report.setId(result.getId());
            report.setContent(result.getContent());
            reports.add(report);
        }
        return reports;
    }

    /*
     * レコード追加
     */
    public void saveReport(ReportForm reqReport) {
        Report saveReport = setReportEntity(reqReport);
        reportRepository.save(saveReport);
    }

    /*
     * リクエストから取得した情報をEntityに設定
     */
    private Report setReportEntity(ReportForm reqReport) {
        Report report = new Report();
        report.setId(reqReport.getId());
        report.setContent(reqReport.getContent());

        return report;
    }

    /*
     * 投稿削除
     */
    public void deleteReport(int id) {
        reportRepository.deleteById(id);
    }

    /*
     * 投稿編集
     */
    public void updateReport(ReportForm reqReport) {
        Report saveReport = setReportEntity(reqReport);
        reportRepository.save(saveReport);
    }

    /*
     * レコード1件取得
     */
    public ReportForm editReport(Integer id) {
        List<Report> results = new ArrayList<>();
        results.add((Report) reportRepository.findById(id).orElse(null));
        List<ReportForm> reports = setReportForm(results);
        return reports.get(0);
    }
}
