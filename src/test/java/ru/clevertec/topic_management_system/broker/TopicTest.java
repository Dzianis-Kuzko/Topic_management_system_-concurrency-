package ru.clevertec.topic_management_system.broker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class TopicTest {
    private final static String TOPIC_NAME = "Clients";
    private final static String MESSAGE = "New message";

    private Topic topic;
    private List<String> messages;

    @BeforeEach
    void SetUp() {
        messages = new ArrayList<>();
        topic = new Topic(TOPIC_NAME, messages);
    }

    @Test
    void addMessageSuccess() {
        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add(MESSAGE);
        topic.addMessage(MESSAGE);

        assertEquals(expectedMessages, messages);
    }

    @Test
    void getMessageSuccess() {
        int messageIndex = 0;
        messages.add(MESSAGE);
        String expectedMessage = topic.getMessage(messageIndex);

        assertEquals(MESSAGE, expectedMessage);
    }

    @Test
    void testHashCodeSuccess() {
        Topic topic1 = new Topic(TOPIC_NAME, new ArrayList<>());
        topic1.addMessage(MESSAGE);
        Topic topic2 = new Topic(TOPIC_NAME, new ArrayList<>());
        assertEquals(topic1.hashCode(), topic2.hashCode());
    }

    @Test
    void testEqualsSuccess() {
        Topic topic1 = new Topic(TOPIC_NAME, new ArrayList<>());
        topic1.addMessage(MESSAGE);
        Topic topic2 = new Topic(TOPIC_NAME, new ArrayList<>());

        assertTrue(topic1.equals(topic2));
        assertTrue(topic2.equals(topic1));
    }
}