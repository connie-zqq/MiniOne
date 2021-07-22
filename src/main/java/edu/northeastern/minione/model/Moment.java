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

@Entity
@Table(name = "moments")
public class Moment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;  // moment id

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id", referencedColumnName = "id")
    private Space space;

    @Column(name = "moment_title", nullable = false)
    private String momentTitle;

    @Column(name = "mement_content", columnDefinition = "TEXT", nullable = false)
    private String momentContent;

    @Column(name = "created_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createdDate;

    public Moment() {
    }

    public int getId() {
        return id;
    }

    public User getCreator() {
        return creator;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreator(User creator) {
        this.creator = creator;
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

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
