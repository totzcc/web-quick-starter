package com.fxit.base.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


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
        private long total;
        private long pages;
        private List<T> list;

        public BasePageRes(org.springframework.data.domain.Page<T> page) {
            this.list = page.getContent();
            this.total = page.getTotalElements();
            this.pages = page.getTotalPages();
        }

        public BasePageRes(List<T> list, org.springframework.data.domain.Page page) {
            this.list = list;
            this.total = page.getTotalElements();
            this.pages = page.getTotalPages();
            if (this.list == null) {
                this.list = Collections.emptyList();
            }
        }

        public BasePageRes(List<T> list, BasePageRes<?> page) {
            this.list = list;
            this.total = page.getTotal();
            this.pages = page.getPages();
            if (this.list == null) {
                this.list = Collections.emptyList();
            }
        }

        public BasePageRes(List<T> list, long total, int pageSize) {
            this.list = list;
            this.total = total;
            if (pageSize % total == 0) {
                this.pages = pageSize / total;
            } else {
                this.pages = pageSize / total + 1;
            }
            if (this.list == null) {
                this.list = Collections.emptyList();
            }
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