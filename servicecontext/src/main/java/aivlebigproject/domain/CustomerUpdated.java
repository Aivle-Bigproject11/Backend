package aivlebigproject.domain;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class CustomerUpdated {
    private Long customerId;
    private String name;
    private Integer age;
    private String phone;
    private String job;
    private String address;
    private String gender;

    private Date birthDate;

    private Boolean hasChildren;
    private Boolean isMarried;
    private List<String> diseaseList;

    public boolean validate() { return customerId != null; }
}
