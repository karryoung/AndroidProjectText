package com.facerecognition.dualdemoVL;

import android.app.Application;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @Description 模型初始化相关，必要类，直接拷贝无需改动
 */
public class ModelInit {
    private static Map<Integer, String> modelDirMap;

    // 初始化模型数据
    public static Map<Integer, String> initModelDirMap(Application cx) {
        Map<Integer, String> modelMap = new HashMap<>();
        modelMap.put(R.raw.detect, "Detect.pkg");
        modelMap.put(R.raw.lm, "Landmark.pkg");
        modelMap.put(R.raw.extract, "Extract.pkg");
        modelMap.put(R.raw.anti,"Anti.pkg");
        modelMap.put(R.raw.prop,"Prop.pkg");

        modelDirMap = new HashMap<>();

        for (Integer key : modelMap.keySet()) {
            String outputFileName = modelMap.get(key);
            try {

                InputStream inputStream = cx.getResources().openRawResource(key);
                String storagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/dualdemoVL"; //  /storage/sdcard0/dualdemoVL/
                File outputDir = new File(storagePath);
                if (!outputDir.exists()) {
                    outputDir.mkdir();
                }

                System.out.println("filePath:" + outputDir.getAbsolutePath());
                File outputFile = new File(outputDir, outputFileName);
                FileOutputStream outputStream = new FileOutputStream(outputFile);

                byte[] buffer = new byte[inputStream.available()];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                inputStream.close();
                outputStream.close();
                modelDirMap.put(key, outputDir.getAbsolutePath());

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return modelDirMap;
    }

    public static void initFras() {
        try {
            System.loadLibrary("fras-jni");
        } catch (UnsatisfiedLinkError e) {
            System.out.println("face recognition library not found!");
        }
    }

    /**
     * 获取加载好的模型数据
     *
     * @return
     */
    public static Map<Integer, String> getModelDirMap() {
        if (modelDirMap == null) {
            throw new NullPointerException("modelDirMap为空，请检查是否已进行了初始化操作");
        } else {
            return modelDirMap;
        }
    }

}
