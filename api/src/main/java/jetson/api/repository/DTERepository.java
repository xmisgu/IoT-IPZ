package jetson.api.repository;

import jetson.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DTERepository extends JpaRepository<Product, Long> {
}
