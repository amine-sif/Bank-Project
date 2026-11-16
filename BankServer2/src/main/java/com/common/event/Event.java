package com.common.event;
/**
 * This class represents an event. An event has a source (Object), is characterized by its type (String)
 * and can have arguments grouped in a string (String).
 */
public class Event {
    private Object source;
    private final String type, argument;

    /**
     * Builds an event.
     *
     * @param source the source of the event
     * @param type the type of the event
     * @param argument String containing the arguments of the event
     */
    public Event(Object source, String type, String argument) {
        this.source = source;
        this.type = type;
        this.argument = argument;
    }

    /**
     * Returns the source of the event
     *
     * @return Object source of the event
     */
    public Object getSource() {
        return source;
    }
    /**
     * Returns the type of the event
     *
     * @return String type of the event
     */
    public String getType() {
        return type;
    }
    /**
     * Returns the argument of the event
     *
     * @return String argument of the event
     */
    public String getArgument() {
        return argument;
    }
}
