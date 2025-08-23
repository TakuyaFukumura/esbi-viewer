package com.example.myapplication.controller;

import com.example.myapplication.entity.EsbiData;
import com.example.myapplication.service.EsbiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/esbi")
public class EsbiController {

    private final EsbiService esbiService;

    public EsbiController(EsbiService esbiService) {
        this.esbiService = esbiService;
    }

    @GetMapping("/input")
    public String inputForm(Model model) {
        // デフォルトユーザー名を設定（実際のアプリケーションでは認証システムから取得）
        String defaultUser = "default_user";
        Optional<EsbiData> latestData = esbiService.getLatestDataByUser(defaultUser);
        
        if (latestData.isPresent()) {
            model.addAttribute("esbiData", latestData.get());
        } else {
            model.addAttribute("esbiData", new EsbiData());
        }
        
        return "esbi/input";
    }

    @PostMapping("/save")
    public String saveEsbiData(
            @RequestParam(value = "employeeIncome", defaultValue = "0") BigDecimal employeeIncome,
            @RequestParam(value = "selfEmployedIncome", defaultValue = "0") BigDecimal selfEmployedIncome,
            @RequestParam(value = "businessOwnerIncome", defaultValue = "0") BigDecimal businessOwnerIncome,
            @RequestParam(value = "investorIncome", defaultValue = "0") BigDecimal investorIncome,
            RedirectAttributes redirectAttributes) {
        
        try {
            String defaultUser = "default_user";
            esbiService.saveEsbiData(defaultUser, employeeIncome, selfEmployedIncome, 
                                   businessOwnerIncome, investorIncome);
            redirectAttributes.addFlashAttribute("successMessage", "ESBIデータを保存しました。");
            return "redirect:/esbi/visualization";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "データの保存中にエラーが発生しました。");
            return "redirect:/esbi/input";
        }
    }

    @GetMapping("/visualization")
    public String visualization(Model model) {
        String defaultUser = "default_user";
        Optional<EsbiData> latestData = esbiService.getLatestDataByUser(defaultUser);
        
        if (latestData.isPresent()) {
            EsbiData data = latestData.get();
            BigDecimal totalIncome = esbiService.getTotalIncome(data);
            
            model.addAttribute("esbiData", data);
            model.addAttribute("totalIncome", totalIncome);
            model.addAttribute("employeePercentage", esbiService.getPercentage(data.getEmployeeIncome(), totalIncome));
            model.addAttribute("selfEmployedPercentage", esbiService.getPercentage(data.getSelfEmployedIncome(), totalIncome));
            model.addAttribute("businessOwnerPercentage", esbiService.getPercentage(data.getBusinessOwnerIncome(), totalIncome));
            model.addAttribute("investorPercentage", esbiService.getPercentage(data.getInvestorIncome(), totalIncome));
        } else {
            model.addAttribute("noData", true);
        }
        
        return "esbi/visualization";
    }

    @GetMapping("/history")
    public String history(Model model) {
        String defaultUser = "default_user";
        List<EsbiData> historyData = esbiService.getAllDataByUser(defaultUser);
        model.addAttribute("historyData", historyData);
        return "esbi/history";
    }

    @GetMapping("/guide")
    public String guide() {
        return "esbi/guide";
    }
}
