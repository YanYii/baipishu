package com.yan1;

/**
 * 白皮书下载器启动程序
 */
public class App {

    public static void main(String[] args) {
        System.out.println("App running...");

        String url = "http://wlwbgzx.com/newsdetail/224";
        String filename = "2020-2023中国高等级自动驾驶产业发展趋势研究.pdf";

        BaipishuDownloader baipishuDownloader = new BaipishuDownloader(url, filename);
        baipishuDownloader.startDownload();

        System.out.println("App finish.");
    }

}
