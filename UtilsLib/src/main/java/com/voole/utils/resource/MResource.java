package com.voole.utils.resource;

import android.content.Context;

/**
 * @author fengyanjie
 * @desc  资源文件转换为ID
 * 例如R.layout.main
 * @time 2017-11-29 9:50
 */

public class MResource {
    /**
     * className 可以是layout或者id
     * @param context
     * @param className
     * @param name
     * @return
     */
    public static int getIdByName(Context context, String className, String name) {
        String packageName = context.getPackageName();
        Class r = null;
        int id = 0;
        try {
            r = Class.forName(packageName + ".R");
            Class[] classes = r.getClasses();
            Class desireClass = null;
            for (int i = 0; i < classes.length; ++i) {
                if (classes[i].getName().split("\\$")[1].equals(className)) {
                    desireClass = classes[i];
                    break;
                }
            }

            if (desireClass != null) {
                    id = desireClass.getField(name).getInt(desireClass);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return id;
    }
}
