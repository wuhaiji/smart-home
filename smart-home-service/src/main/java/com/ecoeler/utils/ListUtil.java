package com.ecoeler.utils;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/9/27
 */
public class ListUtil {

    public static <T> Map<Object, T> toMap(String column, List<T> list) {

        HashMap<Object, T> listMap = new HashMap<>();
        if (list == null || list.size() <= 0) {
            return listMap;
        }
        if (column == null || column.isEmpty()) {
            return listMap;
        }
        list.parallelStream().forEach(t -> {
            try {
                Field f = t.getClass().getDeclaredField(column);
                f.setAccessible(true);
                Object o = f.get(t);
                listMap.put(o, t);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return listMap;
    }


//    public static void main(String[] args) {
//        List<Student> students = CollUtil.toList(
//                new Student("张", 1),
//                new Student("李", 2),
//                new Student("东", 3)
//        );
//        Map<Object, Student> studentMap = toMap("name", students);
//        System.out.println(JSON.toJSON(studentMap));
//        Student student = studentMap.get("张");
//        System.out.println(student);
//    }
}
