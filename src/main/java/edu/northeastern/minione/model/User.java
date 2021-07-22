package edu.northeastern.minione.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;  // user id

	@Column(name = "username", nullable = false, length = 30, unique = true)
	@NotEmpty(message = "Please provide your User Name")
	private String userName;

	@Column(name = "first_name", nullable = false)
	@NotEmpty(message = "Please provide your first name")
	private String firstName;

	@Column(name = "last_name", nullable = false)
	@NotEmpty(message = "Please provide your last name")
	private String lastName;

	@Column(name = "email", nullable = false, unique = true)
	@NotEmpty(message = "Please provide your email")
	private String email;

	@Column(name = "password_hash", length = 60)
	@Length(min = 6, message = "Your password must have at least 5 characters")
	@NotEmpty(message = "Please provide your password")
	private String passwordHash;

	/**
	 *
	 */
	public User() {
	}

	/**
	 * Construct an user with the first name, last name and email.
	 *
	 * @param firstName the first name of the user
	 * @param lastName the last name of the user
	 * @param email the email of the user
	 */
	public User(String userName, String firstName, String lastName, String email) {
		super();
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the passwordHash
	 */
	public String getPasswordHash() {
		return passwordHash;
	}

	/**
	 * @param passwordHash the passwordHash to set
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", passwordHash=" + passwordHash + ", firstName=" + firstName + ", lastName=" + lastName + "]";
	}


}
