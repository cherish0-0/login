package hello.login.web;

import hello.login.web.member.Member;
import hello.login.web.member.MemberRepository;
import hello.login.web.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    // @GetMapping("/")
    public String home() {
        return "home";
    }

    // @GetMapping("/")
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

    // @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model) {

        // 세션 관리자에 저장된 회원 정보 조회
        Member member = (Member)sessionManager.getSession(request);


        if (member == null) {
            return "home"; // 쿠키가 없으면 홈 페이지로 이동
        }

        // 로그인 정보가 있으면 모델에 추가
        model.addAttribute("member", member);
        return "loginHome"; // 로그인된 홈 페이지로 이동
    }


    // @GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model) {

        // 새 세션을 생성하지 않고 기존 세션만 조회해야 하므로 false로 설정
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "home"; // 세션이 없으면 홈 페이지로 이동
        }

        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        if (loginMember == null) {
            return "home"; // 세션에 회원 데이터가 없으면 홈 페이지로 이동
        }

        // 세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome"; // 로그인된 홈 페이지로 이동
    }


    /**
     * @ SessionAttribute 어노테이션을 사용하여 세션에서 회원 정보를 조회
     * 세션을 찾고, 세션에 들어있는 데이터를 찾는 과정을 스프링이 처리해준다.
     */
    @GetMapping("/")
    public String homeLoginV3Spring(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {

        if (loginMember == null) {
            return "home"; // 세션에 회원 데이터가 없으면 홈 페이지로 이동
        }

        // 세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome"; // 로그인된 홈 페이지로 이동
    }
}