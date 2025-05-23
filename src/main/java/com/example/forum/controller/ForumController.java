package com.example.forum.controller;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.controller.form.ReportForm;
import com.example.forum.repository.ReportRepository;
import com.example.forum.service.CommentService;
import com.example.forum.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ForumController {
    @Autowired
    ReportService reportService;
    @Autowired
    CommentService commentService;

    /*
     * 投稿内容表示処理
     */
    @GetMapping
    public ModelAndView top() {
        ModelAndView mav = new ModelAndView();
        // 返信form用の空のentityを準備
        CommentForm commentForm = new CommentForm();
        // 投稿を全件取得
        List<ReportForm> reportData = reportService.findAllReport();
        // 返信を全件取得
        List<CommentForm> commentData = commentService.findAllComment();
        // 画面遷移先を指定
        mav.setViewName("/top");
        // 投稿データオブジェクトを保管
        mav.addObject("formModel", commentForm);
        mav.addObject("reports", reportData);
        mav.addObject("comments", commentData);

        return mav;
    }
    /*
     * 新規投稿画面表示
     */
    @GetMapping("/new")
    public ModelAndView newContent() {
        ModelAndView mav = new ModelAndView();
        // form用の空のentityを準備
        ReportForm reportForm = new ReportForm();
        // 画面遷移先を指定
        mav.setViewName("/new");
        // 準備した空のFormを保管
        mav.addObject("formModel", reportForm);
        return mav;
    }

    /*
     * 新規投稿処理
     */
    @PostMapping("/add")
    public ModelAndView addContent(@ModelAttribute("formModel") ReportForm reportForm){
        // 投稿をテーブルに格納
        reportService.saveReport(reportForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * 返信投稿処理
     */
    @PostMapping("/comment/{reportId}")
    public ModelAndView addComment(@ModelAttribute("formModel") CommentForm commentForm, @PathVariable Integer reportId){
        // 投稿をテーブルに格納
        commentForm.setReport_id(reportId);
        commentService.saveComment(commentForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    @GetMapping(value = "/sample")
// 「name」というリクエストパラメータに紐づく値をsampleメソッドの引数nameに設定
// "http://localhost:8080/sample?name=AAA" でアクセスすると、nameには「AAA」が設定される
// "http://localhost:8080/sample" でアクセスすると、nameには「SAMPLE」が設定される
    public String sample(@RequestParam(name = "name", defaultValue = "SAMPLE", required = false) String name) {
        return name;
    }


    /*
     * 投稿削除処理
     */
    @GetMapping(value = "/delete/{id}")
    public ModelAndView deleteContent2(@PathVariable Integer id){
        // 投稿をテーブルから削除
        reportService.deleteReport(id);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * 投稿削除処理
     */
    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteContent(@PathVariable Integer id) {
        // 投稿をテーブルに格納
        reportService.deleteReport(id);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * 編集画面表示
     */
    @GetMapping("/edit/{id}")
    public ModelAndView editContent(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        // idをもとにDBから投稿を入手する
        ReportForm reportForm = reportService.editReport(id);
        // 画面遷移先を指定
        mav.setViewName("/edit");
        // 準備した空のFormを保管
        mav.addObject("formModel", reportForm);
        return mav;
    }

    /*
     * 編集投稿処理
     */
    @PutMapping("/update/{id}")
    public ModelAndView updateContent(@PathVariable Integer id,@ModelAttribute("formModel") ReportForm reportForm){
        // 投稿をテーブルに格納
        reportForm.setId(id);
        reportService.saveReport(reportForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

}