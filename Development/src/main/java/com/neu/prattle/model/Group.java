package com.neu.prattle.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


/***
 * A Group object represents a basic group information.
 *
 */
public class Group {

  private String name;
  private List<Moderator> moderators;
  private List<User> users = new LinkedList<>();
  private boolean isPrivate;
  private String description;
  private String id;
  private String createdOn;

  public void setModerators(List<Moderator> moderators) {
    this.moderators = moderators;
  }

  public void setUsers(List<User> users) {
    for(User user : users)
      this.users.add(user);
  }

  public void setPrivate(boolean aPrivate) {
    isPrivate = aPrivate;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setCreatedOn(String createdOn) {
    this.createdOn = createdOn;
  }

  public List<Moderator> getModerators() {
    return moderators;
  }

  public List<User> getUsers() {
    return users;
  }

  public boolean isPrivate() {
    return isPrivate;
  }

  public String getDescription() {
    return description;
  }

  public String getId() {
    return id;
  }

  public String getCreatedOn() {
    return createdOn;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public Group() {

  }

  public Group(String name) {
    this.name = name;
  }

  public Group(String name,List<User> users,List<Moderator> moderators){
    this.name = name;
    this.users = users;
    this.moderators = moderators;
  }

  /***
   * Returns the hashCode of this object.
   *
   * As name can be treated as a sort of identifier for
   * this instance, we can use the hashCode of "name"
   * for the complete object.
   *
   *
   * @return hashCode of "this"
   */
  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  /***
   * Makes comparison between two user accounts.
   *
   * Two user objects are equal if their name are equal ( names are case-sensitive )
   *
   * @param obj Object to compare
   * @return a predicate value for the comparison.
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Group))
      return false;

    Group group = (Group) obj;
    return group.name.equals(this.name);
  }
}
