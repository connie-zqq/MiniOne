package edu.northeastern.minione.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * This class is the Like entity and maps to the database table name "likes".
 */
@Entity
@Table(name = "likes")
public class Like {

    /**
     * Automatically assign an id value (increasing value) by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // like id

    @OneToOne
    @JoinColumn(name = "fan_id", referencedColumnName = "id")
    private User fan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moment_id", referencedColumnName = "id")
    private Moment likedMoment;

    /**
     * Construct a default Like.
     */
    public Like() {
    }

    /**
     * Construct a Like object with input fan and likedMoment parameters.
     *
     * @param fan         The fan of this Like
     * @param likedMoment The likedMoment of this Like
     */
    public Like(User fan, Moment likedMoment) {
        this.fan = fan;
        this.likedMoment = likedMoment;
    }

    /**
     * Get the id of this Fan.
     *
     * @return id The id of this Fan
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the id of this Fan.
     *
     * @param id The id of this Fan
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get fan of this Fan.
     *
     * @return fan The fan of this Fan
     */
    public User getFan() {
        return fan;
    }

    /**
     * Set fan of this Fan.
     *
     * @param fan The fan of this Fan
     */
    public void setFan(User fan) {
        this.fan = fan;
    }

    /**
     * Get the likedMoment of this Fan.
     *
     * @return likedMoment The likedMoment of this Fan
     */
    public Moment getLikedMoment() {
        return likedMoment;
    }

    /**
     * Set the liked moment of this Fan.
     *
     * @param likedMoment The moment of this Fan
     */
    public void setlikedMoment(Moment likedMoment) {
        this.likedMoment = likedMoment;
    }
}








