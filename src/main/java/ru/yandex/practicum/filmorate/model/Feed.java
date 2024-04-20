package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Feed {
    private Long eventId;
    private Long userId;
    private Long entityId;
    private Long timestamp;
    private String eventType;
    private String operation;
}
