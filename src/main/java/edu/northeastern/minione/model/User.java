package edu.northeastern.minione.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // user id

    @Column(name = "username", nullable = false, length = 30, unique = true)
    @NotEmpty(message = "Please provide your User Name")
    private String userName;

    @Column(name = "first_name", nullable = false)
    @NotEmpty(message = "Please provide your first name")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotEmpty(message = "Please provide your last name")
    private String lastName;

    @Column(name = "email", unique = true, nullable = false)
    @NotEmpty(message = "Please provide your email")
    private String email;

    @Column(name = "password_hash", length = 60)
    @Length(min = 6, message = "Your password must have at least 5 characters")
    @NotEmpty(message = "Please provide your password")
    private String passwordHash;

    @OneToMany(mappedBy = "follower")
    Set<Follow> follows = new HashSet<>();

    /**
     *
     */
    public User() {
    }

    /**
     * Construct an user with the first name, last name and email.
     *
     * @param firstName the first name of the user
     * @param lastName  the last name of the user
     * @param email     the email of the user
     */
    public User(Long id, String userName, String firstName, String lastName, String email) {
        super();
        this.id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User(String userName, String firstName, String lastName, String email) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Set<Follow> getFollows() {
        return follows;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setFollows(Set<Follow> follows) {
        this.follows = follows;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", userName=" + userName + ", passwordHash=" + passwordHash + ", firstName=" + firstName + ", lastName=" + lastName + "]";
    }


}
