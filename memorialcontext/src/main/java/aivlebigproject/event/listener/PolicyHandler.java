package aivlebigproject.event.listener;

import aivlebigproject.config.kafka.KafkaProcessor;
import aivlebigproject.service.MemorialService;
import aivlebigproject.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;

import java.nio.charset.StandardCharsets;
import java.util.Map;

//<<< Clean Arch / Inbound Adaptor
@Service
@RequiredArgsConstructor
public class PolicyHandler {

    private final MemorialService memorialService;
    private final VideoService videoService;

    // ObjectMapper는 여러 번 생성할 필요 없이 재사용 가능합니다.
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    // 기존 Spring -> Spring 이벤트들은 그대로 유지
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

        memorialService.addFamily(event);
    }

    // --- 통합 이벤트 핸들러 (Python -> Java 및 기타 이벤트 처리) ---
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
                case "VideoCreated":
                    VideoCreated videoCreated = objectMapper.readValue(message, VideoCreated.class);
                    System.out.println("✅ VideoCreated 수신됨: " + videoCreated);
                    System.out.println("\n\n##### listener VideoCreated : " + videoCreated + "\n\n");
                    videoService.saveVideo(videoCreated);
                    break;

                default:
                    System.out.println("👀 처리되지 않은 이벤트 타입: " + type);
                    break;
            }
        } catch (Exception e) {
            System.err.println("⚠️ 이벤트 처리 중 오류 발생 (타입: " + type + "): " + e.getMessage());
            e.printStackTrace();
        }
    }

}
//>>> Clean Arch / Inbound Adaptor