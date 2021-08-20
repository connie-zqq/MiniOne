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
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * The Space model defines the structure/format in which the data has to be stored in the database "spaces".
 */
@Entity
@Table(name = "spaces")
public class Space {

    /**
     * Automatically assign an id value (increasing value) by the database.
     */
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

    @Column(name = "baby_gender", nullable = false)
    private String babyGender;

    @Column(name = "baby_birthday", nullable = false)
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private Date babyBirthday;

    @Column(name = "baby_residence", nullable = false)
    private String babyResidence;

    @Column(name = "space_description", nullable = false)
    private String spaceDescription;

    @Column(name = "baby_image_object_key")
    private String babyImageObjectKey;

    @Column(name = "created_date_time")
    @CreationTimestamp
    private Date createdDateTime;

    // @Transient:
    // The extra fields below doesn't correspond to any columns in the "moments" table
    // because we don't want to save these value. So we use @Transient annotation to make
    // sure that these fields are ignored.
    @Transient
    private String babyAge;

    @Transient
    private String babyDevelopmentStage;

    @Transient
    private Boolean isAdmin;

    @Transient
    private String spaceUrl;

    @Transient
    private String unfollowUrl;

    @Transient
    private String babyImageSignedUrl;

    /**
     * Construct a default space.
     */
    public Space() {

    }

    /**
     * Construct a space with three parameters.
     *
     * @param id               the space id
     * @param spaceName        the space name
     * @param spaceDescription the space description
     */
    public Space(Long id, String spaceName, String spaceDescription) {
        this.id = id;
        this.spaceName = spaceName;
        this.spaceDescription = spaceDescription;

    }

    /**
     * Get the id of this Space.
     *
     * @return id The id of this Space
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the id of this Space.
     *
     * @param id The id of this Space
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the owner of this Space.
     *
     * @return owner The owner of this Space
     */
    public User getOwner() {
        return owner;
    }

    /**
     * Set the owner of this Space.
     *
     * @param owner The owner of this Space
     */
    public void setOwner(User owner) {
        this.owner = owner;
    }

    /**
     * Set the follows of this Space.
     *
     * @return follows A set contains all the follows of this Space
     */
    public Set<Follow> getFollows() {
        return follows;
    }

    /**
     * Set the follows of this Space.
     *
     * @param follows A set contains all the follows of this Space
     */
    public void setFollows(Set<Follow> follows) {
        this.follows = follows;
    }

    /**
     * Get the spaceName of this Space.
     *
     * @return spaceName The spaceName of this Space
     */
    public String getSpaceName() {
        return spaceName;
    }

    /**
     * Set the spaceName of this Space.
     *
     * @param spaceName The spaceName of this Space
     */
    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    /**
     * Get the babyName of this Space.
     *
     * @return babyName The babyName of this Space
     */
    public String getBabyName() {
        return babyName;
    }

    /**
     * Set the babyName of this Space.
     *
     * @param babyName The babyName of this Space
     */
    public void setBabyName(String babyName) {
        this.babyName = babyName;
    }

    /**
     * Get the babyGender of this Space.
     *
     * @return babyGender The babyGender of this Space
     */
    public String getBabyGender() {
        return babyGender;
    }

    /**
     * Set the babyGender of this Space.
     *
     * @param babyGender The babyGender of this Space
     */
    public void setBabyGender(String babyGender) {
        this.babyGender = babyGender;
    }

    /**
     * Get the babyBirthday of this Space.
     *
     * @return babyBirthday The babyBirthday of this Space
     */
    public Date getBabyBirthday() {
        return babyBirthday;
    }

    /**
     * Set the babyBirthday of this Space.
     *
     * @param babyBirthday The babyBirthday of this Space
     */
    public void setBabyBirthday(Date babyBirthday) {
        this.babyBirthday = babyBirthday;
    }

    /**
     * Get the babyResidence of this Space.
     *
     * @return babyResidence The babyResidence of this Space
     */
    public String getBabyResidence() {
        return babyResidence;
    }

    /**
     * Set the babyResidence of this Space.
     *
     * @param babyResidence The babyResidence of this Space
     */
    public void setBabyResidence(String babyResidence) {
        this.babyResidence = babyResidence;
    }

    /**
     * Get the spaceDescription of this Space.
     *
     * @return spaceDescription The spaceDescription of this Space
     */
    public String getSpaceDescription() {
        return spaceDescription;
    }

    /**
     * Set the spaceDescription of this Space.
     *
     * @param spaceDescription The spaceDescription of this Space
     */
    public void setSpaceDescription(String spaceDescription) {
        this.spaceDescription = spaceDescription;
    }

