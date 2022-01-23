package me.gagyeong.tutorial.repository;

import me.gagyeong.tutorial.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// JpaRepository를 extends하여 findAll, save 등의 JPA 메소드를 기본적으로 사용할 수 있게 됨
public interface UserRepository extends JpaRepository<User, Long> { // User 엔티티에 매핑되는 인터페이스
    // @EntityGraph 어노테이션은 쿼리가 수행될 때 Lazy 조회가 아닌 Eager 조회로 authorities 정보를 같이 가져옴
    @EntityGraph(attributePaths = "authorities")
    // findOneWithAuthoritiesByUsername 메소드는 username을 기준으로 User 정보를 가져올 때
    // 권한 정보도 같이 가져오게 됨
    Optional<User> findOneWithAuthoritiesByUsername(String username);
}