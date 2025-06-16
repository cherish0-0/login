package hello.login.web.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @Slf4j 어노테이션은 Lombok 라이브러리에서 제공하는 기능으로,
 * 로깅을 쉽게 할 수 있도록 도와준다.
 *
 * @Repository 어노테이션은 Spring에서 이 클래스를 컴포넌트로 인식하게 해준다. (컴포넌트 스캔 -> 빈으로 등록됨)
 */
@Slf4j
@Repository
public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>(); // static으로 선언하여 애플리케이션 전체에서 공유되는 저장소
    private static long sequence = 0L; // id 생성용 시퀀스

    public Member save(Member member) {
        member.setId(++sequence);
        log.info("save: member={}", member);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(Long id) {
        return store.get(id);
    }

    public Optional<Member> findByLoginId(String loginId) {
        return findAll().stream()
                .filter(member -> member.getLoginId().equals(loginId))
                .findFirst();

    }

    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clear() {
        store.clear();
    }
}
