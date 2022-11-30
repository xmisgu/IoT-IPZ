package jetson.api.service;

import jetson.api.model.Product;
import jetson.api.repository.DTERepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DTEService {
    private final DTERepository dteRepository;

    public Product saveProduct(Product product) {
        dteRepository.save(product);
        System.out.println(product);
        return product;
    }

    public List<Product> getAll() {
        return dteRepository.findAll();
    }
}
