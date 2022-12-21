package jetson.api.controller;

import jetson.api.model.Product;
import jetson.api.model.RFIDClient;
import jetson.api.service.DTEService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/dte-endpoint")
@RequiredArgsConstructor
@Log4j
public class DTEController {
    private final DTEService dteService;

    @PostMapping("send")
    public Product send(@RequestBody Product product) {
         Product savedProduct = dteService.saveProduct(
                 new Product(product.getTemp(), product.getHumidity(), product.getPressure()));
         log.info("Product saved " + savedProduct.toString());
         return savedProduct;
    }

    @GetMapping("getAllProducts")
    public List<Product> getAllProducts() {
        log.info("All products have been requested");
        return dteService.getAllProducts();
    }

    @GetMapping("dteLatestProduct")
    public Product dteLatestProduct() {
        log.info("Latest product has been requested");
        return dteService.getLatestProduct();
    }

    @GetMapping("getAllRFIDs")
    public List<RFIDClient> getAllRFIDs() {
        log.info("All RFIDs have been requested");
        return dteService.getAllRFIDs();
    }

    @PostMapping("sendRFID")
    public RFIDClient saveRFID(@RequestBody RFIDClient rfidClient) {
        RFIDClient savedRfid = dteService.saveRFIDClient(new RFIDClient(rfidClient.getRfid()));
        log.info("RFID saved" + savedRfid.toString());
        return savedRfid;
    }

    @PostMapping("checkRFID")
    public boolean checkRFID(@RequestBody RFIDClient rfidClient) {
        RFIDClient givenRFIDClient = new RFIDClient(rfidClient.getRfid());
        log.info("RFID: " + givenRFIDClient.getId() + " is being checked");
        return dteService.checkIfRFIDExists(givenRFIDClient);
    }
}
