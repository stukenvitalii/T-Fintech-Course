package edu.tbank.hw5.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tbank.hw5.dto.EventDto;
import edu.tbank.hw5.dto.mapper.EventMapper;
import edu.tbank.hw5.entity.Event;
import edu.tbank.hw5.repository.jpa.EventRepositoryJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EventService{

    private final EventMapper eventMapper;

    private final EventRepositoryJpa eventRepositoryJpa;

    private final ObjectMapper objectMapper;

    public List<EventDto> getList() {
        List<Event> events = eventRepositoryJpa.findAll();
        return events.stream()
                .map(eventMapper::toDto)
                .toList();
    }

    public EventDto getOne(Long id) {
        Optional<Event> eventOptional = eventRepositoryJpa.findById(id);
        return eventMapper.toDto(eventOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));
    }

    public List<EventDto> getMany(List<Long> ids) {
        List<Event> events = eventRepositoryJpa.findAllById(ids);
        return events.stream()
                .map(eventMapper::toDto)
                .toList();
    }

    public EventDto create(EventDto dto) {
        Event event = eventMapper.toEntity(dto);
        Event resultEvent = eventRepositoryJpa.save(event);
        return eventMapper.toDto(resultEvent);
    }

    public EventDto patch(Long id, JsonNode patchNode) throws IOException {
        Event event = eventRepositoryJpa.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        EventDto eventDto = eventMapper.toDto(event);
        objectMapper.readerForUpdating(eventDto).readValue(patchNode);
        eventMapper.updateWithNull(eventDto, event);

        Event resultEvent = eventRepositoryJpa.save(event);
        return eventMapper.toDto(resultEvent);
    }

    public List<Long> patchMany(List<Long> ids, JsonNode patchNode) throws IOException {
        Collection<Event> events = eventRepositoryJpa.findAllById(ids);

        for (Event event : events) {
            EventDto eventDto = eventMapper.toDto(event);
            objectMapper.readerForUpdating(eventDto).readValue(patchNode);
            eventMapper.updateWithNull(eventDto, event);
        }

        List<Event> resultEvents = eventRepositoryJpa.saveAll(events);
        return resultEvents.stream()
                .map(Event::getId)
                .toList();
    }

    public EventDto delete(Long id) {
        Event event = eventRepositoryJpa.findById(id).orElse(null);
        if (event != null) {
            eventRepositoryJpa.delete(event);
        }
        return eventMapper.toDto(event);
    }

    public void deleteMany(List<Long> ids) {
        eventRepositoryJpa.deleteAllById(ids);
    }
}
