package com.voole.utils.base;

import java.util.Collection;

/**
 * @author Created by lichao
 * @desc
 * @time 2018/1/12 16:22
 * 邮箱：lichao@voole.com
 */

public class CollectionUtil {
    public static <T> boolean isEmpty(Collection<T> data) {
        return (data == null) || (data.size() == 0);
    }
}
