package edu.northeastern.minione.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * The Space model defines the structure/format in which the data has to be stored in the database "spaces".
 */
@Entity
@Table(name = "spaces")
public class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;  // space id

    @ManyToMany(mappedBy = "followedSpaces", fetch = FetchType.LAZY)
    private Set<User> followers = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    @Column(name = "space_name", nullable = false)
    private String spaceName;

    @Column(name = "space_description")
    private String spaceDescription;

    @Column(name = "created_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createdDate;

    public Space() {

    }

    public Space(int id, String spaceName, String spaceDescription) {
        this.id = id;
        this.spaceName = spaceName;
        this.spaceDescription = spaceDescription;

    }

    public int getId() {
        return id;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public User getOwner() {
        return owner;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public String getSpaceDescription() {
        return spaceDescription;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public void setSpaceDescription(String spaceDescription) {
        this.spaceDescription = spaceDescription;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Space{" +
                "id=" + id +
                ", owner=" + owner +
                ", spaceName='" + spaceName + '\'' +
                ", spaceDescription='" + spaceDescription + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}
