package com.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.domain.LibraryEvent;
import com.kafka.exception.KafkaToJsonException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaFailureCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.ExecutionException;

@Component
@Slf4j
@RequiredArgsConstructor
public class LibraryEventProducer {

    private final KafkaTemplate<Integer, String> kafkaTemplate;

    private final ObjectMapper mapper;

    String topic = "library-events";

    /** Method async which uses lambda expressions to send data using kafka.
     * @param libraryEvent library event
     * @throws KafkaToJsonException exception
     */
    public void sendLibraryEventAsyncLambdas(LibraryEvent libraryEvent) throws KafkaToJsonException {
        try {
            Integer key = libraryEvent.getLibraryEventId();
            String value = mapper.writeValueAsString(libraryEvent);

            ListenableFuture<SendResult<Integer, String>> future = kafkaTemplate.send(topic, key, value);
            future.addCallback(result -> {
                assert result != null;
                handleSuccess(key, value, result);
            }, (KafkaFailureCallback<Integer, String>) ex -> handleFailure(key, value, ex));
        } catch (JsonProcessingException ex) {
            throw new KafkaToJsonException("unable to parse the object into json");
        }
    }

    /** Method sync to send data using kafka.
     * @param libraryEvent library event
     * @throws KafkaToJsonException exception
     */
    public void sendToKafkaSync(LibraryEvent libraryEvent) throws KafkaToJsonException {
        try {
            Integer key = libraryEvent.getLibraryEventId();
            String value = mapper.writeValueAsString(libraryEvent);
            final ProducerRecord<Integer, String> producerRecord = buildProducerRecord(key, value, topic);
            ListenableFuture<SendResult<Integer, String>> listenableFuture = kafkaTemplate.send(producerRecord);
            handleSuccess(key, value, listenableFuture.get());
        }
        catch (ExecutionException | InterruptedException e) {
            handleFailure(null, null, e);
        } catch (JsonProcessingException e) {
            throw new KafkaToJsonException("unable to parse the object into json");
        }
    }

    public void sendLibraryEventProducerRecord(LibraryEvent libraryEvent) throws KafkaToJsonException {

        try {
            Integer key = libraryEvent.getLibraryEventId();
            String value = mapper.writeValueAsString(libraryEvent);
            ProducerRecord<Integer, String> producerRecord = buildProducerRecord(key, value, topic);
            ListenableFuture<SendResult<Integer, String>> listenableFuture = kafkaTemplate.send(producerRecord);
            addCallBack(key, value, listenableFuture);
        } catch (JsonProcessingException ex) {
            throw new KafkaToJsonException("unable to parse the object into json");
        }
    }

    private ProducerRecord<Integer, String> buildProducerRecord(Integer key, String value, String topic) {
        return new ProducerRecord<>(topic, null, key, value, null);
    }

    private void addCallBack(Integer key,
        String value,
        ListenableFuture<SendResult<Integer, String>> listenableFuture) {
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                handleFailure(key, value, ex);
            }

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                handleSuccess(key, value, result);
            }
        });
    }

    private void handleSuccess(Integer key, String value, SendResult<Integer, String> result) {
        log.info("Message sent successfully for key = {} and value = {}, partition is = {}",
                 key, value, result.getRecordMetadata().partition());
    }

    private void handleFailure(Integer key, String value, Throwable ex) {
        try {
            throw ex;
        } catch (Throwable throwable) {
            log.error("log is on failure {}", ex.getMessage());
        }
    }
}
