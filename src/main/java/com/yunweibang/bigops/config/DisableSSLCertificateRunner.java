package com.yunweibang.bigops.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.yunweibang.bigops.util.DisableSSLCertificateCheckUtil;


@Component
@Order(1)
public class DisableSSLCertificateRunner implements CommandLineRunner {
	private static Logger logger = LoggerFactory.getLogger(DisableSSLCertificateRunner.class);
    @Override
    public void run(String... args) throws Exception {
    	 logger.info("The DisableSSLCertificateRunner start to initialize ...");
         DisableSSLCertificateCheckUtil.disableChecks();
    }
}