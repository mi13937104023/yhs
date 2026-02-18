package com.ddzn.dd.framework.common.util;

import com.ddzn.dd.model.base.BusinessException;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.MultipleBarcodeReader;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码解析工具类（支持单码、多码解析）
 */
@Slf4j
public class QrCodeParseUtils {

    // 解析配置（支持多种码制和编码）
    private static final Map<DecodeHintType, Object> HINTS;

    // 网络图片最大限制（10MB）
    private static final long MAX_IMAGE_SIZE = 10 * 1024 * 1024;

    // 网络连接超时（5秒）
    private static final int CONNECT_TIMEOUT = 5000;

    // 网络读取超时（10秒）
    private static final int READ_TIMEOUT = 10000;

    static {
        HINTS = new HashMap<>();
        // 支持UTF-8编码
        HINTS.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        // 开启复杂解析模式（提高模糊图片识别率）
        HINTS.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        // 支持的码制（可根据需求增减）
        HINTS.put(DecodeHintType.POSSIBLE_FORMATS,
                java.util.Arrays.asList(
                        BarcodeFormat.QR_CODE,    // QR码
                        BarcodeFormat.CODE_128,   // 128码
                        BarcodeFormat.EAN_13      // 商品码
                ));
    }

    /**
     * 通过网络URL解析二维码
     *
     * @param imageUrl 图片网络链接（必须以http://或https://开头）
     * @return 二维码内容（多个内容用逗号分隔）
     * @throws BusinessException 链接无效、网络错误或解析失败时抛出
     */
    public static String parseQrCodeByUrl(String imageUrl) throws BusinessException {
        // 1. 校验URL非空
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            throw new BusinessException("图片URL不能为空");
        }

        // 2. 校验URL格式
        String url = imageUrl.trim();
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw new BusinessException("无效的图片URL，必须以http://或https://开头");
        }

        // 3. 网络请求获取图片流（带超时和大小限制）
        try {
            URL imageUrlObj = new URL(url);
            URLConnection connection = imageUrlObj.openConnection();
            connection.setConnectTimeout(CONNECT_TIMEOUT); // 连接超时
            connection.setReadTimeout(READ_TIMEOUT);       // 读取超时

            // 读取图片流并限制大小
            try (InputStream inputStream = connection.getInputStream()) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                long totalBytes = 0;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    totalBytes += bytesRead;
                    if (totalBytes > MAX_IMAGE_SIZE) {
                        throw new BusinessException("图片过大，超过10MB限制");
                    }
                    baos.write(buffer, 0, bytesRead);
                }

                // 转换为输入流解析
                try (InputStream limitedInputStream = new ByteArrayInputStream(baos.toByteArray())) {
                    return parseQrCode(limitedInputStream);
                }
            }
        } catch (IOException e) {
            throw new BusinessException("网络图片读取失败：" + e.getMessage());
        }
    }

    /**
     * 从输入流解析二维码（核心解析逻辑）
     */
    private static String parseQrCode(InputStream inputStream) throws IOException, BusinessException {
        // 读取图片为BufferedImage
        BufferedImage image = ImageIO.read(inputStream);
        if (image == null) {
            throw new BusinessException("无法读取图片，可能是无效格式或损坏的文件");
        }

        // 转换为ZXing可处理的格式
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        // 多码解析器（支持一张图片多个二维码）
        MultipleBarcodeReader reader = new QRCodeMultiReader();
        Result[] results;
        try {
            results = reader.decodeMultiple(bitmap, HINTS);
        } catch (NotFoundException e) {
            throw new BusinessException("未识别到二维码内容");
        }

        // 校验解析结果
        if (results == null || results.length == 0) {
            throw new BusinessException("未识别到二维码内容");
        }

        // 拼接多个二维码内容（用逗号分隔）
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < results.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(results[i].getText());
        }
        return sb.toString();
    }
}