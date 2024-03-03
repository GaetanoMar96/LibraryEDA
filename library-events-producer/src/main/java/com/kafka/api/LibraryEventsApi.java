package com.kafka.api;

import com.kafka.domain.LibraryEvent;
import com.kafka.exception.KafkaToJsonException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(value = "events-producer", tags = { "events-producer" })
@RequestMapping("/v1/libraryevent")
public interface LibraryEventsApi {

    @ApiOperation(value = "Create a library event e send it to kafka topic", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "BadRequest"),
            @ApiResponse(code = 405, message = "Method not allowed")})
    @PostMapping()
    ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody @Valid LibraryEvent obj) throws KafkaToJsonException;

    @ApiOperation(value = "Update a library event e send it to kafka topic", response = String.class)
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 400, message = "BadRequest"),
        @ApiResponse(code = 405, message = "Method not allowed")})
    @PutMapping()
    ResponseEntity<LibraryEvent> putLibraryEvent(@RequestBody @Valid LibraryEvent obj) throws KafkaToJsonException;

}
