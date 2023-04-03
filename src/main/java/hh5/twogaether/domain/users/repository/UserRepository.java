package hh5.twogaether.domain.users.repository;

import hh5.twogaether.domain.users.dto.EmailOnlyDto;
import hh5.twogaether.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u left join fetch u.dogs where u.id =:id")
    User findByIdFetchJoin(@Param("id") Long id);

    @Query("select u from User u where u.id = :id and u.isDelete = false")
    User findNotDeletedUserById(@Param("id") Long id);
    Optional<User> findByUsername(String username);
    @Query("select u.username from User u where u.username =:email")
    Optional<EmailOnlyDto> findByEmail(@Param("email") String email);
    Optional<User> findById(Long id);
    List<User> findAll();

}
