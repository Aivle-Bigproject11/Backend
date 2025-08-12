package aivlebigproject.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

//<<< EDA / CQRS
@Entity
@Table(name = "CustomerReadModel_table")
@Data
public class CustomerReadModel {

    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private Long customerId;

    private String customerName;
    private String customerRrn;
    private Integer customerAge;
    private Date customerBirthOfDate;
    private String customerGender;
    private String customerAddress;
    private String customerPhone;
    private String customerJob;
    private Boolean customerHasChildren;
    private Boolean customerIsMarried;
}