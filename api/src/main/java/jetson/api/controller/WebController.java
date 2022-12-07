package jetson.api.controller;

import jetson.api.model.Product;
import jetson.api.service.WebService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/web")
@AllArgsConstructor
public class WebController {
  private final WebService webService;

    @GetMapping("latestProduct")
    public Product latestProduct() {
        return webService.getLatestProduct();
    }
}
