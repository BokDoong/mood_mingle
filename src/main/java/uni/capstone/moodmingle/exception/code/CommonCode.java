package uni.capstone.moodmingle.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 전역에서 공통으로 발생할 수 있는 커스텀 Exception 에러 코드
 *
 * @author ijin
 */
@Getter
@RequiredArgsConstructor
public enum CommonCode {
    REQUEST_PARAMETER("C-001"),
    JSON_TYPE("C-002"),
    METHOD_NOT_ALLOWED("C-003"),
    FILE_SIZE("C-004"),
    SERVICE_UNAVAILABLE("C-005"),
    DATA_INTEGRITY("C-006"),
    INVALID_REFRESH_TOKEN("C-007"),
    DATA_IO("C-008"),
    LLM_NETWORK("C-009"),
    ASYNC_TASKING("C-010")
    ;

    private final String code;
}
