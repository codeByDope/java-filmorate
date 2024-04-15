package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.directors.DirectorService;
import ru.yandex.practicum.filmorate.utils.ApiPathConstants;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ru.yandex.practicum.filmorate.controller.utils.ApiPathConstants.DIRECTORS_PATH)
@Slf4j
public class DirectorController {
    private final DirectorService service;

    @GetMapping
    public List<Director> getAll() {
        log.info("Был запрошен список всех режиссёров");
        return service.getAll();
    }

    @GetMapping(ApiPathConstants.BY_ID_PATH)
    public Director getById(@PathVariable int id) {
        log.info("Был запрошен режиссёр c id {}", id);
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Director add(@Valid @RequestBody Director director) {
        log.info("Запрошено добавление режиссёра {}", director);
        return service.add(director);
    }

    @PutMapping
    public Director update(@Valid @RequestBody Director director) {
        log.info("Запрошено обновление режиссёра {}", director);
        return service.update(director);
    }

    @DeleteMapping(ApiPathConstants.BY_ID_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("Запрошено удаление режиссёра с id {}", id);
        service.delete(id);
    }
}