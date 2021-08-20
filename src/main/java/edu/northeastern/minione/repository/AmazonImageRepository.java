package edu.northeastern.minione.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.northeastern.minione.entity.AmazonImage;

/**
 * Create the interface AmazonImageRepository.
 */
@Repository
public interface AmazonImageRepository extends JpaRepository<AmazonImage, Long> {

}
