package com.neu.prattle.controller;

import com.neu.prattle.exceptions.FollowException;
import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.exceptions.UserDoesNotExistException;
import com.neu.prattle.model.User;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.PathParam;


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
    List<User> resultUsers;
    try{
        resultUsers = accountService.searchUser(keyword);
    }catch (UserDoesNotExistException e){
        return Response.status(409).build();
    }

    return Response.ok().entity(resultUsers).build();
  }

  @POST
  @Path("/followUser/{followerName}/{userToFollow}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response followUser(@PathParam("followerName") String followerName, @PathParam("userToFollow") String userToFollow) {
    try {
      User follower = accountService.findUserByUsername(followerName);
      User toFollow = accountService.findUserByUsername(userToFollow);
      accountService.followUser(follower, toFollow);
    }  catch (FollowException e){
      return Response.status(409, e.getMessage()).build();
    }
    return Response.status(200,"User Successfully Followed and is now in your circle.").build();
  }


  @POST
  @Path("/followUser/{followerName}/{userToUnfollow}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response unfollowUser(@PathParam("followerName") String followerName, @PathParam("userToUnfollow") String userToUnfollow) {
    try {
      User follower = accountService.findUserByUsername(followerName);
      User toUnfollow = accountService.findUserByUsername(userToUnfollow);
      accountService.followUser(follower, toUnfollow);
    }  catch (FollowException e){
      return Response.status(409, e.getMessage()).build();
    }
    return Response.status(200,"User Successfully UnFollowed and is no longer in your circle.").build();
  }


  @GET
  @Path("/getFollowers/{user}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getFollowers(@PathParam("user") String username) {
      List<User> followers;
    try{
      followers= accountService.getFollowers(username);
    }
    catch (UserDoesNotExistException e){
      return Response.status(500,e.getMessage()).build();
    }

    return Response.ok().entity(followers).build();
  }



  @GET
  @Path("/getFollowing/{user}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getFollowing(@PathParam("user") String username) {
    List<User> followers;
    try{
      followers= accountService.getFollowing(username);
    }
    catch (UserDoesNotExistException e){
      return Response.status(500,e.getMessage()).build();
    }

    return Response.ok().entity(followers).build();
  }


  @POST
  @Path("/setStatus/{username}/{status}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response setStatus(@PathParam("username") String username, @PathParam("status") String status) {
    try {
      accountService.setStatus(username, status);
    }  catch (UserDoesNotExistException e){
      return Response.status(409, e.getMessage()).build();
    }
    return Response.status(200,"User Successfully updated Status").build();
  }


}
