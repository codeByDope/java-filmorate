package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Feed {
    private Long eventId;
    private Long userId;
    private Long entityId;
    private Long timestamp;
    private String eventType;
    private String operation;
}
