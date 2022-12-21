package jetson.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double temp;
    private double humidity;
    private double pressure;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone="IST")
    private Timestamp timeStamp;

    public Product(double temp, double humidity, double pressure) {
        this.temp = temp;
        this.humidity = humidity;
        this.pressure = pressure;
        this.timeStamp = new Timestamp(System.currentTimeMillis());
    }
}
