package com.github.zxxz_ru.storage.file;

import org.springframework.stereotype.Component;

/**
 * Enum used to signal what Entity FileSystemRepository must work with.
 */
public enum EntityMode {
    PROJECT, TASK, USER
}
