package Alumni.backend.infra.principal;

import Alumni.backend.module.domain.Member;
import Alumni.backend.module.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Member byEmail = memberRepository.findByEmail(email);
    if (byEmail == null) {
      throw new UsernameNotFoundException(email);
    }
    return new PrincipalDetails(byEmail);
  }
}
