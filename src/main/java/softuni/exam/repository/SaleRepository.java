package softuni.exam.repository;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Sale;

import java.util.Optional;

import static org.hibernate.loader.Loader.SELECT;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    @Query("SELECT s FROM Sale s WHERE s.number = :number")
    Optional<Sale> findByNumber(@Length(min = 7, max = 7) String number);
}
