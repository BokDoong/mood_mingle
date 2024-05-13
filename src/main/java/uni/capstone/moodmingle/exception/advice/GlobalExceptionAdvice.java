package uni.capstone.moodmingle.exception.advice;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import uni.capstone.moodmingle.exception.BusinessException;
import uni.capstone.moodmingle.exception.ErrorResponse;
import uni.capstone.moodmingle.exception.code.ErrorCode;

import javax.naming.SizeLimitExceededException;
import java.util.stream.Collectors;

/**
 * Spring 전역에서의 예외처리를 위한 Advice 클래스.
 * 로깅 및 발생한 예외마다의 ErrorResponse 로 바꾸어 핸들링
 *
 * @author ijin
 */
@RestControllerAdvice
public class GlobalExceptionAdvice {

    // 비즈니스 예외 처리시 발생
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        return createErrorResponse(e, e.getErrorCode());
    }

    // javax.validation.Valid or @Validated 으로 binding error 발생시 발생
    // HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity<ErrorResponse> methodArgumentValidation(Exception e) {
        return createErrorResponse(e, ErrorCode.INVALID_REQUEST_PARAMETER);
    }

    // @ModelAttribute 으로 바인딩 에러시 발생
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> bindException(BindException e) {
        return createErrorResponse(e, ErrorCode.INVALID_REQUEST_PARAMETER);
    }

    // 지원하지 않은 HTTP Method 호출 할 경우 발생
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> requestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return createErrorResponse(e, ErrorCode.INVALID_METHOD_TYPE);
    }

    // JSON 형식 지키지 않았을 시 발생
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> invalidHttpMessageParsing(HttpMessageNotReadableException e) {
        return createErrorResponse(e, ErrorCode.INVALID_JSON_TYPE);
    }

    // 데이터 잘못 넘어갔을 경우 발생
    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<ErrorResponse> illegalArgumentException(IllegalArgumentException e) {
        return createErrorResponse(e, ErrorCode.INVALID_REQUEST_PARAMETER);
    }

    // 데이터 무결성 위반한 경우 발생
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<ErrorResponse> dataIntegrityViolationException(DataIntegrityViolationException e) {
        return createErrorResponse(e, ErrorCode.DATA_INTEGRITY_VIOLATE);
    }

    // 이미지 크기 초과시 발생
    @ExceptionHandler({MaxUploadSizeExceededException.class, SizeLimitExceededException.class, MultipartException.class})
    protected ResponseEntity<ErrorResponse> imageFileSizeExceedException(Exception e) {
        return createErrorResponse(e, ErrorCode.FILE_SIZE);
    }

    // RequestPart 요청에서 빠진 파라미터가 있을 때
    @ExceptionHandler(MissingServletRequestPartException.class)
    protected ResponseEntity<ErrorResponse> missingServletRequestPartException(MissingServletRequestPartException e) {
        return createErrorResponse(e, ErrorCode.MISSING_REQUESTED_DATA);
    }

    // 설계되지 않은 URI 로 요청한 경우
    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<ErrorResponse> noResourceFoundException(NoResourceFoundException e) {
        return createErrorResponse(e, ErrorCode.NONE_REQUESTED_URI);
    }

    // 나머지 에러 여기서 핸들링
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        return createErrorResponse(e, ErrorCode.SERVICE_UNAVAILABLE);
    }

    // Create ExceptionResponse
    private ResponseEntity<ErrorResponse> createErrorResponse(Exception e, ErrorCode errorCode) {
        ResponseEntity<ErrorResponse> response;
        if (e.getClass().equals(MethodArgumentNotValidException.class)) {
            // MethodArgumentNotValidException 인 경우, 어떤 파라미터가 유효하지 못한지 ErrorResponse 에 정보 추가
            response = ErrorResponse.toResponseEntity(ErrorCode.INVALID_REQUEST_PARAMETER,
                    ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors().stream()
                            .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(" and ")));
        } else {
            response = ErrorResponse.toResponseEntity(errorCode);
        }

        // Logging And Return with Exception
        ExceptionResponseLogger.logResponse(response, e);
        return response;
    }
}
