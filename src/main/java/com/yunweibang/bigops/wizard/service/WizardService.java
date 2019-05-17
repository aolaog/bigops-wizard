/*
 * Copyright 2018 www.yunweibang.com Inc. All rights reserved.
 */
package com.yunweibang.bigops.wizard.service;

import com.yunweibang.bigops.common.JsonResponse;
import com.yunweibang.bigops.wizard.model.WizardConfig;

/**
 * @author lpp
 */
public interface WizardService {

    JsonResponse generateConfig(WizardConfig config);

    String getHomeUrl();

}
