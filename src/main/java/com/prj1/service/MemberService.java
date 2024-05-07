package com.prj1.service;

import com.prj1.domain.CustomUser;
import com.prj1.domain.Member;
import com.prj1.mapper.BoardMapper;
import com.prj1.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class MemberService {
    private final MemberMapper mapper;
    private final BCryptPasswordEncoder encoder;
    private final BoardMapper boardMapper;

    public void signup(Member member) {
        member.setPassword(encoder.encode(member.getPassword()));

        mapper.insert(member);
    }

    public List<Member> list() {
        return mapper.selectAll();
    }

    public Member getid(Integer id) {
        return mapper.selcectById(id);
    }

    public void remove(Integer id) {
        boardMapper.deleteBoardByMemberId(id);
        mapper.deleteById(id);
    }

    public void modify(Member member) {
        if (member.getPassword() != null && member.getPassword().length() > 0) {
            member.setPassword(encoder.encode(member.getPassword()));
        } else {
            Member old = mapper.selcectById(member.getId());
            member.setPassword(old.getPassword());
        }
        mapper.update(member);
    }

    public String emailCheck(String email) {
        Member member = mapper.selcectByEmail(email);
        if (member == null) {
            //사용가능
            return "사용가능";
        } else {
            return "사용불가능";
        }
    }

    public boolean hasAccess(Integer id, Authentication authentication) {
        if (authentication == null) {
            return false;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUser user) {
            Member member = user.getMember();
            return member.getId().equals(id);
        }
        return false;
    }

    public boolean isAdmin(Authentication authentication) {
        Object o = authentication.getPrincipal();
        if (o instanceof CustomUser user) {
            return user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(s -> s.equals("admin"));
        }
        return false;
    }
}
