package edu.northeastern.minione.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;

/**
 * This class is the Comment entity and maps to the database table name "comments".
 */
@Entity
@Table(name = "comments")
public class Comment {

    /**
     * Automatically assign an id value (increasing value) by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //comment id

    @OneToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "moment_id", referencedColumnName = "id")
    private Moment commentedMoment;


    @Column(name = "comment_content")
    private String commentContent;

    @Column(name = "created_date_time")
    @CreationTimestamp
    private Date createdDateTime;

    @Transient
    private String timeAgo;

    /**
     * Construct a default comment.
     */
    public Comment() {
    }

    /**
     * Get the id of this comment.
     *
     * @return Long The id of this comment
     */
    public Long getId() {
        return id;
    }

    /**
     * Get the author of this comment.
     *
     * @return User The author of this comment
     */
    public User getAuthor() {
        return author;
    }

    /**
     * Set the author of this comment.
     *
     * @param author The author of this comment
     */
    public void setAuthor(User author) {
        this.author = author;
    }

    /**
     * Get the moment that this comment is about.
     *
     * @return Moment The moment that this comment is about
     */
    public Moment getCommentedMoment() {
        return commentedMoment;
    }

    /**
     * Set the moment that this comment is about.
     *
     * @param commentedMoment The moment that this comment is about
     */
    public void setCommentedMoment(Moment commentedMoment) {
        this.commentedMoment = commentedMoment;
    }

    /**
     * Get the comment content.
     *
     * @return commentContent The commentContent of this comment
     */
    public String getCommentContent() {
        return commentContent;
    }

    /**
     * Set the comment content.
     *
     * @param commentContent The content of this comment
     */
    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    /**
     * Get the createdDateTime of this comment.
     *
     * @return createdDateTime
     */
    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    /**
     * The method is to calculate the time gap between the current time and the comment-created time.
     *
     * @return String The string represents the time gap.
     */
    public String getTimeAgo() {
        Date currentTime = new Date();
        Long timeDifference = (currentTime.getTime() - createdDateTime.getTime()) / 1000;
        System.out.println(currentTime.getTime());
        System.out.println(createdDateTime.getTime());
        System.out.println(timeDifference);
        if (timeDifference < 60) {
            timeAgo = timeDifference + "s ago";
        } else if (timeDifference < (60 * 60)) {
            timeAgo = timeDifference / 60 + "min ago";
        } else if (timeDifference < (60 * 60 * 24)) {
            timeAgo = timeDifference / (60 * 60) + "hs ago";
        } else if (timeDifference < (60 * 60 * 24 * 24)) {
            timeAgo = timeDifference / (60 * 60 * 24) + "day ago";
        } else if (timeDifference < (60 * 60 * 24 * 365)) {
            timeAgo = timeDifference / (60 * 60 * 24) + "days ago";
        } else {
            timeAgo = timeDifference / (60 * 60 * 24 * 365) + "year(s) ago";
        }
        return timeAgo;
    }
}
