package com.kafka.exception;

public class KafkaToJsonException extends Exception {
    public KafkaToJsonException(String errorMessage) {
        super(errorMessage);
    }
}
