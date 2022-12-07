package jetson.api.service;

import jetson.api.model.Product;
import jetson.api.model.RFIDClient;
import jetson.api.repository.DTERepository;
import jetson.api.repository.RFIDRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DTEService {
    private final DTERepository dteRepository;
    private final RFIDRepository rfidRepository;

    public Product saveProduct(Product product) {
        dteRepository.save(product);
        return product;
    }

    public List<Product> getAllProducts() {
        return dteRepository.findAll();
    }

    public Product getLatestProduct() {
        return dteRepository.findTopByOrderByIdDesc();
    }

    public List<RFIDClient> getAllRFIDs() {
        return rfidRepository.findAll();
    }
    public RFIDClient saveRFIDClient(RFIDClient rfidClient) {
        return rfidRepository.save(rfidClient);
    }

    public boolean checkIfRFIDExists(RFIDClient rfidClient) {
        boolean exists = rfidRepository.exists(Example.of(rfidClient));
        System.out.println(exists);
        return exists;
    }
}
