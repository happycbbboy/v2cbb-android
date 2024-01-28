package com.happycbbboy.domain;

public class EventBusConstant {
    EventBusMsg eventBusMsg;

    private Integer id;

    public EventBusConstant(EventBusMsg eventBusMsg, Integer id) {
        this.eventBusMsg = eventBusMsg;
        this.id = id;
    }

    public EventBusMsg getEventBusMsg() {
        return eventBusMsg;
    }

    public void setEventBusMsg(EventBusMsg eventBusMsg) {
        this.eventBusMsg = eventBusMsg;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
