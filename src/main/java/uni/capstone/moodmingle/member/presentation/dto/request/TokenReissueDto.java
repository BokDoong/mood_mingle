package uni.capstone.moodmingle.member.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TokenReissueDto {
    @NotNull(message =  "RefreshToken is null")
    private String refreshToken;
}
