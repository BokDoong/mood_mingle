package uni.capstone.moodmingle.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberCode {
    NOT_FOUND("M-001"),
    ALREADY_EXISTED("M-002"),
    ENCODING_DATA("M-003"),
    DECODING_DATA("M-004"),
    MAKING_SECRET_KEY("M-005"),
    ;

    private final String code;
}
