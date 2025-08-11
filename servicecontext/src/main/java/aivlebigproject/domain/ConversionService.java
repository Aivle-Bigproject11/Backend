package aivlebigproject.domain;

import aivlebigproject.ServicecontextApplication;

import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ConversionService_table")
@Data
//<<< DDD / Aggregate Root
public class ConversionService {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long serviceId;

    private String serviceName;

    private String imageUrl;

    private String detailedUrl;

    public static ConversionServiceRepository repository() {
        ConversionServiceRepository conversionServiceRepository = ServicecontextApplication.applicationContext.getBean(
                ConversionServiceRepository.class
        );
        return conversionServiceRepository;
    }
}
//>>> DDD / Aggregate Root
