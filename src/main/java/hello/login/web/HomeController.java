package hello.login.web;

import hello.login.web.member.Member;
import hello.login.web.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    // @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/")
    public String homeLogin(@CookieValue(name="memberId", required = false) Long memberId, Model model) {

        if (memberId == null) {
            return "home"; // 쿠키가 없으면 홈 페이지로 이동
        }

        // 로그인
        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null) {
            return "home"; // 로그인 정보가 없으면 홈 페이지로 이동
        }

        // 로그인 정보가 있으면 모델에 추가
        model.addAttribute("member", loginMember);
        return "loginHome"; // 로그인된 홈 페이지로 이동
    }
}