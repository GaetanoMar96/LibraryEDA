package com.kafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.entity.LibraryEvent;
import com.kafka.jpa.LibraryEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LibraryEventService {

    private final ObjectMapper mapper;

    private final LibraryEventRepository repository;

    public void processLibraryEvent(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        LibraryEvent libraryEvent = mapper.readValue(consumerRecord.value(), LibraryEvent.class);
        switch (libraryEvent.getLibraryEventType()) {
            case "NEW":
                save(libraryEvent);
                break;
            case "UPDATE":
                validate(libraryEvent);
                break;
            default:
                log.info("Event type does not exist");
        }
    }

    public void save(LibraryEvent libraryEvent) {
        libraryEvent.getBook().setLibraryEvent(libraryEvent);
        repository.save(libraryEvent);
        log.info("Successfully added record into db");
    }

    public void validate(LibraryEvent libraryEvent) {
        if (libraryEvent.getLibraryEventId() == null)
            throw new IllegalArgumentException();

    }
}
