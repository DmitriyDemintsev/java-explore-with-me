package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.service.compilation.CompilationService;

import javax.validation.Valid;

@Component
@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class CompilationAdminController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        return CompilationMapper.toCompilationDto(compilationService.create(CompilationMapper
                .toNewCompilation(newCompilationDto), newCompilationDto.getEvents()));
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@PathVariable Long compId,
                                            @Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Updating compilation {} with id={}", newCompilationDto, compId);
        return CompilationMapper.toCompilationDto(compilationService.update(CompilationMapper
                .toNewCompilation(newCompilationDto), newCompilationDto.getEvents(), compId));
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        compilationService.delete(compId);
    }
}
