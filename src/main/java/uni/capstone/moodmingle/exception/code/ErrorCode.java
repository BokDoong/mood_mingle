package uni.capstone.moodmingle.exception.code;

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
    INVALID_REQUEST_PARAMETER(CommonCode.REQUEST_PARAMETER.getCode(), BAD_REQUEST, "잘못된 요청 형식"),
    INVALID_JSON_TYPE(CommonCode.JSON_TYPE.getCode(), BAD_REQUEST, "JSON을 파싱할 수 없는 경우"),
    INVALID_METHOD_TYPE(CommonCode.METHOD_NOT_ALLOWED.getCode(), BAD_REQUEST, "지원하지 않는 HTTP 메서드인 경우"),
    DATA_INTEGRITY_VIOLATE(CommonCode.DATA_INTEGRITY.getCode(), BAD_REQUEST, "데이터 무결성을 위반한 경우"),
    FILE_SIZE(CommonCode.FILE_SIZE.getCode(), PAYLOAD_TOO_LARGE, "파일 용량이 초과된 경우"),
    SERVICE_UNAVAILABLE(CommonCode.SERVICE_UNAVAILABLE.getCode(), HttpStatus.SERVICE_UNAVAILABLE, "서비스에 문제가 발생한 경우"),
    REFRESH_TOKEN_NOT_FOUND(CommonCode.INVALID_REFRESH_TOKEN.getCode(), NOT_FOUND, "유효하지 않은 리프레쉬 토큰일 경우"),
    DATA_IO_UNAVAILABLE(CommonCode.DATA_IO.getCode(), PAYLOAD_TOO_LARGE, "데이터를 읽거나 쓸 수 없는 경우"),
    FAILED_LLM_NETWORKING(CommonCode.LLM_NETWORK.getCode(), NOT_IMPLEMENTED, "LLM 과의 통신이 실패한 경우"),
    FAILED_ASYNC_TASKING(CommonCode.ASYNC_TASKING.getCode(), NOT_IMPLEMENTED, "비동기 처리 작업을 실패한 경우"),

    // File I/O
    INPUT_FILE_EMPTY(FileCode.EMPTY.getCode(), BAD_REQUEST, "입력된 파일이 비어있는 경우"),
    NON_FILE_EXTENSION(FileCode.NON_EXTENSION.getCode(), NOT_EXTENDED, "파일의 확장자가 없는 경우"),
    INVALID_FILE_EXTENSION(FileCode.INVALID_EXTENSION.getCode(), BAD_REQUEST, "지원하지 않는 파일 확장자인 경우"),
    FAILED_IO_OPERATION(FileCode.FAILED_IO.getCode(), NOT_IMPLEMENTED, "I/O 작업 중 문제가 생긴 경우"),
    FAILED_PUT_FILE(FileCode.FAILED_PUT_OBJECT.getCode(), NOT_IMPLEMENTED, "원격 저장소에 이미지 업로드가 실패한 경우"),
    FAILED_DELETE_FILE(FileCode.FAILED_DELETE_OBJECT.getCode(), NOT_IMPLEMENTED, "원격 저장소에 이미지 업로드가 실패한 경우"),
    FAILED_DECODE_FILE_KEY(FileCode.FAILED_DECODE_OBJECT_KEY.getCode(), NOT_IMPLEMENTED, "파일 URL로부터 S3 Key 디코딩을 실패한 경우"),

    // Member
    MEMBER_NOT_FOUND(MemberCode.NOT_FOUND.getCode(), NOT_FOUND, "존재하지 않는 회원"),
    MEMBER_ALREADY_EXISTED(MemberCode.ALREADY_EXISTED.getCode(), CONFLICT, "이미 존재하는 회원인 경우"),

    // Diary
    DIARY_NOT_FOUND(DiaryCode.NOT_FOUND.getCode(), NOT_FOUND, "존재하지 않는 일기"),
    DIARY_ALREADY_EXIST(DiaryCode.ALREADY_EXIST.getCode(), CONFLICT, "이미 해당 날짜에 일기가 존재하는 경우"),
    ;

    /**
     * HTTP Status 와 각 Enum 에 담길 커스텀 예외 메세지와 에러코드
     */
    private final String code;
    private final HttpStatus status;
    private final String message;
}
