package jetson.api.service;

import jetson.api.model.Product;
import jetson.api.model.RFIDClient;
import jetson.api.repository.DTERepository;
import jetson.api.repository.RFIDRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DTEService {
    private final DTERepository dteRepository;
    private final RFIDRepository rfidRepository;

    public Product saveProduct(Product product) {
       // if (checkIfRFIDExists())
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
        Optional<RFIDClient> rfidClientFromDB = rfidRepository.findById(rfidClient.getId());

        if (rfidClientFromDB.isPresent()) {
            RFIDClient updatedRFID = rfidClientFromDB.get();
            System.out.println(updatedRFID.isInBuilding());
            updatedRFID.setInBuilding(!updatedRFID.isInBuilding());
            rfidRepository.save(updatedRFID);
        }

        System.out.println(rfidRepository.findById(rfidClient.getId()).get().isInBuilding());

        return rfidClientFromDB.isPresent();
    }
}
