package jetson.api.controller;

import jetson.api.model.Product;
import jetson.api.model.RFIDClient;
import jetson.api.service.DTEService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/dte-endpoint")
@RequiredArgsConstructor
public class DTEController {
    private final DTEService dteService;

    @PostMapping("send")
    public Product send(@RequestBody Product product) {
        return dteService.saveProduct(new Product(product.getTemp(), product.getHumidity(), product.getPressure()));
    }

    @GetMapping("getAllProducts")
    public List<Product> getAllProducts() {
        return dteService.getAllProducts();
    }

    @GetMapping("dteLatestProduct")
    public Product dteLatestProduct() {
        return dteService.getLatestProduct();
    }

    @GetMapping("getAllRFIDs")
    public List<RFIDClient> getAllRFIDs() {
        return dteService.getAllRFIDs();
    }

    @PostMapping("sendRFID")
    public RFIDClient saveRFID(@RequestBody RFIDClient rfidClient) {
        return dteService.saveRFIDClient(new RFIDClient(rfidClient.getRfid()));
    }

    @PostMapping("checkRFID")
    public boolean checkRFID(@RequestBody RFIDClient rfidClient) {
        RFIDClient givenRFIDClient = new RFIDClient(rfidClient.getRfid());
        System.out.println(givenRFIDClient.getId());
        return dteService.checkIfRFIDExists(givenRFIDClient);
    }
}
