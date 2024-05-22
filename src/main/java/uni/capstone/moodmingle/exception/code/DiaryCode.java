package uni.capstone.moodmingle.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DiaryCode {
    NOT_FOUND("D-001"),
    ALREADY_EXIST("D-002"),
    FAILED_ENCODING("D-003"),
    FAILED_DECODING("D-004"),
    ;

    private final String code;
}

