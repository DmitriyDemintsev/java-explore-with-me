package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.service.compilation.CompilationService;

import java.util.List;

@Component
@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class CompilationPublicController {
    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false, defaultValue = "false")
                                                boolean pinned,
                                                @RequestParam(required = false, defaultValue = "0") int from,
                                                @RequestParam(required = false, defaultValue = "10") int size) {
        return CompilationMapper.toCompilationDtos(compilationService.getCompilations(pinned, PageRequest.of(from, size)));
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable Long compId) {
        log.info("Getting compilation with id={}", compId);
        return CompilationMapper.toCompilationDto(compilationService.getCompilationById(compId));
    }
}
