package hh5.twogaether.domain.users.entity;

import hh5.twogaether.domain.dog.entity.Dog;
import hh5.twogaether.domain.mypage.dto.MyPageRequestDto;
import hh5.twogaether.domain.users.dto.SignUpRequestDto;
import hh5.twogaether.util.TimeStamped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Entity
@Getter
@NoArgsConstructor
@Table(name = "Users")
public class User extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    // username이 들어오는 필드
    @Column(nullable = false)
    private String nickname;

    // email이 들어오는 필드
    @Column(nullable = false)
    private String username;
    private String password;

    private Double latitude = 37.537187;   //  위도
    private Double longitude = 127.005476;  //  경도
    private String address;

    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column(nullable = false)
    private int emailCheck;

    @OneToMany(mappedBy = "user")
    private List<Dog> dogs = new ArrayList<>();

    private boolean isDelete = false;

    private int ranges = 400;

    public User(String nickname, String email, String provider, UserRoleEnum role) {
        this.nickname = nickname == null ? "닉네임을 설정해주세요." : nickname;
        this.username = email;
        this.password = provider;
        this.role = role;
    }

    public User(SignUpRequestDto signupRequestDto) {
        this.nickname = signupRequestDto.getUsername();
        this.username = signupRequestDto.getEmail();
        this.password = signupRequestDto.getPassword();
        this.role = signupRequestDto.getUserRole();
    }

    public void patchUser(MyPageRequestDto myPageRequestDto) {
        this.nickname = (myPageRequestDto.getUsername() == null) ? this.getNickname() : myPageRequestDto.getUsername();
        this.password = (myPageRequestDto.getPassword() == null) ? this.getPassword() : myPageRequestDto.getPassword();
        this.latitude = (myPageRequestDto.getLatitude() == null) ? this.getLatitude() : myPageRequestDto.getLatitude();
        this.longitude = (myPageRequestDto.getLongitude() == null) ? this.getLongitude() : myPageRequestDto.getLongitude();
        this.address = (myPageRequestDto.getAddress() == null) ? this.getAddress() : myPageRequestDto.getAddress();
        this.ranges = (myPageRequestDto.getRange() == 0) ? this.getRanges() : myPageRequestDto.getRange();
    }

    public void updateUserEmailCheck() {
        this.emailCheck = 1;
    }

    public void deleteUser() {
        this.isDelete = true;
    }
}
