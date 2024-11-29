package org.mivirim.server.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/template")
public class TemplateController {

    private static final Logger LOG = LogManager.getLogger(TemplateController.class);

    @GetMapping("/test")
    public String testHi() {
        return "hi";
    }

    @GetMapping
    public String view(Model model) {
        LOG.info("Rendering template again....");
        model.addAttribute("model", new DemoModel("dummy"));
        return "org/mivirim/server/test/demo";
    }

    @PostMapping("/clicked")
    public String clicked(Model model) {
        return "org/mivirim/server/test/clicked";
    }

}
