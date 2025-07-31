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
            "",
            "[사용 가능한 전환서비스 목록]",
            "- 결혼정보 서비스",
            "- 프리미엄 골프용품 서비스",
            "- 프리하이모 가발 패키지 서비스",
            "- 여행서비스",
            "- 웨딩서비스",
            "- 골프패키지",
            "- 돌잔치 서비스",
            "- 어학연수 서비스",
            "- 수연 서비스",
            "- 현대 리바트 홈 인테리어 서비스",
            "- 세라잼 홈헬스케어서비스",
            "- 장지 서비스"
    );

    public static List<Map<String, String>> asChatMessage(FilterCriteria c) {
        String userPrompt = String.format(
                "고객 정보:\n" +
                        "- 나이대: %s\n" +
                        "- 성별: %s\n" +
                        "- 질병 유무: %s\n" +
                        "- 가족 구성: %s\n\n" +
                        "아래 형식을 따라 출력해주세요:\n\n" +
                        "[추천된 전환서비스]\n" +
                        "- [서비스명1]\n" +
                        "- [서비스명2]\n\n" +
                        "[메시지 내용]\n" +
                        "[위 고객 조건에 따른 마케팅 문구 작성]",
                c.getAgeGroup(), c.getGender(), c.getDisease(), c.getFamily()
        );

        return List.of(
                Map.of("role", "system", "content", SYSTEM_PROMPT),
                Map.of("role", "user", "content", userPrompt)
        );
    }
}
