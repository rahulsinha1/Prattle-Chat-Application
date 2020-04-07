package com.neu.prattle.controller;

import com.neu.prattle.exceptions.UserDoesNotExistException;
import com.neu.prattle.model.User;
import com.neu.prattle.service.MessageService;
import com.neu.prattle.service.MessageServiceImpl;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(value = "/message")
public class MessageController {

  private MessageService messageService = MessageServiceImpl.getInstance();
  private UserService userService = UserServiceImpl.getInstance();

  @GET
  @Path("/getMessages/{username}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getAllUserConversation(@PathParam("username") String username) {
    User user = userService.findUserByUsername(username);
    if(user!=null) {
      List listofAllMessages;
      listofAllMessages = messageService.getMessages(username);
      return Response.status(200).entity(listofAllMessages).build();
    }
    throw new UserDoesNotExistException("User does not exist with name" + username);
  }
}
