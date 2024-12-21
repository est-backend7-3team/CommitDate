package est.commitdate.dto.member;

import lombok.Builder;
import lombok.Setter;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
public class CustomUserDetails implements OAuth2User {

    private Long id;
    private String name;
    private String email;
    private boolean additionalInfoCompleted;
    private Map<String, Object> attributes;

    @Setter
    private String role;

    @Builder
    public CustomUserDetails(String name, String email, String role, boolean additionalInfoCompleted, Map<String, Object> attributes, Long id) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.additionalInfoCompleted = additionalInfoCompleted;
        this.attributes = attributes;

    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getName() {
        return name;
    }

}