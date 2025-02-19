package ru.yandex.practicum.catsgram.model;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = "id")
public class Image {
    private Long id;
    private Long postId;
    private String originalFileName;
    private String filePath;
}
