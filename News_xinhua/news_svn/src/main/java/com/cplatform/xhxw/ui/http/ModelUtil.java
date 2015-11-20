package com.cplatform.xhxw.ui.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.cplatform.xhxw.ui.util.LogUtil;
import com.loopj.android.http.RequestParams;

public class ModelUtil {

    public static RequestParams getRequestParamsFromObject(Object obj) {
        RequestParams params = new RequestParams();

        if (obj != null) {
            Class classType = obj.getClass();

            Field[] fields = classType.getDeclaredFields();
            if (fields != null) {
                int length = fields.length;
                for (int i = 0; i < length; i++) {
                    Field field = fields[i];
                    String fieldName = field.getName();
                    if(fieldName.equals("saasRequest")) {
                    	continue;
                    }
                    String getMethodName = "get"
                            + fieldName.substring(0, 1).toUpperCase()
                            + fieldName.substring(1);
                    try {
                        Method getMethod;
                        try {
                            getMethod = classType.getMethod(getMethodName, new Class[]{});
                        } catch (NoSuchMethodException e1) {
                        	e1.printStackTrace();
                            continue;
                        }
                        Object value = getMethod.invoke(obj, new Object[]{});
                        if (value instanceof File) {
                            try {
                                params.put(fieldName, (File) value);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        } else if(value instanceof HashMap<?, ?>) {//处理多文件上传
                        	try {
                        		HashMap<String, File> data = (HashMap<String, File>) value;
								for(String key : data.keySet()) {
									LogUtil.e("---", "------fileName-----" + key + 
											" ---filePath--------" + data.get(key).getAbsolutePath());
									params.put(key, data.get(key));
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
                        } else {
                            params.put(fieldName, value != null ? String.valueOf(value) : (String) null);
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return params;
    }
}
