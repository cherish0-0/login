package hello.login.web.filter;

import hello.login.web.SessionConst;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    private static final String[] whitelist = {"/", "/members/add", "/login", "/logout", "/css/*"};
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            log.info("로그인 체크 필터 시작 {}", requestURI);

            if (isLoginCheckPath(requestURI)) {
                log.info("인증 체크 로직 실행 {}", requestURI);
                HttpSession session = httpRequest.getSession(false);
                if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
                    log.info("미인증 사용자 요청 {}", requestURI);
                    // 로그인으로 redirect
                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI);
                    return; // 필터 체인 중단, 필터/서블릿/컨트롤러가 더 호출되지 않고 redirect가 응답으로 적용되며 요청 종료
                }
            }

            // 인증이 완료되었거나, 인증 체크가 필요 없는 요청인 경우 필터 체인을 계속 진행
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("인증 체크 필터 종료 {}", requestURI);
        }
    }

    /**
     * whitelist에 있는 URL은 로그인 체크를 하지 않음
     */
    private boolean isLoginCheckPath(String requestURI) {
        // PatternMatchUtils.simpleMatch() 메서드를 사용하여 요청 URI가 whitelist에 있는지 확인
        // 요청 URI가 whitelist에 포함되어 있으면 false를 반환하여 로그인 체크를 하지 않음
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }
}
