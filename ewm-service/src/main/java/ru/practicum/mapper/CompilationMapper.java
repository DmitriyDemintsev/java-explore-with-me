package ru.practicum.mapper;

import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.model.Compilation;

import java.util.ArrayList;
import java.util.List;

public class CompilationMapper {

    public static Compilation toNewCompilation(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(EventMapper.toShortDtos(compilation.getEvents()))
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

    public static List<CompilationDto> toCompilationDtos(List<Compilation> compilations) {
        List<CompilationDto> dtos = new ArrayList<>();
        for (Compilation compilation : compilations) {
            dtos.add(toCompilationDto(compilation));
        }
        return dtos;
    }
}
