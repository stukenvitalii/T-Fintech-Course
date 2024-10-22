package edu.tbank.hw5.controller;
import com.fasterxml.jackson.databind.JsonNode;
import edu.tbank.hw5.dto.EventDto;
import edu.tbank.hw5.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/rest/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public List<EventDto> getList() {
        return eventService.getList();
    }

    @GetMapping("/{id}")
    public EventDto getOne(@PathVariable Long id) {
        return eventService.getOne(id);
    }

    @GetMapping("/by-ids")
    public List<EventDto> getMany(@RequestParam List<Long> ids) {
        return eventService.getMany(ids);
    }

    @PostMapping
    public EventDto create(@RequestBody EventDto dto) {
        return eventService.create(dto);
    }

    @PatchMapping("/{id}")
    public EventDto patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        return eventService.patch(id, patchNode);
    }

    @PatchMapping
    public List<Long> patchMany(@RequestParam @Valid List<Long> ids, @RequestBody JsonNode patchNode) throws IOException {
        return eventService.patchMany(ids, patchNode);
    }

    @DeleteMapping("/{id}")
    public EventDto delete(@PathVariable Long id) {
        return eventService.delete(id);
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        eventService.deleteMany(ids);
    }
}
