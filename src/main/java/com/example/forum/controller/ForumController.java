package com.example.forum.controller;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.controller.form.ReportForm;
import com.example.forum.service.CommentService;
import com.example.forum.service.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes(value= "errorMessages")
public class ForumController {
    @Autowired
    ReportService reportService;
    @Autowired
    CommentService commentService;
    @Autowired
    HttpSession session;
    /*
     * 投稿内容表示処理
     */
    @GetMapping
    public ModelAndView top(@RequestParam(value="start", required = false)String start,
                            @RequestParam(value = "end", required = false)String end,
                            HttpServletRequest request) throws ParseException {
        ModelAndView mav = new ModelAndView();
        // 返信form用の空のentityを準備
        CommentForm commentsForm = new CommentForm();
        // 投稿を全件取得 日付検索に変えた
        List<ReportForm> reportData = reportService.findByCreated_dateReport(start, end);
        // 返信を全件取得
        List<CommentForm> commentData = commentService.findAllComment();
        //エラーメッセージを取得
        List<String> errorMessageForm = (List<String>) session.getAttribute("errorMessages");
        Integer reportId = (Integer) session.getAttribute("reportId");
        mav.addObject("mavErrorMessages", session.getAttribute("errorMessages"));
        mav.addObject("reportId", session.getAttribute("reportId"));
        session.invalidate();
        // 画面遷移先を指定
        mav.setViewName("/top");
        // 投稿データオブジェクトを保管
        mav.addObject("formModel", commentsForm);
        mav.addObject("reports", reportData);
        mav.addObject("comments", commentData);
        mav.addObject("start", start);
        mav.addObject("end", end);

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
    public ModelAndView addContent(@Validated @ModelAttribute("formModel") ReportForm reportForm, BindingResult result){
        if(result.hasErrors()) {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("/new");
            return mav;
        }
        // 投稿をテーブルに格納
        reportService.saveReport(reportForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * 返信投稿処理
     */
    @PostMapping("/comment/{reportId}")
    public String addComment(@Validated @ModelAttribute("formModel") CommentForm commentForm,
                             BindingResult result,
                             @PathVariable Integer reportId){
        if(result.hasErrors()) {
            List<String> errorMessages = new ArrayList<String>();
            for (FieldError error : result.getFieldErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            session.setAttribute("errorMessages", errorMessages);
            session.setAttribute("reportId", reportId);
            return "redirect:/";
        }
        // 投稿をテーブルに格納
        commentForm.setReport_id(reportId);
        commentService.saveComment(commentForm);
        // idをもとにDBから投稿を入手する
        ReportForm reportForm = reportService.editReport(reportId);
        // 投稿のupdated_dateを更新
        reportService.saveReport(reportForm);
        // rootへリダイレクト
        return "redirect:/";
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
     * 返信削除処理
     */
    @DeleteMapping("/comment/delete/{id}")
    public ModelAndView deleteComment(@PathVariable Integer id) {
        // 投稿をテーブルに格納
        commentService.deleteComment(id);
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
     * コメント編集画面表示
     */
    @GetMapping("/comment/edit/{id}/{reportId}")
    public ModelAndView editComment(@PathVariable Integer id,@PathVariable Integer reportId) {
        ModelAndView mav = new ModelAndView();
        // idをもとにDBから投稿を入手する
        CommentForm commentForm = commentService.editComment(id);
        // 画面遷移先を指定
        mav.setViewName("/editComment");
        // 準備した空のFormを保管
        mav.addObject("formModel", commentForm);
        return mav;
    }

    /*
     * 編集投稿処理
     */
    @PutMapping("/update/{id}")
    public ModelAndView updateContent(@PathVariable Integer id,@Validated @ModelAttribute("formModel") ReportForm reportForm, BindingResult result){
        if(result.hasErrors()) {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("/edit");
            return mav;
        }
        // 投稿をテーブルに格納
        reportForm.setId(id);
        reportService.saveReport(reportForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * 編集投稿処理
     */
    @PutMapping("/comment/update/{id}/{reportId}")
    public ModelAndView updateComment(@PathVariable Integer id,@PathVariable Integer reportId,@Validated @ModelAttribute("formModel") CommentForm commentForm, BindingResult result){
        if(result.hasErrors()) {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("/editComment");
            return mav;
        }
        // 返信をテーブルに格納
        commentForm.setId(id);
        commentForm.setReport_id(reportId);
        commentService.saveComment(commentForm);
        // idをもとにDBから投稿を入手する
        ReportForm reportForm = reportService.editReport(reportId);
        // 投稿のupdated_dateを更新
        reportService.saveReport(reportForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

}