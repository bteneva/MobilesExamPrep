package softuni.exam.repository;


import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Device;
import softuni.exam.models.entity.enums.DeviceType;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {


    Optional<Device> findByBrandAndModel(@Length(min = 2, max = 20) String brand, @Length(min = 1, max = 20) String model);

    @Query("SELECT d FROM Device d WHERE d.deviceType = :type " +
            "AND d.price < :priceLess " +
            "AND d.storage >= :storageEqualOrMore " +
            "ORDER BY LOWER(d.brand)")
    List<Device> export(DeviceType type, double priceLess, int storageEqualOrMore);



}
