package hello.login.web.member;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Controller 어노테이션은 Spring MVC에서 이 클래스를 컨트롤러로 인식하게 해준다.
 * @RequiredArgsConstructor 어노테이션은 Lombok 라이브러리에서 제공하는 기능으로,
 * 이 클래스에 생성자를 자동으로 생성해준다.
 * @RequestMapping 어노테이션은 HTTP 요청을 특정 메서드에 매핑해주는 역할을 한다.
 *  - 이 클래스의 모든 메소드에 대해 "/members" 경로를 기본으로 사용하게 한다.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/add")
    public String addForm(@ModelAttribute("member") Member member) {
        return "members/addMemberForm";
    }


    /**
     * @ModelAttribute 어노테이션은 폼 데이터를 Member 객체로 변환해준다.
     */
    @PostMapping("/add")
    public String save(@Valid @ModelAttribute Member member, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "members/addMemberForm"; // 유효성 검사 실패 시 폼으로 돌아감
        }

        memberRepository.save(member);
        return "redirect:/"; // 루트 페이지로 리다이렉트
    }

}
