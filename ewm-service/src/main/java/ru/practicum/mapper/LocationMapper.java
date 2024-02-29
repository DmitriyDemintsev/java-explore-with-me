package ru.practicum.mapper;

import ru.practicum.dto.location.LocationDto;
import ru.practicum.model.Location;

public class LocationMapper {

    public static LocationDto toLocationDto(Location location) {
        LocationDto locationDto = new LocationDto(
                location.getLat(),
                location.getLon());
        return locationDto;
    }

    public static Location toLocation(LocationDto locationDto) {
        Location location = new Location(
                locationDto.getLat(),
                locationDto.getLon());
        return location;
    }

}
