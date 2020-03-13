package com.neu.prattle.controller;

import com.neu.prattle.exceptions.GroupAlreadyPresentException;
import com.neu.prattle.exceptions.UserDoesNotExist;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.Moderator;
import com.neu.prattle.model.User;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.GroupServiceImpl;

import org.springframework.web.bind.annotation.RestController;

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
@RestController
public class GroupController {

  // Usually Dependency injection will be used to inject the service at run-time
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
  public Response createGroup(Group group) {
    try {
      groupService.createGroup(group);
    } catch (GroupAlreadyPresentException e) {
      return Response.status(409).build();
    }
    catch (UserDoesNotExist u) {
      return Response.status(409,u.getMessage()).build();
    }    return Response.ok().build();
  }

  @POST
  @Path("/addUser/{groupName}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addUser(@PathParam("groupName") String groupName, User user) {
    try {
      Group group = groupService.getGroupByName(groupName);
      groupService.addUser(group,user);
    } catch (GroupAlreadyPresentException e) {
      return Response.status(409).build();
    }
    return Response.ok().build();
  }

  @POST
  @Path("/removeUser")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response removeUser(Group group, User user) {
    try {
      groupService.removeUser(group,user);
    } catch (GroupAlreadyPresentException e) {
      return Response.status(409).build();
    }
    return Response.ok().build();
  }

  @POST
  @Path("/addModerator/{groupName}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addModerator(@PathParam("groupName") String groupName, Moderator moderator) {
    try {
      Group group = groupService.getGroupByName(groupName);
      groupService.addModerator(group,moderator);
    } catch (GroupAlreadyPresentException e) {
      return Response.status(409).build();
    }
    return Response.ok().build();
  }

  @POST
  @Path("/removeModerator")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response removeModerator(Group group, Moderator moderator) {
    try {
      groupService.removeModerator(group,moderator);
    } catch (GroupAlreadyPresentException e) {
      return Response.status(409).build();
    }
    return Response.ok().build();
  }

  @POST
  @Path("/updateGroup")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response updateGroup(Group group) {
    try {
      groupService.updateGroup(group);
    } catch (GroupAlreadyPresentException | UserDoesNotExist ex) {
      return Response.status(409).build();
    }
    return Response.ok().build();
  }

  @POST
  @Path("/deleteGroup")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response deleteGroup(Group group) {
    try {
      groupService.deleteGroup(group);
    } catch (GroupAlreadyPresentException e) {
      return Response.status(409).build();
    }
    return Response.ok().build();
  }

  @GET
  @Path("/getAll")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getAllGroups() {
    try {
      groupService.getAllGroups();
    } catch (GroupAlreadyPresentException e) {
      return Response.status(409).build();
    }
    List<Response> responses = new ArrayList<>();
    GenericEntity<List> list = new GenericEntity<List> (responses) {};
    list.getEntity().add(groupService.getAllGroups());
    return Response.status(200).entity(list).build();
  }


  @GET
  @Path("/getAllUserGroups/{username}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getAllUserGroups(@PathParam("username") String username) {
    try {
      groupService.getAllGroupsByUsername(username);
    } catch (GroupAlreadyPresentException e) {
      return Response.status(409).build();
    }
    List<Response> responses = new ArrayList<>();
    GenericEntity<List> list = new GenericEntity<List> (responses) {};
    list.getEntity().add(groupService.getAllGroupsByUsername(username));
    return Response.status(200).entity(list).build();
  }

  @GET
  @Path("/getGroupDetails/{groupName}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getGroupDetails(@PathParam("groupName") String groupName) {
    try {
      groupService.getGroupByName(groupName);
    } catch (GroupAlreadyPresentException e) {
      return Response.status(409).build();
    }
    List<Response> responses = new ArrayList<>();
    GenericEntity<List> list = new GenericEntity<List> (responses) {};
    list.getEntity().add(groupService.getGroupByName(groupName));
    return Response.status(200).entity(list).build();
  }
}
