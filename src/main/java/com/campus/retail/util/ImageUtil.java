package com.campus.retail.util;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 图片处理工具类 - 压缩图片
 */
public class ImageUtil {

    /**
     * 压缩商品图片（先裁剪成正方形，再压缩到指定尺寸）
     * @param sourceFile 源文件
     * @param targetFile 目标文件
     * @param size 目标尺寸（像素，正方形）
     * @param quality 压缩质量（0.0-1.0，1.0为最高质量）
     * @throws IOException 压缩失败时抛出异常
     */
    public static void compressProductImage(File sourceFile, File targetFile, int size, double quality) throws IOException {
        // 读取原始图片
        BufferedImage originalImage = ImageIO.read(sourceFile);
        if (originalImage == null) {
            throw new IOException("无法读取图片文件");
        }
        
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        
        // 计算裁剪尺寸（取较小的边作为正方形的边长）
        int cropSize = Math.min(originalWidth, originalHeight);
        
        // 计算裁剪起始位置（居中裁剪）
        int x = (originalWidth - cropSize) / 2;
        int y = (originalHeight - cropSize) / 2;
        
        // 先裁剪成正方形，再压缩到目标尺寸
        Thumbnails.of(sourceFile)
                .sourceRegion(x, y, cropSize, cropSize)  // 从中心裁剪成正方形
                .size(size, size)                       // 压缩到目标尺寸
                .outputQuality(quality)                 // 设置压缩质量
                .outputFormat("jpg")                    // 统一输出为JPG格式
                .toFile(targetFile);
    }

    /**
     * 压缩商品图片（使用默认参数：800x800，质量0.8）
     * @param sourceFile 源文件
     * @param targetFile 目标文件
     * @throws IOException 压缩失败时抛出异常
     */
    public static void compressProductImage(File sourceFile, File targetFile) throws IOException {
        compressProductImage(sourceFile, targetFile, 800, 0.8);
    }

    /**
     * 压缩并保存商品图片
     * @param multipartFile 上传的文件
     * @param targetFile 目标文件
     * @throws IOException 压缩失败时抛出异常
     */
    public static void compressAndSaveProductImage(MultipartFile multipartFile, File targetFile) throws IOException {
        // 先保存临时文件
        File tempFile = new File(targetFile.getParent(), "temp_" + System.currentTimeMillis() + "_" + targetFile.getName());
        multipartFile.transferTo(tempFile);
        
        try {
            // 压缩图片（先裁剪成正方形，再压缩到800x800）
            compressProductImage(tempFile, targetFile);
        } finally {
            // 删除临时文件
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }
}
