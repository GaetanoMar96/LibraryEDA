package com.kafka.api.controller;

import com.kafka.api.LibraryEventsApi;
import com.kafka.domain.LibraryEvent;
import com.kafka.enums.LibraryEventType;
import com.kafka.exception.KafkaToJsonException;
import com.kafka.producer.LibraryEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LibraryEventsController implements LibraryEventsApi {

  private final LibraryEventProducer libraryEventProducer;

  @Override
  public ResponseEntity<LibraryEvent> postLibraryEvent(LibraryEvent libraryEvent) throws KafkaToJsonException {
    libraryEvent.setLibraryEventType(LibraryEventType.NEW.toString());
    libraryEventProducer.sendLibraryEventProducerRecord(libraryEvent);
    return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
  }

  @Override
  public ResponseEntity<LibraryEvent> putLibraryEvent(LibraryEvent libraryEvent) throws KafkaToJsonException {
    if (libraryEvent.getLibraryEventId() == null)
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(libraryEvent);

    libraryEvent.setLibraryEventType(LibraryEventType.UPDATE.toString());
    libraryEventProducer.sendLibraryEventProducerRecord(libraryEvent);
    return ResponseEntity.status(HttpStatus.OK).body(libraryEvent);
  }
}
