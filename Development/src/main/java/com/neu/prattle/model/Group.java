package com.neu.prattle.model;

import com.neu.prattle.exceptions.UserDoesNotExistException;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
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
  private List<User> members = new LinkedList<>();
  private Boolean isGroupPrivate;
  private String password;
  private String description;
  private int id = 0;
  private String createdOn;
  private String createdBy;

  public void setModerators(List<Moderator> moderators) {
    this.moderators = moderators;
  }

  public void setCreatedBy(String username){this.createdBy = username;}

  public void setMembers(List<User> members) {
      this.members.addAll(members);
  }

  public void setIsGroupPrivate(Boolean isPrivate) {
    this.isGroupPrivate = isPrivate;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setCreatedOn(String createdOn) {
    this.createdOn = createdOn;
  }

    public void setPassword(String password){
        this.password = password;
    }

  public List<Moderator> getModerators() {
    return moderators;
  }


  public String getCreatedBy(){ return this.createdBy;}

  public List<User> getMembers() {
    return members;
  }

  public boolean getIsGroupPrivate() {
    return isGroupPrivate;
  }


  public String getPassword(){
      return this.password;
  }


  public String getDescription() {
    return description;
  }

  public int getId() {
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

  public Group(String groupName) {
    this.name = groupName;
  }

  public Group(String groupName, String description, String createdBy, String password, Boolean isGroupPrivate){
      String strDate = setTimestamp();


      this.name = groupName;
      this.description = description;
      this.isGroupPrivate = isGroupPrivate;
      this.password = password;

      this.createdOn = strDate;
      this.createdBy = createdBy;
      this.id += id;
      this.moderators = new ArrayList<>();
      this.members = new ArrayList<>();


      this.moderators.add(new Moderator(createdBy));
  }

    /**
     * Sets the timestamped.
     * @return the time in the format yyyy-MM-dd HH:mm:ss
     */
    private String setTimestamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        return sdfDate.format(now);
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
