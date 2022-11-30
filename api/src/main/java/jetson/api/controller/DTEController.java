package jetson.api.controller;

import jetson.api.model.Product;
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
    public Product method(@RequestBody Product product) {
        return dteService.saveProduct(new Product(product.getTemp(), product.getHumidity(), product.getPressure()));
    }

    @GetMapping("getAll")
    public List<Product> method() {
        return dteService.getAll();
    }
}
