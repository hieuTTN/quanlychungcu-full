package com.web.controller;

import com.web.entity.Maintenance;
import com.web.entity.RepairRequest;
import com.web.entity.Resident;
import com.web.repository.MaintenanceRepository;
import com.web.repository.RepairRequestRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class BaoTriController {

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @GetMapping(value = {"/bao-tri"})
    public String view(HttpSession session, Model model){
        List<Maintenance> list = maintenanceRepository.findAll();
        list.forEach(p->{
            p.setContent(extractBodyContent(p.getContent()));
        });
        model.addAttribute("listRes", list);

        return "baotri";
    }

    public String extractBodyContent(String htmlContent) {
        if (htmlContent == null || htmlContent.isEmpty()) {
            return "";
        }
        // Parse HTML content
        Document document = Jsoup.parse(htmlContent);
        // Extract content inside <body>
        return document.body().html();
    }
}
