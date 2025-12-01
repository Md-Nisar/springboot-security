package com.mna.springbootsecurity.event.core.service;

import com.mna.springbootsecurity.event.core.model.Event;

public interface EventManager {

    void triggerEvent(Event event);

}

