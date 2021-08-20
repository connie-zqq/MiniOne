package edu.northeastern.minione.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

/**
 * This class is the Invitation Entity and maps to the database table name "invitation".
 */
@Entity
@Table(name = "invitations")
public class Invitation {

    /**
     * Automatically assign an id value (increasing value) by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // invitation id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id", referencedColumnName = "id")
    private Space space;

    /**
     * The email cannot be null and is unique in the database.
     */
    @Column(name = "email", unique = true, nullable = false)
    @NotEmpty(message = "Please provide the invitee's email")
    private String email;

    /**
     * Construct a default invitation.
     */
    public Invitation() {
    }

    /**
     * Construct an invitation with email.
     *
     * @param space the Space object
     * @param email the email of the invitee
     */
    public Invitation(Space space, String email) {
        this.space = space;
        this.email = email;
    }

	/**
	 * Get the id of this invitation.
	 *
	 * @return id the id of this invitation
	 */
    public Long getId() {
        return id;
    }

	/**
	 * Set the id of this invitation.
	 *
	 * @param id the id of this invitation
	 */
    public void setId(Long id) {
        this.id = id;
    }

	/**
	 * Get the space of this invitation.
	 *
	 * @return space the space of this invitation
	 */
    public Space getSpace() {
        return space;
    }

	/**
	 * Set the space of this invitation.
	 *
	 * @param space the space of this invitation
	 */
    public void setSpace(Space space) {
        this.space = space;
    }

	/**
	 * Get the email of this invitation.
	 *
	 * @return email The email of this invitation
	 */
    public String getEmail() {
        return email;
    }

	/**
	 * Set the email of this invitation
	 *
	 * @param email The email of this invitation
	 */
    public void setEmail(String email) {
        this.email = email;
    }
}
