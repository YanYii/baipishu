package com.yan1;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * PDF工具
 */
public class PDFTool {

    /**
     * 合并所有图片成一个PDF文件
     *
     * @param linkPairList 图片地址列表
     * @param filename     PDF文件名
     */
    public static void mergePDFFromLinkPair(List<Pair<String, String>> linkPairList, String filename) {
        PDDocument doc = new PDDocument();

        singleThreadAddDoc(doc, linkPairList);

        try {
            doc.save(filename);
            doc.close();

            System.out.println("\nPDF文件【" + filename + "】保存成功");
        } catch (IOException e) {
            e.printStackTrace();

            System.out.println("\nPDF文件【" + filename + "】合成失败");
        }
    }

    private static void singleThreadAddDoc(PDDocument doc, List<Pair<String, String>> linkPairList) {
        linkPairList.forEach(pair -> {
            String link = pair.getKey();
            String title = pair.getValue();
            try {
                System.out.println("==>下载文件：" + title + ", link: " + link);
                URL url = new URL(link);
                BufferedImage bufferedImage = ImageIO.read(url);
                addPage(doc, bufferedImage);

                System.out.println("<==合入PDF：" + title + ", link: " + link);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 把一张图片合并到PDF文档里
     *
     * @param doc           pdf文档对象
     * @param bufferedImage 图片对象
     * @throws IOException
     */
    private static void addPage(PDDocument doc, BufferedImage bufferedImage) throws IOException {
        PDImageXObject pdImage = LosslessFactory.createFromImage(doc, bufferedImage);
        int w = pdImage.getWidth();
        int h = pdImage.getHeight();

        PDPage page = new PDPage(new PDRectangle(w, h));
        PDPageContentStream pdPageContentStream = new PDPageContentStream(doc, page);
        pdPageContentStream.drawImage(pdImage, 0, 0, w, h);
        pdPageContentStream.close();

        doc.addPage(page);
    }

}
