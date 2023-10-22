package Alumni.backend.module.repository;

import Alumni.backend.module.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    /*@Query("select I from Image I where I.storageImageName = :storageImageName")
    Optional<Image> findByStorageImageName(@Param("storageImageName") String storageImageName);*/

    Optional<Image> findByStorageImageName(String storageImageName);
}
