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
    FuneralInfoRepository funeralInfoRepository;

    @Autowired
    ObituaryRepository obituaryRepository;

    @Autowired
    DeathReportRepository deathReportRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='ObituaryCreationRequested'"
    )
    public void wheneverObituaryCreationRequested_CreateObituaryData(
        @Payload ObituaryCreationRequested obituaryCreationRequested
    ) {
        ObituaryCreationRequested event = obituaryCreationRequested;
        System.out.println(
            "\n\n##### listener CreateObituaryData : " +
            obituaryCreationRequested +
            "\n\n"
        );

        // Sample Logic //
        Obituary.createObituaryData(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='AllDocumentsCreationRequested'"
    )
    public void wheneverAllDocumentsCreationRequested_CreateObituaryData(
        @Payload AllDocumentsCreationRequested allDocumentsCreationRequested
    ) {
        AllDocumentsCreationRequested event = allDocumentsCreationRequested;
        System.out.println(
            "\n\n##### listener CreateObituaryData : " +
            allDocumentsCreationRequested +
            "\n\n"
        );

        // Sample Logic //
        Obituary.createObituaryData(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='DeathReportCreationRequested'"
    )
    public void wheneverDeathReportCreationRequested_CreateDeathReportData(
        @Payload DeathReportCreationRequested deathReportCreationRequested
    ) {
        DeathReportCreationRequested event = deathReportCreationRequested;
        System.out.println(
            "\n\n##### listener CreateDeathReportData : " +
            deathReportCreationRequested +
            "\n\n"
        );

        // Sample Logic //
        DeathReport.createDeathReportData(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='AllDocumentsCreationRequested'"
    )
    public void wheneverAllDocumentsCreationRequested_CreateDeathReportData(
        @Payload AllDocumentsCreationRequested allDocumentsCreationRequested
    ) {
        AllDocumentsCreationRequested event = allDocumentsCreationRequested;
        System.out.println(
            "\n\n##### listener CreateDeathReportData : " +
            allDocumentsCreationRequested +
            "\n\n"
        );

        // Sample Logic //
        DeathReport.createDeathReportData(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='ScheduleCreationRequested'"
    )
    public void wheneverScheduleCreationRequested_CreateScheduleData(
        @Payload ScheduleCreationRequested scheduleCreationRequested
    ) {
        ScheduleCreationRequested event = scheduleCreationRequested;
        System.out.println(
            "\n\n##### listener CreateScheduleData : " +
            scheduleCreationRequested +
            "\n\n"
        );

        // Sample Logic //
        Schedule.createScheduleData(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='AllDocumentsCreationRequested'"
    )
    public void wheneverAllDocumentsCreationRequested_CreateScheduleData(
        @Payload AllDocumentsCreationRequested allDocumentsCreationRequested
    ) {
        AllDocumentsCreationRequested event = allDocumentsCreationRequested;
        System.out.println(
            "\n\n##### listener CreateScheduleData : " +
            allDocumentsCreationRequested +
            "\n\n"
        );

        // Sample Logic //
        Schedule.createScheduleData(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
