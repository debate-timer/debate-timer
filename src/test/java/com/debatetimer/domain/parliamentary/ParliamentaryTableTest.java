package com.debatetimer.domain.parliamentary;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.debatetimer.domain.member.Member;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ParliamentaryTableTest {

    @Nested
    class Validate {

        @ParameterizedTest
        @ValueSource(strings = {"a bc가다", "가나 다ab"})
        void 테이블_이름은_영문과_한글_띄어쓰기만_가능하다(String name) {
            Member member = new Member("member");
            assertThatCode(() -> new ParliamentaryTable(member, name, "agenda", 10))
                    .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @ValueSource(ints = {0, ParliamentaryTable.NAME_MAX_LENGTH + 1})
        void 테이블_이름은_정해진_길이_이내여야_한다(int length) {
            Member member = new Member("member");
            assertThatThrownBy(() -> new ParliamentaryTable(member, "f".repeat(length), "agenda", 10))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테이블 이름은 1자 이상 %d자 이하여야 합니다".formatted(ParliamentaryTable.NAME_MAX_LENGTH));
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "\t", "\n"})
        void 테이블_이름은_적어도_한_자_있어야_한다(String name) {
            Member member = new Member("member");
            assertThatThrownBy(() -> new ParliamentaryTable(member, name, "agenda", 10))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테이블 이름은 1자 이상 %d자 이하여야 합니다".formatted(ParliamentaryTable.NAME_MAX_LENGTH));
        }

        @ParameterizedTest
        @ValueSource(strings = {"abc12", "가나다12", "abc\tde"})
        void 허용된_글자_이외의_문자는_불가능하다(String name) {
            Member member = new Member("member");
            assertThatThrownBy(() -> new ParliamentaryTable(member, name, "agenda", 10))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테이블 이름은 영문/한글만 가능합니다");
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1, -60})
        void 테이블_시간은_양수만_가능하다(int duration) {
            Member member = new Member("member");
            assertThatThrownBy(() -> new ParliamentaryTable(member, "name", "agenda", duration))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("시간은 양수만 가능합니다");
        }
    }
}