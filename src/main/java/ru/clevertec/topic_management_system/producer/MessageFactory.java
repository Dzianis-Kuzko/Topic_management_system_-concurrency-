package ru.clevertec.topic_management_system.producer;

public class MessageFactory {
    private final int INITIAL_NEXT_MESSAGE_INDEX = 1;
    private static final String TEMPLATE_CREATED_MESSAGE_DATA = "message#%d";
    private int nextMessageIndex;

    public MessageFactory() {
        this.nextMessageIndex = INITIAL_NEXT_MESSAGE_INDEX;
    }

    public String create() {
        return String.format(TEMPLATE_CREATED_MESSAGE_DATA, findAndIncrementNextMessageIndex());
    }

    private synchronized int findAndIncrementNextMessageIndex() {
        return this.nextMessageIndex++;
    }
}
