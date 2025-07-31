// ========================================
// FILENAME: aivlebigproject/funeralcontext/src/main/java/aivlebigproject/domain/PolicyHandler.java
// 역할 : 
// ========================================

package aivlebigproject.infra;

import aivlebigproject.config.kafka.KafkaProcessor;
import aivlebigproject.domain.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.springframework.messaging.handler.annotation.Headers;

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
    CustomerRepository customerRepository;

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
    
    // --- Python -> Java 이벤트 처리 ---
    // ObjectMapper는 여러 번 생성할 필요 없이 재사용 가능합니다.
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @StreamListener(KafkaProcessor.INPUT)
    public void handleAllEvents(@Payload String message, @Headers Map<String, Object> headers) {
        Object typeHeader = headers.get("type");
        String type = null;

        // 헤더의 'type'을 문자열로 안전하게 변환합니다.
        if (typeHeader instanceof byte[]) {
            type = new String((byte[]) typeHeader, StandardCharsets.UTF_8);
        } else if (typeHeader instanceof String) {
            type = (String) typeHeader;
        }

        if (type == null) {
            System.out.println("👀 수신된 이벤트에 'type' 헤더가 없습니다.");
            return;
        }

        // 이벤트 타입에 따라 적절한 Policy를 호출합니다.
        try {
            switch (type) {

                // --- Python -> Java 이벤트 처리 ---
                case "ObituaryDocumentGenerated":
                    ObituaryDocumentGenerated obDocGen = objectMapper.readValue(message, ObituaryDocumentGenerated.class);
                    System.out.println("✅ ObituaryDocumentGenerated 수신됨: " + obDocGen);
                    Obituary.updateObituaryData(obDocGen);
                    break;

                case "DeathReportDocumentGenerated":
                    DeathReportDocumentGenerated drDocGen = objectMapper.readValue(message, DeathReportDocumentGenerated.class);
                    System.out.println("✅ DeathReportDocumentGenerated 수신됨: " + drDocGen);
                    DeathReport.updateDeathReportData(drDocGen);
                    break;

                case "ScheduleDocumentGenerated":
                    ScheduleDocumentGenerated schDocGen = objectMapper.readValue(message, ScheduleDocumentGenerated.class);
                    System.out.println("✅ ScheduleDocumentGenerated 수신됨: " + schDocGen);
                    Schedule.updateScheduleData(schDocGen);
                    break;

                default:
                    System.out.println("👀 처리되지 않은 이벤트 타입: " + type);
                    break;
            }
        } catch (Exception e) {
            System.err.println("⚠️ 이벤트 처리 중 오류 발생 (타입: " + type + "): " + e.getMessage());
        }
    }

}
//>>> Clean Arch / Inbound Adaptor
