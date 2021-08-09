package edu.northeastern.minione.model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

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

    @OneToMany(mappedBy = "followedSpace", fetch = FetchType.LAZY)
    Set<Follow> follows = new HashSet<>();

    @Column(name = "space_name", nullable = false)
    private String spaceName;

    @Column(name = "baby_name", nullable = false)
    private String babyName;

    @Column(name = "baby_gender")
    private String babyGender;

    @Column(name = "baby_birthday")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private Date babyBirthday;

    @Column(name = "baby_residence")
    private String babyResidence;

    @Column(name = "space_description")
    private String spaceDescription;

    @Column(name = "created_date_time")
    @CreationTimestamp
    private Date createdDateTime;

    // Todo: baby birthday
    // Todo: height, weight

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

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
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

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public String getBabyName() {
        return babyName;
    }

    public void setBabyName(String babyName) {
        this.babyName = babyName;
    }

    public String getBabyGender() {
        return babyGender;
    }

    public void setBabyGender(String babyGender) {
        this.babyGender = babyGender;
    }

    public Date getBabyBirthday() {
        return babyBirthday;
    }

    public void setBabyBirthday(Date babyBirthday) {
        this.babyBirthday = babyBirthday;
    }

    public String getBabyResidence() {
        return babyResidence;
    }

    public void setBabyResidence(String babyResidence) {
        this.babyResidence = babyResidence;
    }

    public String getSpaceDescription() {
        return spaceDescription;
    }

    public void setSpaceDescription(String spaceDescription) {
        this.spaceDescription = spaceDescription;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String claculateBabyAge(Date babyBirthday) {

        //Converting obtained Date object to LocalDate object
        Instant instant = this.babyBirthday.toInstant();
        ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
        LocalDate givenDate = zone.toLocalDate();

        //Calculating the difference between given date to current date.
        Period period = Period.between(givenDate, LocalDate.now());
        // eg. 3D
        if (period.getYears() == 0 && period.getMonths() == 0) {
            return period.getDays()+"D";
        }
        // eg. "2M20D"
        if (period.getYears() == 0) {
            return period.getMonths()+"M"+period.getDays()+"D";
        }
        return period.getYears()+"Y"+period.getMonths()+"M"+period.getDays()+"D";
    }

    @Override
    public String toString() {
        return "Space{" +
                ", spaceName='" + spaceName + '\'' +
                ", spaceDescription='" + spaceDescription +
                "'}";
    }
}
