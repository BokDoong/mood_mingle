package uni.capstone.moodmingle.diary.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Image 값객체
 *
 * @author ijin
 */
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Image {

    /**
     * DB 에 저장되는 이미지 url
     */
    @Column(name = "image_url")
    private String imageUrl;

    /**
     * 기본 생성자
     *
     * @param imageUrl 이미지 URL
     */
    @Builder
    public Image(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
