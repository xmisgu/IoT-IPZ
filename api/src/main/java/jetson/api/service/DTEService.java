package jetson.api.service;

import jetson.api.model.Product;
import jetson.api.model.RFIDClient;
import jetson.api.repository.DTERepository;
import jetson.api.repository.RFIDRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j
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
        Product latestProduct = dteRepository.findTopByOrderByIdDesc();
        log.info("Latest product is " + latestProduct.toString());
        return  latestProduct;
    }

    public List<RFIDClient> getAllRFIDs() {
        return rfidRepository.findAll();
    }
    public RFIDClient saveRFIDClient(RFIDClient rfidClient) {
        log.info("RFIDClient which is being saved: " + rfidClient.toString());
        return rfidRepository.save(rfidClient);
    }

    public boolean checkIfRFIDExists(RFIDClient rfidClient) {
        Optional<RFIDClient> rfidClientFromDB = rfidRepository.findById(rfidClient.getId());

        if (rfidClientFromDB.isPresent()) {
            log.info("RFID exists in database");
            RFIDClient updatedRFID = rfidClientFromDB.get();
            log.info("Inside?: " + updatedRFID.isInBuilding());
            updatedRFID.setInBuilding(!updatedRFID.isInBuilding());
            rfidRepository.save(updatedRFID);
        }

        return rfidClientFromDB.isPresent();
    }
}
