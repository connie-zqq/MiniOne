package edu.northeastern.minione.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * This class is the Follow Entity and maps to the database table name "follows".
 */
@Entity
@Table(name = "follows")
public class Follow {

    /**
     * Automatically assign an id value (increasing value) by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // follow id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", referencedColumnName = "id")
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followed_space_id", referencedColumnName = "id")
    private Space followedSpace;

    /**
     * Construct a default Follow.
     */
    public Follow() {
    }

    /**
     * Construct a follow with input follower and followedSpace.
     *
     * @param follower      The user that wants to follow the space
     * @param followedSpace The space that the follower wants to follow
     */
    public Follow(User follower, Space followedSpace) {
        this.follower = follower;
        this.followedSpace = followedSpace;
    }

    /**
     * Get the id of this Follow.
     *
     * @return id The id of this Follow
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the id of this Follow.
     *
     * @param id The id of this Follow
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the follower of this Follow.
     *
     * @return follower The follower of this Follow
     */
    public User getFollower() {
        return follower;
    }

    /**
     * Set the follow of this Follow.
     *
     * @param follower The follower of this Follow
     */
    public void setFollower(User follower) {
        this.follower = follower;
    }

    /**
     * Get the followedSpace of this Follow.
     *
     * @return followedSpace The followedSpace of this Follow
     */
    public Space getFollowedSpace() {
        return followedSpace;
    }

    /**
     * Set the followSpace of this Follow.
     *
     * @param followedSpace The followedSpace of this Follow
     */
    public void setFollowedSpace(Space followedSpace) {
        this.followedSpace = followedSpace;
    }

    /**
     * Get the info of this Follow in string.
     */
    @Override
    public String toString() {
        return "Follow{" +
                "id=" + id +
                ", follower=" + follower +
                ", followedSpace=" + followedSpace +
                '}';
    }
}
