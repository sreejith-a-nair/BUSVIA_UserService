package com.busvia.userservice.client;

import com.busvia.userservice.model.BusRootRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

    @HttpExchange
    public interface BusRootClient {
        @PostExchange("/bus/addBusRootAndTime")
        String addBusRootAndTime(@RequestBody BusRootRequest busRootRequest);

        default void fallback(Exception e){
            throw new RuntimeException("Fallback response for Adding root and time   .");
        }

    }
