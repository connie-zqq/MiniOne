package edu.northeastern.minione.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * This class is the AmazonImage Entity and maps to the database table name "amazonimages".
 */
@Entity
@Table(name = "amazonimages")
public class AmazonImage {

    /**
     * Automatically assign an id value (increasing value) by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Image id

    @Column(name = "object_key")
    private String objectKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moment_id", referencedColumnName = "id")
    private Moment moment;

    /**
     * Define the signed url of the amazonImage
     */
    @Transient
    private String signedUrl;

    /**
     * Get the signedUrl of this amazonImage.
     *
     * @return signedUrl
     */
    public String getSignedUrl() {
        return signedUrl;
    }

    /**
     * Get the signedUrl of this amazonImage.
     *
     * @param signedUrl The signedUrl of this amazonImage
     */
    public void setSignedUrl(String signedUrl) {
        this.signedUrl = signedUrl;
    }

    /**
     * Get the id of this amazonImage
     *
     * @return id The id of this amazonImage
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the id of this amazonImage
     *
     * @param id The id of this amazonImage
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the objectKey of this amazonImage
     *
     * @return objectKey The objectKey of this amazonImage
     */
    public String getObjectKey() {
        return objectKey;
    }

    /**
     * Set the objectKey of this amazonImage
     *
     * @param objectKey The objectKey of this amazonImage
     */
    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    /**
     * Get the moment of this amazonImage
     *
     * @return moment The moment that this amazonImage is belongs to
     */
    public Moment getMoment() {
        return moment;
    }

    /**
     * Set the moment of this amazonImage
     *
     * @param moment The moment that this amazonImage is belongs to
     */
    public void setMoment(Moment moment) {
        this.moment = moment;
    }
}
