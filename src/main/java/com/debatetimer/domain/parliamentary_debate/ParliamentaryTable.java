package com.debatetimer.domain.parliamentary_debate;

import com.debatetimer.domain.member.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParliamentaryTable {

    private static final String NAME_REGEX = "^[a-zA-Z가-힣\\s]+$";
    public static final int NAME_MAX_LENGTH = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    private String name;

    @NotNull
    private String agenda;

    @NotNull
    private int duration;

    public ParliamentaryTable(Member member, String name, String agenda, int duration) {
        validate(name, duration);
        this.member = member;
        this.name = name;
        this.agenda = agenda;
        this.duration = duration;
    }

    private void validate(String name, int duration) {
        if (name.isBlank() || name.length() > NAME_MAX_LENGTH) {
            throw new IllegalArgumentException("테이블 이름은 1자 이상 %d자 이하여야 합니다".formatted(NAME_MAX_LENGTH));
        }
        if (!name.matches(NAME_REGEX)) {
            throw new IllegalArgumentException("테이블 이름은 영문/한글만 가능합니다");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("시간은 양수만 가능합니다");
        }
    }
}