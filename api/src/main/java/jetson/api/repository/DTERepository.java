package jetson.api.repository;

import jetson.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DTERepository extends JpaRepository<Product, Long> {

    Product findTopByOrderByIdDesc();
}
