package hh5.twogaether.domain.match.repository;

import hh5.twogaether.domain.match.entity.Pass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PassRepository extends JpaRepository<Pass, Long> {

    @Query("select p from Pass p where p.createdBy =:createdBy and p.dogId =:dogId")
    Pass findByCreatedByAndDogId(@Param("createdBy") Long createdBy, @Param("dogId") Long dogId);

    @Modifying(clearAutomatically = true)
    @Query("delete from Pass p where p.isLoved = false")
    void deleteAllisNotLoved();
}
