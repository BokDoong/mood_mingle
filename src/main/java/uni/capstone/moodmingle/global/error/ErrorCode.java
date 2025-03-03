package uni.capstone.moodmingle.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * 커스텀 ErrorCode 들을 한 번에 관리하여 처리하기 위한 커스텀 ErrorCode
 *
 * @author ijin
 */
@AllArgsConstructor
@Getter
public enum ErrorCode {

    /**
     * 도메인 별로 예외 정보를 담은 Enum 값
     */
    // Common
    INVALID_REQUEST_PARAMETER("C-001", BAD_REQUEST, "잘못된 요청 형식"),
    INVALID_JSON_TYPE("C-002", BAD_REQUEST, "JSON을 파싱할 수 없는 경우"),
    INVALID_METHOD_TYPE("C-003", BAD_REQUEST, "지원하지 않는 HTTP 메서드인 경우"),
    DATA_INTEGRITY_VIOLATE("C-004", BAD_REQUEST, "데이터 무결성을 위반한 경우"),
    EXCEED_FILE_SIZE("C-005", PAYLOAD_TOO_LARGE, "파일 용량이 초과된 경우"),
    SERVICE_UNAVAILABLE("C-006", HttpStatus.SERVICE_UNAVAILABLE, "서비스에 문제가 발생한 경우"),
    INVALID_REFRESH_TOKEN("C-007", NOT_FOUND, "유효하지 않은 리프레쉬 토큰일 경우"),
    MISSING_REQUESTED_DATA("C-008", BAD_REQUEST, "요청에서 빠진 파라미터 혹은 데이터가 있을 때"),
    NONE_REQUESTED_URI("C-009", BAD_REQUEST, "요청한 URI 가 잘못된 경우"),
    TOO_MANY_REQUESTS("C-010", HttpStatus.TOO_MANY_REQUESTS, "요청 횟수가 초과된 경우"),
    FAILED_IO_OPERATION("C-011", NOT_IMPLEMENTED, "외부 I/O 작업 중 문제가 생긴 경우"),
    CIRCUIT_BREAKER_OPENED("C-012", NOT_IMPLEMENTED, "외부 API 서버 장애로 인해 서킷브레이커가 오픈된 경우"),

    // KMS
    FAILED_ENCODE_DATA("K-001", NOT_IMPLEMENTED, "KMS Key로 데이터 인코딩을 실패한 경우"),
    FAILED_DECODE_DATA("K-002", NOT_IMPLEMENTED, "KMS Key로 데이터 디코딩을 실패한 경우"),

    // S3
    INPUT_FILE_EMPTY("F-001", BAD_REQUEST, "입력된 파일이 비어있는 경우"),
    NON_FILE_EXTENSION("F-002", NOT_EXTENDED, "파일의 확장자가 없는 경우"),
    INVALID_FILE_EXTENSION("F-003", BAD_REQUEST, "지원하지 않는 파일 확장자인 경우"),
    FAILED_PUT_FILE("F-004", NOT_IMPLEMENTED, "원격 저장소에 이미지 업로드가 실패한 경우"),
    FAILED_DELETE_FILE("F-005", NOT_IMPLEMENTED, "원격 저장소에 이미지 삭제를 실패한 경우"),
    FAILED_DECODE_KEY("F-006", NOT_IMPLEMENTED, "파일 URL로부터 S3 Key 디코딩을 실패한 경우"),

    // Member
    MEMBER_NOT_FOUND("M-001", NOT_FOUND, "존재하지 않는 회원"),
    MEMBER_ALREADY_EXISTED("M-002", CONFLICT, "이미 존재하는 회원인 경우"),
    FAILED_ENCODING_DATA("M-003", NOT_IMPLEMENTED, "CBC 비밀키 혹은 초기 벡터 암호화에 실패한 경우"),
    FAILED_DECODING_DATA("M-004", NOT_IMPLEMENTED, "CBC 비밀키 혹은 초기 벡터 복호화에 실패한 경우"),
    FAILED_MAKING_SECRET_KEY("M-005", NOT_IMPLEMENTED, "CBC 비밀키 생성에 실패한 경우"),

    // Diary
    DIARY_NOT_FOUND("D-001", NOT_FOUND, "존재하지 않는 일기"),
    DIARY_ALREADY_EXIST("D-002", CONFLICT, "이미 해당 날짜에 일기가 존재하는 경우"),
    FAILED_ENCODING_DIARY("D-003", NOT_IMPLEMENTED, "일기 및 답변의 인코딩이 실패한 경우"),
    FAILED_DECODING_DIARY("D-004", NOT_IMPLEMENTED, "일기 및 답변의 디코딩이 실패한 경우")
    ;

    /**
     * HTTP Status 와 각 Enum 에 담길 커스텀 예외 메세지와 에러코드
     */
    private final String code;
    private final HttpStatus status;
    private final String message;
}
