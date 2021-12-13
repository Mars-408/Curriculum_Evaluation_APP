package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * 去除list中的重复值，用于数据库内容提取
 * 原理:利用LinkedHashSet不能添加重复数据并能保证添加顺序的特性
 */
public class RemoveSame  {
    public static  void ToRemove(List<String> list){
        HashSet<String> set = new HashSet<String>(list.size());
        List<String> result = new ArrayList<String>(list.size());
        for(String str:list) {
            if (set.add(str)) {
                result.add(str);
            }
        }
        list.clear();
        list.addAll(result);
    }
}