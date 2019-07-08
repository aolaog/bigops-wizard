package com.yunweibang.bigops.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExecUtil {

    public static Log log = LogFactory.getLog(ExecUtil.class);

    public static String exec(String command) {

        Runtime runtime = Runtime.getRuntime();
        StringBuffer sb = new StringBuffer();

        String result = "";
        try {

            Process process = runtime.exec(command);

            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line = null;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            is.close();
            isr.close();
            br.close();

            result = sb.toString();
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    public static String execShell(String shell) {

        String[] execShell = new String[]{"/bin/sh", "-c", shell};

        Runtime runtime = Runtime.getRuntime();
        StringBuffer sb = new StringBuffer();

        String result = "";
        try {

            Process process = runtime.exec(execShell);

            if (!process.waitFor(Constants.EXEC_TIMEOUT, TimeUnit.MILLISECONDS)) {
                log.error(shell + "  连接超时");
                return null;
            }

            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line = null;

            while ((line = br.readLine()) != null) {
                if (!"".equals(line)) {
                    sb.append(line);
                }
            }

            is.close();
            isr.close();
            br.close();

            result = sb.toString();
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    public static List<String> shellLine(String shell) {

        List<String> result = new ArrayList<>();

        String[] execShell = new String[]{"/bin/sh", "-c", shell};

        Runtime runtime = Runtime.getRuntime();

        Process process = null;
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {

            process = runtime.exec(execShell);

            if (!process.waitFor(Constants.EXEC_TIMEOUT, TimeUnit.MILLISECONDS)) {
                log.error(shell + "  连接超时");
                return null;
            }

            is = process.getInputStream();
            isr = new InputStreamReader(is, "UTF-8");
            br = new BufferedReader(isr);
            String line = "";

            while ((line = br.readLine()) != null) {
                if (!"".equals(line)) {
                    line = line.replace("\r", "");
                    line = line.replace("\n", "");
                    result.add(line);
                }
            }

            br.close();
            isr.close();
            is.close();

            process.destroy();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return result;
    }

}
