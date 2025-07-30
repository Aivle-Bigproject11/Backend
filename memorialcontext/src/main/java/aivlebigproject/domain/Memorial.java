package aivlebigproject.domain;

import aivlebigproject.MemorialcontextApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Memorial_table")
@Data
//<<< DDD / Aggregate Root
public class Memorial {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String memorialId;

    private Long customerId;

    private String imageUrl;

    private String name;

    private Integer age;

    private Date birthOfDate;

    private Date deceasedDate;

    private String gender;

    @ElementCollection
    private List<Long> familyList;

    public static MemorialRepository repository() {
        MemorialRepository memorialRepository = MemorialcontextApplication.applicationContext.getBean(
            MemorialRepository.class
        );
        return memorialRepository;
    }

    //<<< Clean Arch / Port Method
    public static void saveMemorial(
        FuneralInfoRegistered funeralInfoRegistered
    ) {
        //implement business logic here:

        /** Example 1:  new item 
        Memorial memorial = new Memorial();
        repository().save(memorial);

        */

        /** Example 2:  finding and process
        

        repository().findById(funeralInfoRegistered.get???()).ifPresent(memorial->{
            
            memorial // do something
            repository().save(memorial);


         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void addFamily(FamilyApproved familyApproved) {
        //implement business logic here:

        /** Example 1:  new item 
        Memorial memorial = new Memorial();
        repository().save(memorial);

        */

        /** Example 2:  finding and process
        

        repository().findById(familyApproved.get???()).ifPresent(memorial->{
            
            memorial // do something
            repository().save(memorial);


         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
