package org.example.application.controllers;

import org.example.application.dto.RequestDto;
import org.example.application.dto.ResponseDto;
import org.example.application.services.NthSmallestNumberService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
public class Controller {
    private final NthSmallestNumberService service;

    public Controller(NthSmallestNumberService service) {
        this.service = service;
    }

    @PostMapping("/")
    public ResponseDto post(@RequestBody RequestDto requestDto) {
        try {
            return service.findNthSmallestNumber(requestDto);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Не удалось прочитать файл", e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неверный ввод!", e);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера!", e);
        }
    }
}

