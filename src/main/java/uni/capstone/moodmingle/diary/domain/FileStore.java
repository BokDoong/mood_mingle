package uni.capstone.moodmingle.diary.domain;

import org.springframework.web.multipart.MultipartFile;

/**
 * 일기 이미지 저장/삭제하기 위해 Infra 에서 구현할 인터페이스
 *
 * @author ijin
 */
public interface FileStore {

    /**
     * 이미지 업로드
     *
     * @param file 이미지 파일
     * @return 업로드된 이미지의 URL
     */
    String upload(MultipartFile file);

    /**
     * 이미지 삭제
     *
     * @param url 삭제할 이미지 URL
     */
    void delete(String url);
}
