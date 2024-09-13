package ru.clevertec.topic_management_system.broker;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class Topic {
    @Getter
    private final String name;
    private final List<String> messages;
    private final Lock messageListLock;
    private final Condition newMessage;

    public Topic(String name, List<String> messages) {
        this.name = name;
        this.messages = messages;
        this.messageListLock = new ReentrantLock();
        this.newMessage = this.messageListLock.newCondition();
    }

    public void addMessage(String message) {
        messageListLock.lock();
        try {
            this.messages.add(message);
            log.info(Thread.currentThread().getName() + " " + this.getName() + this.messages);
            this.newMessage.signalAll();
        } finally {
            messageListLock.unlock();
        }
    }

    public String getMessage(int index) {
        this.messageListLock.lock();
        try {
            while (!hasNextMessage(index)) {
                log.info(Thread.currentThread().getName() + " waits");
                this.newMessage.await();
                log.info(Thread.currentThread().getName() + " woke up");
            }
            String message = this.messages.get(index);
            log.info(Thread.currentThread().getName() + " " + message);
            return message;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException();
        } finally {
            this.messageListLock.unlock();
        }
    }

    private boolean hasNextMessage(int index) {
        return this.messages.size() > index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Topic topic = (Topic) o;
        return Objects.equals(name, topic.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
