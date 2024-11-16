package com.example.moono.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary Key

    @Column(nullable = false, unique = true)
    private String memberID; // 아이디 (중복 불가)

    @Column(nullable = false)
    private String password; // 비밀번호
}
