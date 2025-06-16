package hello.login.web.session;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 세션 관리 기능을 제공하는 컴포넌트
 */
@Component
public class SessionManager {

    public static final String SESSION_COOKIE_NAME = "sessionID";
    /**
     * 세션을 저장하는 저장소
     * - ConcurrentHashMap을 사용하여 멀티스레드 환경에서도 안전하게 동작하도록 함 (동시성 이슈 방지)
     */
    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    /**
     * 세션을 생성하고 저장하는 메소드
     */
    public void createSession(Object value, HttpServletResponse response) {

        // 세션 id 생성 및 저장
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);

        // 쿠키 생성
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        response.addCookie(mySessionCookie);
    }

    /**
     * 세션을 조회하는 메소드
     */

    public Object getSession(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie == null) {
            return null;
        }

        return sessionStore.get(sessionCookie.getValue());
    }

    /**
     * 세션을 만료시키는 메소드
     */
    public void expire(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie != null) {
            sessionStore.remove(sessionCookie.getValue());
        }
    }

    public Cookie findCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) {
            return null;
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);
    }
}
