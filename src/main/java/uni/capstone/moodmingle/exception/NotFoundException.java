package uni.capstone.moodmingle.exception;

import uni.capstone.moodmingle.exception.code.ErrorCode;

/**
 * BusinessException 를 상속하고, 엔티티를 찾지 못했을 때 던져지는 커스텀 예외 클래스
 * 예외 정보의 가독성을 높이고, id 값으로 예외 메세지를 구성하기 위함
 *
 * @author ijin
 */
public class NotFoundException extends BusinessException {
    /**
     * 기본 생성자
     *
     * @param errorCode 커스텀 ErrorCode
     */
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * 찾지못한 id 값을 담은 메세지를 포함한 생성자
     * 이 메세지는 NotFoundException -> BusinessException -> RuntimeException 으로 전달되어 Logger 에서 로깅된다.
     *
     * @param errorCode 커스텀 ErrorCode
     * @param id 찾지 못한 엔티티의 id 값
     */
    public NotFoundException(ErrorCode errorCode, long id) {
        super(errorCode, "id " + id + " is not found");
    }
}
