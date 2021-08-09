package edu.northeastern.minione.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Many-to-many with a new Entity
 */
@Entity
@Table(name = "follows")
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // follow id

    @ManyToOne
    @JoinColumn(name = "follower_id", referencedColumnName = "id")
    private User follower;

    @ManyToOne
    @JoinColumn(name = "followed_space_id", referencedColumnName = "id")
    private Space followedSpace;

  // Todo: user role: Admin (mom, dad); Edit (grandma, grandpa)； nickname in th space
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private FamilyRole role;

    @Column(name = "nickname")
    private String nickName;

    public Follow() {

    }

    public Follow(User follower, Space followedSpace) {
        this.follower = follower;
        this.followedSpace = followedSpace;
    }

    public Long getId() {
        return id;
    }

    public User getFollower() {
        return follower;
    }

    public Space getFollowedSpace() {
        return followedSpace;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public void setFollowedSpace(Space followedSpace) {
        this.followedSpace = followedSpace;
    }

    public FamilyRole getRole() {
        return role;
    }

    public void setRole(FamilyRole role) {
        this.role = role;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
