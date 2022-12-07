package jetson.api.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "rfid_client")
public class RFIDClient {

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private int rfid;

    public RFIDClient(int rfid) {
        this.rfid = rfid;
    }
}
