package com.neu.prattle.controller;

import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.exceptions.UserDoesNotExistException;
import com.neu.prattle.model.User;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.GET;

/***
 * A Resource class responsible for handling CRUD operations
 * on User objects.
 *
 * @author CS5500 Fall 2019 Teaching staff
 * @version dated 2019-10-06
 */
@Path(value = "/user")
public class UserController {

  // Usually Dependency injection will be used to inject the service at run-time
  private UserService accountService = UserServiceImpl.getInstance();

    /***
     * Handles a HTTP POST request for user creation
     *
     * @param user -> The User object decoded from the payload of POST request.
     * @return -> A Response indicating the outcome of the requested operation.
     */
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUserAccount(User user) {
        try {
            accountService.addUser(user);
        } catch (UserAlreadyPresentException e) {
            return Response.status(409).build();
        }

        return Response.ok().build();
  }

    /**
     * Handles a HTTP GET request for user information.
     *
     * @param username -> The User's username.
     * @return -> A Response indicating the user's information.
     */
    @GET
    @Path("/getUser/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("username") String username) {
        User user;
        try{
            user = accountService.findUserByUsername(username);

            if(user == null){
                throw new UserDoesNotExistException("User does not exist.");
            }
        }catch (UserDoesNotExistException e ){
            return Response.status(409).build();
        }

        return Response.ok().entity(user).build();
    }


  @GET
  @Path("/search/{keyword}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response searchResult(@PathParam("keyword") String keyword) {
    List resultUsers;
    resultUsers = accountService.searchUser(keyword);
    return Response.ok().entity(resultUsers).build();
  }


}
