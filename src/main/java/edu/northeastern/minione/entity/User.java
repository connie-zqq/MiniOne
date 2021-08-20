package edu.northeastern.minione.entity;

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

/**
 * Create the User Entity and map to the database table name "users".
 */
@Entity
@Table(name = "users")
public class User {
    /**
     * The database will automatically assign an id value (increasing value).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // user id

    /**
     * The username cannot be null and is unique in the database.
     */
    @Column(name = "username", nullable = false, length = 30, unique = true)
    @NotEmpty(message = "Please provide your User Name")
    private String userName;

    @Column(name = "first_name", nullable = false)
    @NotEmpty(message = "Please provide your first name")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotEmpty(message = "Please provide your last name")
    private String lastName;

    /**
     * No duplicate email address is allowed.
     */
    @Column(name = "email", unique = true, nullable = false)
    @NotEmpty(message = "Please provide your email")
    private String email;

    @Column(name = "user_image_object_key")
    private String userImageObjectKey;

    @Column(name = "password_hash", length = 60)
    @Length(min = 6, message = "Your password must have at least 5 characters")
    @NotEmpty(message = "Please provide your password")
    private String passwordHash;

    @OneToMany(mappedBy = "follower")
    Set<Follow> follows = new HashSet<>();

    private String userImageSignedUrl;

    /**
     * Construct a default User.
     */
    public User() {
    }

    /**
     * Construct an User with the first name, last name and email.
     *
     * @param firstName The first name of this User
     * @param lastName  The last name of this User
     * @param email     The email of this User
     */
    public User(Long id, String userName, String firstName, String lastName, String email) {
        super();
        this.id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    /**
     * Construct an User with the userName, firstName, lastName and email.
     *
     * @param userName  The userName of this User
     * @param firstName The first name of this User
     * @param lastName  The last name of this User
     * @param email     The email of this User
     */
    public User(String userName, String firstName, String lastName, String email) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    /**
     * Get the id of this User.
     *
     * @return id The id of this User
     */
    public Long getId() {
        return id;
    }

    /**
     * Get the userName of this User.
     *
     * @return userName The userName of this User
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Get the firstName of this User.
     *
     * @return firstName The firstName of this User
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Get the lastName of this User.
     *
     * @return lastName The lastName of this User
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Get the email of this User.
     *
     * @return email The email of this User
     */
    public String getEmail() {
        return email;
    }

    /**
     * Get the passwordHash of this User.
     *
     * @return passwordHash The passwordHash of this User
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Set the follows of this User.
     *
     * @return follows A set contains all the follows of this User
     */
    public Set<Follow> getFollows() {
        return follows;
    }

    /**
     * Set the id of this User.
     *
     * @param id The id of this User
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Set the userName of this User.
     *
     * @param userName The userName of this User
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Set the firstName of this User.
     *
     * @param firstName The firstName of this User
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Set the lastName of this User.
     *
     * @param lastName The lastName of this User
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Set the email of this User.
     *
     * @param email The email of this User
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Set the passwordHash of this User.
     *
     * @param passwordHash The passwordHash of this User
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Set the follows of this User.
     *
     * @param follows The follows of this User
     */
    public void setFollows(Set<Follow> follows) {
        this.follows = follows;
    }

    /**
     * Get the userImageObjectKey of this User.
     *
     * @return userImageObjectKey The userImageObjectKey of this User
     */
    public String getUserImageObjectKey() {
        return userImageObjectKey;
    }

    /**
     * Set the userImageObjectKey of this User.
     *
     * @param userImageObjectKey The userImageObjectKey of this User
     */
    public void setUserImageObjectKey(String userImageObjectKey) {
        this.userImageObjectKey = userImageObjectKey;
    }

    /**
     * Get the userImageSignedUrl of this User.
     *
     * @return userImageSignedUrl The userImageSignedUrl of this User
     */
    public String getUserImageSignedUrl() {
        return userImageSignedUrl;
    }

    /**
     * Set the userImageSignedUrl of this User.
     *
     * @param userImageSignedUrl The userImageSignedUrl of this User
     */
    public void setUserImageSignedUrl(String userImageSignedUrl) {
        this.userImageSignedUrl = userImageSignedUrl;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", userName=" + userName + ", passwordHash=" + passwordHash + ", firstName=" + firstName + ", lastName=" + lastName + "]";
    }
}
