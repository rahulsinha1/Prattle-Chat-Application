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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


/***
 * A Group object represents a basic group information.
 *
 */

@Entity
@Table(name = "groups")

public class Group {

  @Id
  @Column(name = "group_id", unique = true)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  @Column(name = "group_name", unique = true)
  private String name;
  @ManyToMany(cascade = {CascadeType.MERGE})
  @JoinTable( name = "group_mods",
          joinColumns = @JoinColumn(name = "group_id"),
          inverseJoinColumns = @JoinColumn(name = "moderator_id"))
  private List<User> moderators;
  @ManyToMany(cascade = {CascadeType.MERGE})
  @JoinTable(name = "group_users",
          joinColumns = @JoinColumn(name = "group_id"),
          inverseJoinColumns = @JoinColumn(name = "user_id"))


  private List<User> members = new LinkedList<>();
  @Column(name = "is_private", unique = false)
  private Boolean isGroupPrivate;
  @Column(name = "group_password", unique = false)
  private String password;
  @Column(name = "group_description", unique = false)
  private String description;
  @Column(name = "created_on", unique = false)
  private String createdOn;
  @Column(name = "created_by", unique = false)
  private String createdBy;

  public void setModerators(List<User> moderators) {
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

  public List<User> getModerators() {
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