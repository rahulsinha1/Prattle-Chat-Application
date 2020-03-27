package com.neu.prattle.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/***
 * A User object represents a basic account information for a user.
 *
 * @author CS5500 Fall 2019 Teaching staff
 * @version dated 2019-10-06
 */

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

public class User {
  
  public String getUsername() {
    return username;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getTimezone() {
    return timezone;
  }

  public String getPassword() {
    return password;
  }

  public List<Group> getGroupParticipant() {
    return groupParticipant;
  }

  public List<User> getFollowers() {
    return followers;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setTimezone(String timezone) {
    this.timezone = timezone;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setGroupParticipant(List<Group> groupParticipant) {
    this.groupParticipant.addAll(groupParticipant);
  }

  public void setFollowers(List<User> followers) {
    this.followers.addAll(followers);
  }


  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  @Id
  @Column(name = "user_id", unique = true)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int userId;

  @Column(name = "username")
  private String username;
  @Column(name = "first_name", unique = false)
  private String firstName;
  @Column(name = "last_name", unique = false)
  private String lastName;
  @Column(name = "user_password", unique = false)
  private String password;
  @Column(name = "timezone", unique = false)
  private String timezone;

  @ManyToMany(cascade = {CascadeType.ALL})
  @JoinTable(name = "group_users",
          joinColumns = @JoinColumn(name = "userId"),
          inverseJoinColumns = @JoinColumn(name = "group_id"))
  private List<Group> groupParticipant;

  @ManyToMany
  private List<User> followers;

  public User() {
    groupParticipant = new ArrayList();
    followers = new ArrayList<>();
    this.timezone = "default";
    this.firstName = "first_name";
    this.lastName = "last_name";
    this.password = getEncryptedPassword();
  }

  public User(String firstName, String lastName, String username, String password, String timezone) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.password = password;
    this.timezone = timezone;
    groupParticipant = new ArrayList();
    followers = new ArrayList<>();
  }

  public User(String username) {
    this.username = username;
    this.timezone = "default";
    this.firstName = "first_name";
    this.lastName = "last_name";
    this.password = getEncryptedPassword();
    groupParticipant = new ArrayList();
    followers = new ArrayList<>();
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
    return Objects.hash(username);
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
    if (!(obj instanceof User))
      return false;

    User user = (User) obj;
    return user.username.equals(this.username);
  }

  private String getEncryptedPassword() {
    StringBuilder s = new StringBuilder("pass");
    return s.reverse().toString();
  }

}
