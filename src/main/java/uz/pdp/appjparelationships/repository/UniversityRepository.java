package uz.pdp.appjparelationships.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.appjparelationships.entity.University;

@Repository
public interface UniversityRepository extends JpaRepository<University, Integer> {
    Page<University> findAllByAddress_District(String address_district, Pageable pageable);
}
