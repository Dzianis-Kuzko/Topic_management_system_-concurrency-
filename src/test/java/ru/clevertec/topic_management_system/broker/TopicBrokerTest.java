package ru.clevertec.topic_management_system.broker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TopicBrokerTest {
    private final static String TOPIC_NAME = "Clients";
    private final static String MESSAGE = "New message";

    @Spy
    private Map<String, Topic> topics = new HashMap<>();
    private TopicBroker topicBroker;

    @Mock
    private Topic topicMock;

    @BeforeEach
    void SetUp() {
        topicBroker = new TopicBroker(topics);
    }

    @Test
    void addTopicSuccess() {
        Topic topic = new Topic(TOPIC_NAME, new ArrayList<>());
        Map<String, Topic> expectedTopics = new HashMap<>();
        expectedTopics.put(topic.getName(), topic);
        topicBroker.addTopic(topic);

        assertTrue(topics.containsKey(TOPIC_NAME));
        assertEquals(expectedTopics, topics);
    }

    @Test
    void shouldThrowExceptionWhenTopicAlreadyExists() {
        Topic topic = new Topic(TOPIC_NAME, new ArrayList<>());
        topicBroker.addTopic(topic);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            topicBroker.addTopic(topic);
        });

        assertEquals("A topic with '" + TOPIC_NAME + "' name already exists", exception.getMessage());
    }

    @Test
    void addMessageToTopicSuccess() {
        when(topics.get(TOPIC_NAME)).thenReturn(topicMock);
        topicBroker.addMessageToTopic(TOPIC_NAME, MESSAGE);

        verify(topicMock).addMessage(MESSAGE);
    }

    @Test
    void getMessageFromTopicSuccess() {
        int messageIndex = 0;
        when(topics.get(TOPIC_NAME)).thenReturn(topicMock);
        when(topicMock.getMessage(messageIndex)).thenReturn(MESSAGE);
        String result = topicBroker.getMessageFromTopic(TOPIC_NAME, messageIndex);

        assertEquals(MESSAGE, result);
        verify(topicMock).getMessage(messageIndex);
    }
}