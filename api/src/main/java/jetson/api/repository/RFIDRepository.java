package jetson.api.repository;

import jetson.api.model.RFIDClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RFIDRepository extends JpaRepository<RFIDClient, Long> {

}
