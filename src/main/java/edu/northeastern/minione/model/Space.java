package edu.northeastern.minione.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

/**
 * The Space model defines the structure/format in which the data has to be stored in the database "spaces".
 */
@Entity
@Table(name = "spaces")
public class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // space id

    @OneToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    @OneToMany(mappedBy = "followedSpace")
    Set<Follow> follows = new HashSet<>();

    @Column(name = "space_name", nullable = false)
    private String spaceName;

    @Column(name = "space_description")
    private String spaceDescription;

    @Column(name = "created_date_time")
    @CreationTimestamp
    private Date createdDateTime;

    public Space() {

    }

    public Space(Long id, String spaceName, String spaceDescription) {
        this.id = id;
        this.spaceName = spaceName;
        this.spaceDescription = spaceDescription;

    }

    public Long getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public Set<Follow> getFollows() {
        return follows;
    }

    public void setFollows(Set<Follow> follows) {
        this.follows = follows;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public String getSpaceDescription() {
        return spaceDescription;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    @Override
    public String toString() {
        return "Space{" +
                ", spaceName='" + spaceName + '\'' +
                ", spaceDescription='" + spaceDescription +
                "'}";
    }
}
