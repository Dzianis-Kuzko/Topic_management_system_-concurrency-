package ru.clevertec.topic_management_system.broker;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class TopicBroker {
    private final Map<String, Topic> topics;
    private final Lock topicListLock;

    public TopicBroker(Map<String, Topic> topics) {
        this.topics = topics;
        this.topicListLock = new ReentrantLock();
    }

    public void addTopic(Topic topic) {
        topicListLock.lock();
        try {
            if (this.topics.containsKey(topic.getName())) {
                throw new IllegalArgumentException("A topic with '" + topic.getName() + "' name already exists");
            }
            this.topics.put(topic.getName(), topic);
            log.info(topics.toString());
        } finally {
            topicListLock.unlock();
        }
    }

    public void addMessageToTopic(String topicName, String message) {
        Topic topic = this.topics.get(topicName);
        topic.addMessage(message);
    }

    public String getMessageFromTopic(String topicName, int messageIndex) {
        Topic topic = this.topics.get(topicName);
        return topic.getMessage(messageIndex);
    }
}
