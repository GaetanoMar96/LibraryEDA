package com.kafka.util;

import com.kafka.domain.Book;
import com.kafka.domain.LibraryEvent;
import com.kafka.enums.LibraryEventType;

public class CreateCommonObjects {

    public static LibraryEvent createLibraryEvent() {
        LibraryEvent libraryEvent = new LibraryEvent();
        libraryEvent.setLibraryEventId(1);
        libraryEvent.setLibraryEventType(LibraryEventType.NEW.toString());
        libraryEvent.setBook(createBook());
        return libraryEvent;
    }

    public static Book createBook() {
        Book book = new Book();
        book.setBookId(1);
        book.setBookName("Harry Potter");
        book.setBookAuthor("J. K. Rowling");
        return book;
    }
}
