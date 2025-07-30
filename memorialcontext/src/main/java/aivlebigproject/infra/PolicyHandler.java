package aivlebigproject.infra;

import aivlebigproject.config.kafka.KafkaProcessor;
import aivlebigproject.domain.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    MemorialRepository memorialRepository;

    @Autowired
    VideoRepository videoRepository;

    @Autowired
    PhotoRepository photoRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    TributeRepository tributeRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='FuneralInfoRegistered'"
    )
    public void wheneverFuneralInfoRegistered_SaveMemorial(
        @Payload FuneralInfoRegistered funeralInfoRegistered
    ) {
        FuneralInfoRegistered event = funeralInfoRegistered;
        System.out.println(
            "\n\n##### listener SaveMemorial : " +
            funeralInfoRegistered +
            "\n\n"
        );

        // Sample Logic //
        Memorial.saveMemorial(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='FamilyApproved'"
    )
    public void wheneverFamilyApproved_AddFamily(
        @Payload FamilyApproved familyApproved
    ) {
        FamilyApproved event = familyApproved;
        System.out.println(
            "\n\n##### listener AddFamily : " + familyApproved + "\n\n"
        );

        // Sample Logic //
        Memorial.addFamily(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
