package hh5.twogaether.domain.dog.entity;

import hh5.twogaether.domain.dog.dto.DogPatchRequestDto;
import hh5.twogaether.domain.dog.dto.DogSignupRequestDto;
import hh5.twogaether.domain.image.entity.Image;
import hh5.twogaether.domain.users.entity.User;
import hh5.twogaether.util.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Dog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dog_id")
    private Long id;

    @Column(nullable = false)
    private String dogName;

    @Column(nullable = false)
    private String dogSex;

    @Column(nullable = false)
    private String dogDetails;

    private boolean isDelete = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "dog")
    private List<Image> dogImages = new ArrayList<>();

    public Dog(DogSignupRequestDto dogSignupRequestDto, User user) {
        this.dogName = dogSignupRequestDto.getDogName();
        this.dogSex = dogSignupRequestDto.getDogSex();
        this.dogDetails = dogSignupRequestDto.getDogDetails();
        this.user = user;
    }

    public void patchDog(DogPatchRequestDto dogPatchRequestDto) {
        this.dogName = (dogPatchRequestDto.getDogName()==null)? this.getDogName() : dogPatchRequestDto.getDogName();
        this.dogSex = (dogPatchRequestDto.getDogSex()==null)? this.getDogSex() : dogPatchRequestDto.getDogSex();
        this.dogDetails = (dogPatchRequestDto.getDogDetails()==null)? this.getDogDetails() : dogPatchRequestDto.getDogDetails();
    }

    public void deleteDog(){
        this.isDelete = true;
    }

}
