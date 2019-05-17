/*
 * Copyright 2018 www.yunweibang.com Inc. All rights reserved.
 */
package com.yunweibang.bigops.wizard.controller;

import com.yunweibang.bigops.common.JsonResponse;
import com.yunweibang.bigops.wizard.model.WizardConfig;
import com.yunweibang.bigops.wizard.service.WizardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author lpp
 */
@Controller
public class WizardController {
    @Autowired
    private WizardService wizardService;

    @GetMapping("/")
    public String index() {
        return "wizard";
    }

    @PostMapping("/config/generate")
    @ResponseBody
    public JsonResponse generateConfig(@RequestBody WizardConfig config) {
        return wizardService.generateConfig(config);
    }

    @GetMapping("/success")
    public String success(Map<String, Object> map) {
        map.put("homeUrl", wizardService.getHomeUrl());
        return "success";
    }

}
