package aivlebigproject.domain;

import lombok.Data;

@Data
public class CustomerDeleted {
    private Long customerId;
    public boolean validate() { return customerId != null; }
}
