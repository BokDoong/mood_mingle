package uni.capstone.moodmingle.member.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MemberCreateDto {
    @NotBlank(message = "name is blank")
    private String name;
    @NotBlank(message = "email is blank")
    private String email;
    private String imageUrl;
}
