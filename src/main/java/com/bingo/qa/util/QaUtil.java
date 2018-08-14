package com.bingo.qa.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.google.common.math.DoubleMath;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.util.Map;

public class QaUtil {
    private static final Logger logger = LoggerFactory.getLogger(QaUtil.class);

    public static int SYSTEM_USERID = 12;

    public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            logger.error("生成MD5失败", e);
            return null;
        }
    }

    public static void createIdenticon(String username, int size) throws IOException {
        String hash = MD5(username);

        AvatarGenerator generator = new AvatarGenerator();
        Preconditions.checkArgument(size > 0 && StringUtils.isNotBlank(hash));

        boolean[][] array = generator.getBooleanValueArray(hash);


        int ratio = DoubleMath.roundToInt(size / 5.0, RoundingMode.HALF_UP);

        BufferedImage identicon = new BufferedImage(ratio * 5, ratio * 5, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = identicon.getGraphics();

        graphics.setColor(generator.getBackgroundColor()); // 背景色
        graphics.fillRect(0, 0, identicon.getWidth(), identicon.getHeight());

        graphics.setColor(generator.getForegroundColor()); // 图案前景色
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                if (array[i][j]) {
                    graphics.fillRect(j * ratio, i * ratio, ratio, ratio);
                }
            }
        }

        FileOutputStream fos = new FileOutputStream("src/main/resources/static/images/avatar/" + username + ".png");
        ImageIO.write(identicon, "PNG", fos);
        fos.close();


    }


    public static String getJSONString(int code) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        return jsonObject.toJSONString();
    }

    public static String getJSONString(int code, String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("msg", message);
        return jsonObject.toJSONString();
    }

    public static String getJSONString(int code, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }
        return json.toJSONString();
    }

    /**
     * 处理4/6字节的特殊字符串
     * @param str
     * @return
     */
    public static String dealString(String str) {
        return str.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*");
    }

}
