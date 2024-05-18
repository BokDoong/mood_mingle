package uni.capstone.moodmingle.config.security.oidc.utils;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@ExtendWith(MockitoExtension.class)
class OidcTokenVerifierTest {

    @Mock
    OidcTokenVerifier verifier;

    @Test
    public void 토큰_파싱() throws Exception {
        String token = "eyJraWQiOiI5ZjI1MmRhZGQ1ZjIzM2Y5M2QyZmE1MjhkMTJmZWEiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJkOTQ4MDllOWExZWEyZTBhOGQ1MTY0N2I1ODViZjY4ZCIsInN1YiI6IjM0NzU3MDExODIiLCJhdXRoX3RpbWUiOjE3MTU4NTgwMTUsImlzcyI6Imh0dHBzOi8va2F1dGgua2FrYW8uY29tIiwibmlja25hbWUiOiLsnbTsp4QiLCJleHAiOjE3MTU4Nzk2MTUsImlhdCI6MTcxNTg1ODAxNSwicGljdHVyZSI6Imh0dHA6Ly9rLmtha2FvY2RuLm5ldC9kbi9YdHZmOC9idHNGbWtld01pQS83bjFKVWZNZ3Jna24yV3FPdlJsazdrL2ltZ18xMTB4MTEwLmpwZyIsImVtYWlsIjoibGVlMjM3QGtha2FvLmNvbSJ9.g4p0ElYYKZ3nonJsXaKAOms_K7ZitBdKxMI70bi1gJW6t2b6YPa-rCG3SXTR-4LdvdOiFN8BWOowBezStA_PMS809SPeVEmvaz7TpqWenm8cSUf91msgRai2JfayYJV6-uvBxjVk2mod4pv0M5kkUgpZWB3V6k21LvoO3raYliola9b7BfQR1lEaD4QX7aTduA_ypQk0dNG7XIZ5a1p7Dl_-xliCN_qK_aa_6fOEFkH-KJoUcxQhL8JKj8u6cWauhDcEx1sFBuC6MSCjqR3Y3A0-mbYFl-8EdNZTOhgs6kW3EtDEcq2zjnUnKcxOj6PwYJ7KuvmoRwQIUL6cI_lqTA";
        String[] splitTokens = token.split("\\.");

        String parsedTokens = Jwts.parserBuilder()
                .build()
                .parseClaimsJwt(splitTokens[0] + "." + splitTokens[1] + ".")
                .toString();

        log.info(parsedTokens);
    }

    @Test
    public void 정규표현식_테스트() {
        String regex = "^/api/v1/diary/.*";
        Pattern pattern = Pattern.compile(regex);

        String testString0 = "/api/v1/diary/";
        String testString1 = "/api/v1/diary/123";
        String testString2 = "/api/v1/diary/entry";
        String testString3 = "/api/v1/user/123";

        Matcher matcher0 = pattern.matcher(testString0);
        Matcher matcher1 = pattern.matcher(testString1);
        Matcher matcher2 = pattern.matcher(testString2);
        Matcher matcher3 = pattern.matcher(testString3);

        log.info(String.valueOf(matcher0.matches()));
        log.info(String.valueOf(matcher1.matches()));
        log.info(String.valueOf(matcher2.matches()));
        log.info(String.valueOf(matcher3.matches()));
    }

}
