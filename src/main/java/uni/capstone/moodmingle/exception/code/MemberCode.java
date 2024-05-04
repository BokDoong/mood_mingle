package uni.capstone.moodmingle.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberCode {
    NOT_FOUND("M-001"),
    ALREADY_EXISTED("M-002"),
    ;

    private final String code;
}
