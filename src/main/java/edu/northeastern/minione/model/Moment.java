package edu.northeastern.minione.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CreationTimestamp;

/**
 * This is the moment class, which is a JPA entity that maps to a "moments" table in the database.
 */
@Entity
@Table(name = "moments")
public class Moment {

    /**
     * Automatically assign an id value (increasing value) by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // moment id

    @OneToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "space_id", referencedColumnName = "id")
    private Space space;

    @OneToMany(mappedBy = "moment", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<AmazonImage> photos;

    @Column(name = "moment_title")
    private String momentTitle;

    @Column(name = "moment_content", columnDefinition = "TEXT", nullable = false)
    private String momentContent;

    @Column(name = "created_date_time")
    @CreationTimestamp
    private Date createdDateTime;

    @OneToMany(mappedBy = "likedMoment", orphanRemoval = true)
    private List<Like> likes;

    @OneToMany(mappedBy = "commentedMoment", orphanRemoval = true)
    private List<Comment> comments;

    @Transient
    private String momentFansString;

    /**
     * Construct a default Moment.
     */
    public Moment() {
    }

    /**
     * Get the id of this Moment.
     *
     * @return id The id of this Moment
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the id of this Moment.
     *
     * @param id The id of this Moment
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the author of this Moment.
     *
     * @return author The author of this Moment
     */
    public User getAuthor() {
        return author;
    }

    /**
     * Set the author of this Moment.
     *
     * @param author The author of this Moment
     */
    public void setAuthor(User author) {
        this.author = author;
    }

    /**
     * Get the AmazonImage of this Moment.
     *
     * @return List A list contains all the AmazonImage of this Moment
     */
    public List<AmazonImage> getPhotos() {
        return photos;
    }

    /**
     * Set the AmazonImage of this Moment.
     *
     * @param photos A list contains all the AmazonImage of this Moment
     */
    public void setPhotos(List<AmazonImage> photos) {
        this.photos = photos;
    }

    /**
     * Get the space of this Moment.
     *
     * @return Space The space that this Moment belongs to.
     */
    public Space getSpace() {
        return space;
    }

    /**
     * Set the space of this Moment.
     *
     * @param space The space that this Moment belongs to.
     */
    public void setSpace(Space space) {
        this.space = space;
    }

    /**
     * Get the momentTitle of this Moment.
     *
     * @return momentTitle The momentTitle of this Moment
     */
    public String getMomentTitle() {
        return momentTitle;
    }

    /**
     * Set the momentTitle of this Moment.
     *
     * @param momentTitle The momentTitle of this Moment.
     */
    public void setMomentTitle(String momentTitle) {
        this.momentTitle = momentTitle;
    }

    /**
     * Get the momentContent of this Moment.
     *
     * @return momentContent The momentContent of this Moment
     */
    public String getMomentContent() {
        return momentContent;
    }

    /**
     * Set the momentContent of this Moment.
     *
     * @param momentContent The momentContent of this Moment
     */
    public void setMomentContent(String momentContent) {
        this.momentContent = momentContent;
    }

    /**
     * Get the createdDateTime of this Moment.
     *
     * @return createdDateTime The createdDateTime of this Moment
     */
    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    /**
     * Set the createdDateTime of this Moment.
     *
     * @param createdDateTime The createdDateTime of this Moment
     */
    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    /**
     * Get all the Like of this Moment.
     *
     * @return likes A list contains all the Like of this Moment
     */
    public List<Like> getLikes() {
        return likes;
    }

    /**
     * Set all the Like of this Moment.
     *
     * @param likes A list contains all the Like of this Moment
     */
    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    /**
     * Check whether there is any Like of this Moment.
     *
     * @return boolean RETURN true if Moment has at least one Like, otherwise, RETURN false
     */
    public boolean isLiked() {
        return this.getLikes().size() > 0;
    }

    /**
     * Get all the comment of this Moment.
     *
     * @return comments A list contains all the comment of this Moment
     */
    public List<Comment> getComments() {
        return comments;
    }

    /**
     * Set the comment of this Moment.
     *
     * @param comments A list contains all the comment of this Moment
     */
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    /**
     * Get the momentFanString of this Moment.
     *
     * @return momentFansString The momentFansString of this Moment
     */
    public String getMomentFansString() {
        return momentFansString;
    }

    /**
     * Set the momentFansString of this Moment.
     *
     * @param momentFansString The momentFansString of this Moment
     */
    public void setMomentFansString(String momentFansString) {
        this.momentFansString = momentFansString;
    }

    /**
     * Generate a String that contains all the likes of this Moment.
     *
     * @param likes A list of all the Like of this moment
     * @return String
     */
    public String generateMomentFansString(List<Like> likes) {
        if (likes.isEmpty()) {
            return null;
        } else {
            List<String> fanFirstNames = new ArrayList<>();
            for (Like like : likes) {
                fanFirstNames.add(like.getFan().getFirstName());
            }
            return StringUtils.join(fanFirstNames, ", ");
        }
    }
}
