package edu.northeastern.minione.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "moments")
public class Moment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // moment id

    @OneToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id", referencedColumnName = "id")
    private Space space;

    @Column(name = "moment_title", nullable = false)
    private String momentTitle;

    @Column(name = "moment_content", columnDefinition = "TEXT", nullable = false)
    private String momentContent;

    // Todo: add column - name="photo"
    // Todo: add column - name="location"

    @Column(name = "created_date_time")
    @CreationTimestamp
    private Date createdDateTime;

    public Moment() {
    }

    public Long getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public Space getSpace() {
        return space;
    }

    public String getMomentTitle() {
        return momentTitle;
    }

    public String getMomentContent() {
        return momentContent;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    public void setMomentTitle(String momentTitle) {
        this.momentTitle = momentTitle;
    }

    public void setMomentContent(String momentContent) {
        this.momentContent = momentContent;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
}
