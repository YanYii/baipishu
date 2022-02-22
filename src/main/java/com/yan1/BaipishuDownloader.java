package com.yan1;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 白皮书下载器
 */
public class BaipishuDownloader {

    private final String link;
    private final String filename;

    public BaipishuDownloader(String link, String filename) {
        this.link = link;
        this.filename = filename;
    }

    /**
     * 开始下载
     */
    public void startDownload() {
        // 步骤1和步骤2：访问网站，解析出每个文档图片的下载地址
        List<Pair<String, String>> pairs = parseHtml(link);

        // 步骤3和步骤4：下载图片合并成PDF文件
        if (CollectionUtils.isNotEmpty(pairs)) {
            PDFTool.mergePDFFromLinkPair(pairs, filename);
        } else {
            System.out.println("解析失败，未能正常下载文件生成pdf");
        }
    }

    /**
     * 解析网站上的文档图片地址
     *
     * @param link 网站地址
     * @return
     */
    private List<Pair<String, String>> parseHtml(String link) {
        try {
            URL url = new URL(link);
            Document document = Jsoup.parse(url, 1000 * 10);
            Elements elements = document.select("#lblContent").select("p").select("img");

            return elements.stream().map(element -> {
                String src = element.attr("src");
                String title = element.attr("title");

                System.out.println("解析出文件名: " + title + ", 文件路径：" + src);
                return Pair.of(completeLink(src), title);
            }).collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String completeLink(String src) {
        // TODO 待完善
        return "http://wlwbgzx.com" + src;
    }

}