    /**
     * Get the createdDateTime of this Space.
     *
     * @return createdDateTime The createdDateTime of this Space
     */
    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    /**
     * Set the createdDateTime of this Space.
     *
     * @param createdDateTime The createdDateTime of this Space
     */
    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    /**
     * Get the babyAge of this Space.
     *
     * @return babyAge The babyAge of this Space
     */
    public String getBabyAge() {
        return babyAge;
    }

    /**
     * Check whether this Space is the admin.
     *
     * @return boolean RETURN true is this Space is the admin, otherwise, RETURN false
     */
    public Boolean getIsAdmin() {
        return isAdmin;
    }

    /**
     * Get the babyImageObjectKey of this Space.
     *
     * @return babyImageObjectKey The babyImageObjectKey of this Space
     */
    public String getBabyImageObjectKey() {
        return babyImageObjectKey;
    }

    /**
     * Set the babyImageObjectKey of this Space.
     *
     * @param babyImageObjectKey The babyImageObjectKey of this Space
     */
    public void setBabyImageObjectKey(String babyImageObjectKey) {
        this.babyImageObjectKey = babyImageObjectKey;
    }

    /**
     * Get the babyImageSignedUrl of this Space.
     *
     * @return babyImageSignedUrl The babyImageSignedUrl of this Space
     */
    public String getBabyImageSignedUrl() {
        return babyImageSignedUrl;
    }

    /**
     * Set the babyImageSignedUrl of this Space.
     *
     * @param babyImageSignedUrl The babyImageSignedUrl of this Space
     */
    public void setBabyImageSignedUrl(String babyImageSignedUrl) {
        this.babyImageSignedUrl = babyImageSignedUrl;
    }

    /**
     * The method is to set the space's owner as admin.
     *
     * @param user The current user
     */
    public void setIsAdmin(User user) {
        if (user == this.owner) {
            isAdmin = true;
        } else {
            isAdmin = false;
        }
    }

    /**
     * Get the spaceUrl of this Space.
     *
     * @return spaceUrl The spaceUrl of this Space
     */
    public String getSpaceUrl() {
        return spaceUrl;
    }

    /**
     * Set the spaceUrl of this Space.
     *
     * @param spaceUrl The spaceUrl of this Space
     */
    public void setSpaceUrl(String spaceUrl) {
        this.spaceUrl = spaceUrl;
    }

    /**
     * Get the unfollowUrl of this Space.
     *
     * @return unfollowUrl The unfollowUrl of this Space
     */
    public String getUnfollowUrl() {
        return unfollowUrl;
    }

    /**
     * Set the unfollowUrl of this Space.
     *
     * @param unfollowUrl The unfollowUrl of this Space
     */
    public void setUnfollowUrl(String unfollowUrl) {
        this.unfollowUrl = unfollowUrl;
    }

    /**
     * Set the babyAge of this Space.
     *
     * @param babyAge The babyAge of this Space
     */
    public void setBabyAge(String babyAge) {
        this.babyAge = babyAge;
    }

    /**
     * The method is to calculate the ago of the baby.
     *
     * @param babyBirthday The babyBirthday in the database
     * @return String The String represents the baby ago in the specific format
     */
    public String calculateBabyAge(Date babyBirthday) {

        //Converting obtained Date object to LocalDate object
        Instant instant = this.babyBirthday.toInstant();
        ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
        LocalDate givenDate = zone.toLocalDate();

        //Calculating the difference between given date to current date.
        Period period = Period.between(givenDate, LocalDate.now());
        // eg. 3D
        if (period.getYears() == 0 && period.getMonths() == 0) {
            return period.getDays() + "D";
        }
        if (period.getYears() == 0) {
            // eg. "3M"
            if (period.getDays() == 0) {
                return period.getMonths() + "M";
            }
            // eg. "2M-20D"
            return period.getMonths() + "M-" + period.getDays() + "D";
        }
        return period.getYears() + "Y-" + period.getMonths() + "M-" + period.getDays() + "D";
    }

    /**
     * Calculate the baby development stage based on the age.
     *
     * @param age The age of the baby
     * @return String The String contains the info on the baby development stage
     */
    public String getBabyDevelopmentStage(String age) {
        int year = 0;
        String babyDevStage;
        for (int i = 0; i < age.length(); i++) {
            if (age.charAt(i) == 'Y') {
                year = Integer.parseInt(age.substring(0, i));
                break;
            }
        }

        if (year == 0) {
            babyDevStage = "Infants";
        } else if (year >= 1 && year < 3) {
            babyDevStage = "Toddlers";
        } else if (year >= 3 && year < 5) {
            babyDevStage = "Preschoolers";
        } else if (year >= 5 && year < 12) {
            babyDevStage = "Middle Childhood";
        } else if (year >= 12 && year < 18) {
            babyDevStage = "Teens";
        } else if (year >= 18 && year < 22) {
            babyDevStage = "Young Adults";
        } else {
            babyDevStage = "Adults";
        }
        return babyDevStage;
    }
}
