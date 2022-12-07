package jetson.api.service;

import jetson.api.model.Product;
import jetson.api.repository.DTERepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WebService {
    private final DTERepository dteRepository;

    public Product getLatestProduct() {
        return dteRepository.findTopByOrderByIdDesc();
    }
}
