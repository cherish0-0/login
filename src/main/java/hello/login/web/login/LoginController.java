package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.web.member.Member;
import hello.login.web.session.SessionManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Data
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;

    // @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm"; // 로그인 폼을 보여주는 뷰 이름
    }

    /**
     * HttpServletResponse를 사용해 HTTP 응답을 제어
     */
    // @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm"; // 유효성 검사 실패 시 폼으로 돌아감
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        // 로그인 실패 처리 (글로벌 오류)
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        // 로그인 성공 처리

        // 쿠키에 시간 정보 주지 않으면 세션 쿠키 (브라우저 종료 시 삭제됨)
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        response.addCookie(idCookie);

        return "redirect:/"; // 로그인 성공 후 루트 페이지로 리다이렉트
    }

    // @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        expireCookie(response, "memberId");
        return "redirect:/"; // 로그아웃 후 루트 페이지로 리다이렉트
    }



    @GetMapping("/login")
    public String loginFormV2(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm"; // 로그인 폼을 보여주는 뷰 이름
    }

    /**
     * HttpServletResponse를 사용해 HTTP 응답을 제어
     */
    @PostMapping("/login")
    public String loginV2(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm"; // 유효성 검사 실패 시 폼으로 돌아감
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        // 로그인 실패 처리 (글로벌 오류)
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        // 로그인 성공 처리

        // 세션 관리자를 통해 세션 생성, 회원 데이터 보관
        sessionManager.createSession(loginMember, response);
        return "redirect:/"; // 로그인 성공 후 루트 페이지로 리다이렉트
    }

    @PostMapping("/logout")
    public String logoutV2(HttpServletRequest request) {
        sessionManager.expire(request);
        return "redirect:/"; // 로그아웃 후 루트 페이지로 리다이렉트
    }




    /**
     * 쿠키를 만료시키는 메소드
     * MaxAge가 0인 쿠키를 response에 추가하여 브라우저에서 삭제하도록 함
     */
    private static void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
