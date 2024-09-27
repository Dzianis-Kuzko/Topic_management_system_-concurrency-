package ru.clevertec.topic_management_system.integration_test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.topic_management_system.broker.Topic;
import ru.clevertec.topic_management_system.broker.TopicBroker;
import ru.clevertec.topic_management_system.consumer.Consumer;
import ru.clevertec.topic_management_system.producer.MessageFactory;
import ru.clevertec.topic_management_system.producer.Producer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TopicManagementSystemIT {
    private final static String TOPIC_NAME = "Clients";

    private MessageFactory messageFactory;
    private Map<String, Topic> topicMap;
    private TopicBroker topicBroker;

    @BeforeEach
    void SetUp() {
        messageFactory = new MessageFactory();
        topicMap = new HashMap<>();
        topicBroker = new TopicBroker(topicMap);
    }

    @Test
    void runTwoProducersAndTwoConsumersSuccessIT() throws InterruptedException {
        topicBroker.addTopic(new Topic(TOPIC_NAME, new ArrayList<>()));
        CountDownLatch latch0 = new CountDownLatch(5);
        CountDownLatch latch1 = new CountDownLatch(5);
        List<String> messages0 = new ArrayList<>();
        List<String> messages1 = new ArrayList<>();

        Thread producer0 = new Thread(new Producer(topicBroker, TOPIC_NAME, messageFactory));
        Thread producer1 = new Thread(new Producer(topicBroker, TOPIC_NAME, messageFactory));
        Thread consumer0 = new Thread(new Consumer(topicBroker, TOPIC_NAME, latch0, messages0));
        Thread consumer1 = new Thread(new Consumer(topicBroker, TOPIC_NAME, latch1, messages1));

        startThreads(producer0, producer1, consumer0, consumer1);
        latch0.await();
        latch1.await();

        assertEquals(messages0, messages1);
    }

    private static void startThreads(final Thread... threads) {
        Arrays.stream(threads).forEach(Thread::start);
    }
}