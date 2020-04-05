package com.neu.prattle.controller;

import com.neu.prattle.exceptions.*;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.GroupServiceImpl;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(value = "/group")
public class GroupController {

  // Usually Dependency injection will be used to inject the service at run-time
  private UserService userService = UserServiceImpl.getInstance();
  private GroupService groupService = GroupServiceImpl.getInstance();


    /***
   * Handles a HTTP POST request for user creation
   *
   * @param group -> The User object decoded from the payload of POST request.
   * @return -> A Response indicating the outcome of the requested operation.
   */
  @POST
  @Path("/create")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createGroup(Group group){
      try {
          groupService.createGroup(group);
      } catch (GroupAlreadyPresentException e) {
          return Response.status(409,e.getMessage()).build();
      }

      return Response.status(200,"Group Successfully Created.").build();
  }

    /**
     * Handles a HTTP GET request for user information.
     *
     * @param username -> The User's username.
     * @return -> A Response indicating the user's information.
     */
    @GET
    @Path("/getGroup/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getGroup(@PathParam("username") String username) {
        List userGroup;
        try{
            userGroup = groupService.getAllGroupsByUsername(username);
        }catch (UserDoesNotHaveAnyGroup e ){
            return Response.status(409, e.getMessage()).build();
        }
        return Response.status(200).entity(userGroup).build();
    }


  @POST
  @Path("/addUser/{groupName}/{username}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addUser(@PathParam("groupName") String groupName, @PathParam("username") String username) {
        try {
            User user = userService.findUserByUsername(username);
            Group group = groupService.getGroupByName(groupName);
            groupService.addUser(group, user);
        }  catch (UserAlreadyPresentInGroupException e){
            return Response.status(409, e.getMessage()).build();
        }
    return Response.status(200,"User Successfully Added In Group.").build();
  }

  @POST
  @Path("/removeUser/{groupName}/{username}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response removeUser(@PathParam("groupName") String groupName, @PathParam("username") String username) {
        try{
            User user = userService.findUserByUsername(username);
            Group group = groupService.getGroupByName(groupName);
            groupService.removeUser(group, user);
        }catch (UserDoesNotExistException e){
            return Response.status(409, e.getMessage()).build();
        }
    return Response.status(200, "User Removed From Group.").build();
  }

  @POST
  @Path("/addModerator/{groupName}/{moderator}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addModerator(@PathParam("groupName") String groupName, @PathParam("moderator") String moderator) {
        try{
            User user = userService.findUserByUsername(moderator);
            Group group = groupService.getGroupByName(groupName);
            groupService.addModerator(group,user);
        }catch (UserAlreadyPresentInGroupException e){
            return Response.status(409, e.getMessage()).build();
        }

      return Response.status(200,"User Successfully Added As Moderator.").build();
  }

  @POST
  @Path("/removeModerator/{groupName}/{moderator}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response removeModerator(@PathParam("groupName") String groupName, @PathParam("moderator") String moderator) {
      try{
          User user = userService.findUserByUsername(moderator);
          Group group = groupService.getGroupByName(groupName);
          groupService.removeModerator(group,user);
      }catch (UserDoesNotExistException e){
          return Response.status(409, e.getMessage()).build();
      }

      return Response.status(200,"User Successfully Removed As Moderator From Group.").build();
  }

  @POST
  @Path("/updateGroup/{groupName}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response updateGroup(@PathParam("groupName") String groupName) {
      try{
          Group group = groupService.getGroupByName(groupName);
          groupService.updateGroup(group);
      }catch (GroupDoesNotExistException e){
          return Response.status(409, e.getMessage()).build();
      }

      return Response.status(200,"Group Has Been Successfully Updated").build();
  }

  @POST
  @Path("/deleteGroup/{groupName}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response deleteGroup(@PathParam("groupName") String groupName) {
      try{
          groupService.getGroupByName(groupName);
          groupService.deleteGroup(groupName);
      }catch (GroupDoesNotExistException e){
          return Response.status(409, e.getMessage()).build();
      }

      return Response.status(200,"Group Has Been Successfully Deleted").build();
  }

  @GET
  @Path("/getAll")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getAllGroups() {
      List listOfAllGroup;
      listOfAllGroup = groupService.getAllGroups();
    return Response.status(200).entity(listOfAllGroup).build();
  }


  @GET
  @Path("/getAllUserGroups/{username}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getAllUserGroups(@PathParam("username") String username) {
        List groupByUsername;
        try{
            groupByUsername = groupService.getAllGroupsByUsername(username);
        }catch(UserDoesNotExistException e){
            return Response.status(409, e.getMessage()).build();
        }
    return Response.status(200).entity(groupByUsername).build();
  }

  @GET
  @Path("/getGroupDetails/{groupName}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getGroupDetails(@PathParam("groupName") String groupName) {
        Group groupDetails;
        try{
            groupDetails = groupService.getGroupByName(groupName);
        }catch (GroupDoesNotExistException e){
            return Response.status(409, e.getMessage()).build();
        }

    return Response.status(200).entity(groupDetails).build();
  }
}
