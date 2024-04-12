package com.busvia.userservice.client;

import com.busvia.userservice.exception.CustomException;
import com.busvia.userservice.model.UserRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface AuthClient {

    @PostExchange("/auth/register")
     String addNewUser(@RequestBody UserRequest userRequest);

    @PostExchange("/auth/register")
    String updateUser(@RequestBody UserRequest userRequest);


    @PostExchange("/auth/blockUser")
    String blockUser(@RequestBody UserRequest userRequest);


    @PostExchange("/auth/unblockUser")
    String unblockUser(@RequestBody UserRequest userRequest);

    default void fallback(Exception e){
        throw new CustomException("Fallback response for adding a new user.");
    }


}
