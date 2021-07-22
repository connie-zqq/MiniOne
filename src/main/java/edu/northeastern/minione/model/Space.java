package edu.northeastern.minione.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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

    // To load it on-demand (i.e. lazily) when you call the space's getUserId() method
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", referencedColumnName = "id")
    private User admin;

    @Column(name = "space_name", nullable = false)
    private String spaceName;

    @Column(name = "space_description")
    private String spaceDescription;

    @Column(name = "created_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createdDate;

    public Space() {

    }

    public int getId() {
        return id;
    }

    public User getAdmin() {
        return admin;
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

    public void setAdmin(User admin) {
        this.admin = admin;
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
}
