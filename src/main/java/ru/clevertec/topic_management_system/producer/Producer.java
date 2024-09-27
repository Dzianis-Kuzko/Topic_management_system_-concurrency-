package ru.clevertec.topic_management_system.producer;

import ru.clevertec.topic_management_system.broker.TopicBroker;

public class Producer implements Runnable {
    private final TopicBroker topicBroker;
    private final MessageFactory messageFactory;
    private final String topicName;

    public Producer(TopicBroker topicBroker, String topicName, MessageFactory messageFactory) {
        this.topicBroker = topicBroker;
        this.messageFactory = messageFactory;
        this.topicName = topicName;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            final String message = this.messageFactory.create();
            this.topicBroker.addMessageToTopic(topicName, message);
        }
    }
}
