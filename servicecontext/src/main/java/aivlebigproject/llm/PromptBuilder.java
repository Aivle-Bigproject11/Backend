package aivlebigproject.llm;

import aivlebigproject.domain.dto.FilterCriteria;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class PromptBuilder {

    private static final String SYSTEM_PROMPT = String.join("\n",
            "당신은 고객 정보에 기반해 적절한 전환 서비스를 추천하는 마케팅 전문가입니다.",
            "고객 조건에 따라 가장 알맞은 전환 서비스 2가지를 선정하고, 따뜻하고 정중한 말투로 마케팅 메시지를 5줄 이내로 작성해주세요.",
            "",
            "주의 사항:",
            "- 반드시 아래 제공된 서비스 목록 중에서만 골라 추천하세요.",
            "- 목록에 없는 서비스를 임의로 만들거나 변경해서는 안 됩니다.",
            "- 출력은 반드시 한국어로만 작성하세요.",
            "- 아래 형식을 지켜 출력하세요.",
            "- 입력 내에 큰 따옴표를 쓰지 마세요. 텍스트 형식으로만 출력하세요.",
            "- 자녀가 있는 경우 자녀의 나이는 대략 -30을 해서 생각해주세요. 손자까지도 고려를 해주세요.",
            "",
            "[사용 가능한 전환서비스 목록]",
            "- 결혼정보 서비스",
            "- 프리미엄 골프용품 서비스",
            "- 프리하이모 가발 패키지 서비스",
            "- 여행 서비스",
            "- 웨딩 서비스",
            "- 골프 패키지",
            "- 돌잔치 서비스",
            "- 어학연수 서비스",
            "- 수연 서비스",
            "- 현대 리바트 홈 인테리어 서비스",
            "- 세라잼 홈 헬스케어 서비스",
            "- 장지 서비스"
    );

    public static List<Map<String, String>> asChatMessage(FilterCriteria c) {
        String userPrompt =
                "고객 정보:\n" +
                        "- 나이대: " + c.getAgeGroup() + "\n" +
                        "- 성별: " + c.getGender() + "\n" +
                        "- 질병 유무: " + c.getDisease() + "\n" +
                        "- 자녀 유무: " + c.getHasChildren() + "\n" +
                        "- 결혼 여부: " + c.getIsMarried() + "\n\n" +
                        "아래 형식을 따라 출력해주세요:\n\n" +
                        "\n"+
                        "[추천된 전환서비스]\n" +
                        "- [서비스명1]\n" +
                        "- [서비스명2]\n\n" +
                        "[메시지 내용]\n" +
                        "[00상조]에서 60대이신 고객님께 가장 어울리는 전환 서비스를 추천드립니다.\n" +
                        "해외 골프 여행의 꿈, 프리미엄 골프용품 서비스와 함께 시작해보세요!\n" +
                        "자녀의 글로벌 역량을 위한 어학연수 서비스도 준비되어 있습니다.\n" +
                        "00상조에서는 고객님을 위한 다양한 전환 서비스를 제공하고 있습니다.\n\n" +
                        "위의 예시를 참고하여 마케팅에 적합하고, 고객에게 정중하고 따뜻하게 메시지를 작성해주세요.";

        return List.of(
                Map.of("role", "system", "content", SYSTEM_PROMPT),
                Map.of("role", "user", "content", userPrompt)
        );
    }
}