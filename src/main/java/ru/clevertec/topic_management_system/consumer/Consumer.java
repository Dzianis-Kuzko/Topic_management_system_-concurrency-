package ru.clevertec.topic_management_system.consumer;

import lombok.extern.slf4j.Slf4j;
import ru.clevertec.topic_management_system.broker.TopicBroker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class Consumer implements Runnable {
    private final TopicBroker topicBroker;
    private final String topicName;
    private final List<String> messages;
    private CountDownLatch latch;
    private int nextIndex;

    public Consumer(TopicBroker topicBroker, String topicName) {
        this.topicBroker = topicBroker;
        this.topicName = topicName;
        this.nextIndex = 0;
        this.messages = new ArrayList<>();
    }

    public Consumer(TopicBroker topicBroker, String topicName, CountDownLatch latch, List<String> messages) {
        this.topicBroker = topicBroker;
        this.topicName = topicName;
        this.nextIndex = 0;
        this.latch = latch;
        this.messages = messages;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            final String message = this.topicBroker.getMessageFromTopic(topicName, nextIndex);
            messages.add(message);
            if (latch != null) {
                this.latch.countDown();
                if (this.latch.getCount() == 0) {
                    Thread.currentThread().interrupt();
                }
            }
            nextIndex++;
            log.info(Thread.currentThread().getName() + " " + messages);
        }
    }
}
