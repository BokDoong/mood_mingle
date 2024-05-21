package uni.capstone.moodmingle.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileCode {
    EMPTY("F-001"),
    NON_EXTENSION("F-002"),
    INVALID_EXTENSION("F-003"),
    FAILED_IO("F-004"),
    FAILED_PUT_OBJECT("F-005"),
    FAILED_DELETE_OBJECT("F-006"),
    FAILED_DECODE_OBJECT_KEY("F-007")
    ;

    private final String code;
}
