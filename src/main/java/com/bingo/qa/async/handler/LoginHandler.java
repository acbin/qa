package com.bingo.qa.async.handler;

import com.bingo.qa.async.EventHandler;
import com.bingo.qa.async.EventModel;
import com.bingo.qa.async.EventType;

import java.util.List;

public class LoginHandler implements EventHandler{
    @Override
    public void doHandler(EventModel model) {

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return null;
    }
}
