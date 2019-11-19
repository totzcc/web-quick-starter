package com.fxit.base.utils;

import com.fxit.base.exception.BizException;
import com.fxit.base.exception.IBizError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Slf4j
public class Copies {
    public static <S, T> T mapObject(S s, Class<T> tClass, Copyable<S, T> copyable) {
        if (s == null)
            return null;
        T t = BeanUtils.instantiateClass(tClass);
        BeanCopier.create(s.getClass(), tClass, false).copy(s, t, null);
        if (copyable != null)
            copyable.copy(s, t);
        return t;
    }

    public static <S, T> T mapObject(S s, Class<T> tClass) {
        return mapObject(s, tClass, null);
    }

    public static <S, T> List<T> mapArray(Iterable<S> sIterable, Class<T> tClass, Copyable<S, T> copyable) {
        if (sIterable == null) {
            sIterable = Collections.emptyList();
        }
        return StreamSupport.stream(sIterable.spliterator(), false).map(s -> mapObject(s, tClass, copyable)).collect(Collectors.toList());
    }

    public static <S, T> List<T> mapArray(Iterable<S> sIterable, Class<T> tClass) {
        return mapArray(sIterable, tClass, null);
    }


    public static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }

    public static interface Copyable<S, T> {
        void copy(S s, T t);
    }

    @Data
    @NoArgsConstructor
    public static class BasePageRes<T> implements Serializable {
        private long total = -1;
        private long pages = -1;
        private List<T> list;

        public static <T> BasePageRes<T> fromPagesTotal(List<T> list, long total, long pageSize) {
            if (list == null) {
                list = Collections.emptyList();
            }
            BasePageRes<T> res = new BasePageRes<>();
            res.list = list;
            res.total = total;
            long pages = total / pageSize;
            if (total % pageSize != 0) {
                pages += 1;
            }
            res.pages = pages;
            return res;
        }

        public static <T> BasePageRes<T> fromPage(List<T> list, long pages, long total) {
            if (list == null) {
                list = Collections.emptyList();
            }
            BasePageRes<T> res = new BasePageRes<>();
            res.list = list;
            res.pages = pages;
            res.total = total;
            return res;
        }

        /**
         * @param pageObject
         * <pre>
         * org.springframework.data.domain.Page
         * com.fxit.base.utils.Copies.BasePageRes
         * </pre>
         */
        public static <T> BasePageRes<T> fromPageObject(@NonNull Object pageObject) {
            return fromPageObject(null, pageObject);
        }

        /**
         * @param pageObject
         * <pre>
         * org.springframework.data.domain.Page
         * com.fxit.base.utils.Copies.BasePageRes
         * </pre>
         */
        @SuppressWarnings("unchecked")
        public static <T> BasePageRes<T> fromPageObject(List<T> list, @NonNull Object pageObject) {
            BasePageRes<T> res = new BasePageRes<>();
            if (pageObject instanceof BasePageRes) {
                res.pages = ((BasePageRes) pageObject).getPages();
                res.total = ((BasePageRes) pageObject).getTotal();
                if (list == null) {
                    list = ((BasePageRes) pageObject).getList();
                }
            }
            Exception checkTypeException = null;
            if (res.pages == -1) {
                try {
                    Class<?> aClass = Class.forName("org.springframework.data.domain.Page");
                    if (aClass.isInstance(pageObject)) {
                        res.total = (long) aClass.getMethod("getTotalElements").invoke(pageObject);
                        res.pages = (long) aClass.getMethod("getTotalPages").invoke(pageObject);
                        list = (List) aClass.getMethod("getContent").invoke(pageObject);
                    }
                } catch (Exception e) {
                    checkTypeException = e;
                }
                try {
                    Class<?> aClass = Class.forName("com.baomidou.mybatisplus.core.metadata.IPage");
                    if (aClass.isInstance(pageObject)) {
                        res.total = (long) aClass.getMethod("getTotal").invoke(pageObject);
                        res.pages = (long) aClass.getMethod("getPages").invoke(pageObject);
                        list = (List) aClass.getMethod("getRecords").invoke(pageObject);
                    }
                } catch (Exception e) {
                    checkTypeException = e;
                }
            }
            if (res.pages == -1) {
                throw new BizException(IBizError.BizCommonError.SYSTEM_ERROR, "pageObject不支持此类: " + pageObject.getClass(), checkTypeException);
            }
            res.list = list;
            return res;
        }

        public static <T> BasePageRes<T> empty() {
            BasePageRes<T> page = new BasePageRes<>();
            page.setList(Collections.emptyList());
            page.setTotal(0L);
            page.setPages(0);
            return page;
        }

        public boolean isEmpty() {
            return CollectionUtils.isEmpty(list);
        }
    }

    @Data
    @AllArgsConstructor
    public static class BasePageReq<T> implements Serializable {
        private T data;
        private Integer pageNum;
        private Integer pageSize;
    }
}