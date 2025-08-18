package aivlebigproject.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "CustomerInfo_table")
@Data
public class CustomerInfo {

        @Id
        private Long id;  // 고객 식별자  customerId

        private String name;
        private Integer age;
        private String phone;
        private String job;
        private String address;
        private String gender;
        private Boolean hasChildren;
        private Boolean isMarried;
        private Date birthDate;

        @Convert(converter = StringListConverter.class)
        private List<String> disease; // 질병 리스트 (저장용)
}
