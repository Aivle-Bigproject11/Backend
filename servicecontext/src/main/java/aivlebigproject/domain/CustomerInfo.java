package aivlebigproject.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Date;
import lombok.Data;
import java.time.LocalDate;


//<<< EDA / CQRS
@Entity
@Table(name="CustomerInfo_table")
@Data
public class CustomerInfo {

        @Id
        //@GeneratedValue(strategy=GenerationType.AUTO)
        private Long id;
        private Integer age;
        private List&lt;String&gt; disease;
        private Boolean hasChildern;
        private String gender;
        private Boolean isMarriage;


}
