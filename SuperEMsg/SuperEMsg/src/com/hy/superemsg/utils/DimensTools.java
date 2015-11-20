
package com.hy.superemsg.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author menglinggen 29274682@qq.com
 * @date 2013-02-06
 */
public class DimensTools {
    /** 源文件 */
    static String oldFilePath = "./res/values-nodpi-800x480/dimens.xml";
    /** 新生成文件路径 */
    static String filePath1280x720 = "./res/values-nodpi-1280x720/";
    /** 新生成文件路径 */
    static String filePath1280x800 = "./res/values-nodpi-1280x800/";
    /** 新生成文件路径 */
    static String filePath1280x672 = "./res/values-nodpi-1280x672/";

    /** 新生成文件路径 */
    static String filePath854x480 = "./res/values-nodpi-854x480/";
    /** 新生成文件路径 */
    static String filePath800x480 = "./res/values-nodpi-800x480/";
    /** 新生成文件路径 */
    static String filePath800x444 = "./res/values-nodpi-800x444/";
    /** 新生成文件路径 */
    static String filePath960x540 = "./res/values-nodpi-960x540/";
    /** 新生成文件路径 */
    static String filePath960x640 = "./res/values-nodpi-960x640/";

    /** 新生成文件路径 */
    static String filePath1024x720 = "./res/values-nodpi-1024x720/";
    /** 新生成文件路径 */
    static String filePath1024x600 = "./res/values-nodpi-1024x600/";
    /** 新生成文件路径 */
    static String filePath1196x768 = "./res/values-nodpi-1196x768/";
    /** 新生成文件路径 */
    static String filePath1366x768 = "./res/values-nodpi-1366x768/";
    /** 新生成文件路径 */
    static String filePath1216x684 = "./res/values-nodpi-1216x684/";
    /** 新生成文件路径 */
    static String filePath2048x1460 = "./res/values-nodpi-2048x1460/";
    /** 新生成文件路径 */
    static String filePath1920x1128 = "./res/values-nodpi-1920x1128/";
    /** 新生成文件路径 */
    static String filePath1920x1080 = "./res/values-nodpi-1920x1080/";

    public static void main(String[] args) {

        RewriteFile(filePath1280x720, 800 / 1280f);
        RewriteFile(filePath1280x800, 800 / 1280f);
        RewriteFile(filePath1280x672, 800 / 1280f);

        RewriteFile(filePath854x480, 800 / 854f);
        RewriteFile(filePath800x480, 800 / 800f);
        RewriteFile(filePath800x444, 800 / 800f);
        RewriteFile(filePath960x540, 800 / 960f);
        RewriteFile(filePath960x640, 800 / 960f);

        RewriteFile(filePath1024x720, 800 / 1024f);
        RewriteFile(filePath1024x600, 800 / 1024f);
        RewriteFile(filePath1196x768, 800 / 1196f);
        RewriteFile(filePath1366x768, 800 / 1366f);
        RewriteFile(filePath1216x684, 800 / 1216f);

        RewriteFile(filePath2048x1460, 800 / 2048f);
        RewriteFile(filePath1920x1128, 800 / 1920f);
        RewriteFile(filePath1920x1080, 800 / 1920f);

    }

    /** 读取文件 生成缩放后字符串 */
    public static String convertStreamToString(String filepath, float f) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader bf = new BufferedReader(new FileReader(filepath));
            String line = null;
            String endmark = "px</dimen>";
            String startmark = ">";
            while ((line = bf.readLine()) != null) {
                if (line.contains(endmark)) {
                    int end = line.lastIndexOf(endmark);
                    int start = line.indexOf(startmark);
                    String stpx = line.substring(start + 1, end);
                    int px = Integer.parseInt(stpx);
                    int newpx = (int) (px / f);
                    String newline = line.replace(px + "px", newpx + "px");
                    sb.append(newline + "\r\n");
                } else {
                    sb.append(line + "\r\n");
                }
            }
            // System.out.println(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     * 
     * @param sPath 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */
    public static void mkResFile(String sPath) {
        File file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) {
            file.mkdirs();

        }
    }

    /** 存为新文件 */
    public static void RewriteFile(String filepath, float changes) {
        System.out.println(filepath + "  " + changes);
        String st = convertStreamToString(oldFilePath, changes);
        try {
            mkResFile(filepath);
            FileWriter fw = new FileWriter(filepath + "dimens.xml");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(st);
            bw.flush();
            bw.close();

            fw = new FileWriter(filepath + "strings.xml");
            bw = new BufferedWriter(fw);
            bw.write("<resources>\n\t<string name=\"values_dir\">"
                    + (new File(filepath).getName())
                    + "</string>\n</resources>");
            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除单个文件
     * 
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
}
