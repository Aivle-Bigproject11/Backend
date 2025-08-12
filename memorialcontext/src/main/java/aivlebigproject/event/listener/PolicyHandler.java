package aivlebigproject.event.listener;

import aivlebigproject.config.kafka.KafkaProcessor;

import aivlebigproject.service.CommentService;
import aivlebigproject.service.MemorialService;
import aivlebigproject.service.PhotoService;
import aivlebigproject.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

//<<< Clean Arch / Inbound Adaptor
@Service
@RequiredArgsConstructor
public class PolicyHandler {

    private final MemorialService memorialService;

    private final VideoService videoService;

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

        memorialService.createMemorial(event);
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
//        Memorial.addFamily(event);
    }

    @StreamListener(
            value = KafkaProcessor.INPUT,
            condition = "headers['type']=='VideoCreated'"
    )
    public void wheneverVideoCreated_SaveVideo(
            @Payload VideoCreated videoCreated
    ) {
        VideoCreated event = videoCreated;
        System.out.println(
                "\n\n##### listener SaveMemorial : " + videoCreated + "\n\n"
        );

        // Sample Logic //
        videoService.saveVideo(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
