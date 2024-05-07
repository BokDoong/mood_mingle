package uni.capstone.moodmingle.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DiaryCode {
    NOT_FOUND("D-001"),
    ;

    private final String code;
}

