package com.github.zxxz_ru.entity;

public interface StoreUnit {
    Integer getId();
    void setId(Integer i);

    <T extends StoreUnit> T from(T t);
}
