package com.neu.prattle.controller;

import com.neu.prattle.exceptions.GroupAlreadyPresentException;
import com.neu.prattle.exceptions.GroupDoesNotExistException;
import com.neu.prattle.exceptions.UserAlreadyPresentInGroupException;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.Moderator;
import com.neu.prattle.model.User;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.GroupServiceImpl;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;


import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(value = "/group")
public class GroupController {

  // Usually Dependency injection will be used to inject the service at run-time
  private GroupService groupService = GroupServiceImpl.getInstance();
  private UserService userService = UserServiceImpl.getInstance();

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
        }catch (GroupDoesNotExistException e ){
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
//        try{
//            User user = userService.findUserByUsername(username);
//            Group group = groupService.getGroupByName(groupName);
//            groupService.removeUser(group, user);
//        }catch (){
//
//        }
    return Response.ok().build();
  }

  @POST
  @Path("/addModerator/{groupName}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addModerator(@PathParam("groupName") String groupName, Moderator moderator) {

    Group group = groupService.getGroupByName(groupName);
    groupService.addModerator(group, moderator);

    return Response.ok().build();
  }

  @POST
  @Path("/removeModerator")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response removeModerator(Group group, Moderator moderator) {

    groupService.removeModerator(group, moderator);

    return Response.ok().build();
  }

  @POST
  @Path("/updateGroup")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response updateGroup(Group group) {

    groupService.updateGroup(group);

    return Response.ok().build();
  }

  @POST
  @Path("/deleteGroup")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response deleteGroup(Group group) {

    groupService.deleteGroup(group);

    return Response.ok().build();
  }

  @GET
  @Path("/getAll")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getAllGroups() {

    groupService.getAllGroups();

    List<Response> responses = new ArrayList<>();
    GenericEntity<List> list = new GenericEntity<List>(responses) {
    };
    list.getEntity().add(groupService.getAllGroups());
    return Response.status(200).entity(list).build();
  }


  @GET
  @Path("/getAllUserGroups/{username}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getAllUserGroups(@PathParam("username") String username) {

    groupService.getAllGroupsByUsername(username);

    List<Response> responses = new ArrayList<>();
    GenericEntity<List> list = new GenericEntity<List>(responses) {
    };
    list.getEntity().add(groupService.getAllGroupsByUsername(username));
    return Response.status(200).entity(list).build();
  }

  @GET
  @Path("/getGroupDetails/{groupName}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getGroupDetails(@PathParam("groupName") String groupName) {

    groupService.getGroupByName(groupName);

    List<Response> responses = new ArrayList<>();
    GenericEntity<List> list = new GenericEntity<List>(responses) {
    };
    list.getEntity().add(groupService.getGroupByName(groupName));
    return Response.status(200).entity(list).build();
  }
}
