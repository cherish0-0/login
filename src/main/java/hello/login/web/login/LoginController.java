package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.web.member.Member;
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

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm"; // 로그인 폼을 보여주는 뷰 이름
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm"; // 유효성 검사 실패 시 폼으로 돌아감
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        // 로그인 실패 처리 (글로벌 오류)
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        // 로그인 성공 처리 TODO
        return "redirect:/"; // 로그인 성공 후 루트 페이지로 리다이렉트
    }
}
