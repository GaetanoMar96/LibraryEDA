package com.kafka.controller;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.domain.Book;
import com.kafka.domain.LibraryEvent;
import com.kafka.enums.LibraryEventType;
import com.kafka.producer.LibraryEventProducer;
import com.kafka.util.CreateCommonObjects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

@WebMvcTest
@AutoConfigureMockMvc
class LibraryEventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    LibraryEventProducer producer;

    @Test
    void postLibraryEvent_expected201() throws Exception {

        Book book =
            Book.builder().
                bookId(1).
                bookName("Harry Potter").
                bookAuthor("J. K. Rowling").build();

        LibraryEvent libraryEvent =
            LibraryEvent.builder().
                libraryEventId(1).
                libraryEventType(LibraryEventType.NEW.toString()).
                book(book).build();

        String json = mapper.writeValueAsString(libraryEvent);
        doNothing().when(producer).sendLibraryEventProducerRecord(libraryEvent);

        mockMvc.perform(post("/v1/libraryevent/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)).andExpect(status().isCreated());
    }

    @Test
    void putLibraryEvent_expected200() throws Exception {

        Book book =
            Book.builder().
                bookId(1).
                bookName("Harry Potter").
                bookAuthor("J. K. Rowling").build();

        LibraryEvent libraryEvent =
            LibraryEvent.builder().
                libraryEventId(1).
                libraryEventType(LibraryEventType.UPDATE.toString()).
                book(book).build();

        String json = mapper.writeValueAsString(libraryEvent);
        doNothing().when(producer).sendLibraryEventProducerRecord(libraryEvent);

        mockMvc.perform(put("/v1/libraryevent/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)).andExpect(status().isOk());
    }

    @Test
    void putLibraryEvent_expected400() throws Exception {

        Book book =
            Book.builder().
                bookId(1).
                bookName("Harry Potter").
                bookAuthor("J. K. Rowling").build();

        LibraryEvent libraryEvent =
            LibraryEvent.builder().
                libraryEventId(null).
                libraryEventType(LibraryEventType.UPDATE.toString()).
                book(book).build();

        String json = mapper.writeValueAsString(libraryEvent);
        doNothing().when(producer).sendLibraryEventProducerRecord(libraryEvent);

        mockMvc.perform(put("/v1/libraryevent/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)).andExpect(status().isBadRequest());
    }

}
