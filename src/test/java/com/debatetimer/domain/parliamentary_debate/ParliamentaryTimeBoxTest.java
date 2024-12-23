package com.debatetimer.domain.parliamentary_debate;

import com.debatetimer.domain.BoxType;
import com.debatetimer.domain.Stance;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ParliamentaryTimeBoxTest {

    @Nested
    class Validate {

        @Test
        void 순서는_양수만_가능하다() {
            ParliamentaryTable table = new ParliamentaryTable();
            assertThatThrownBy(() -> new ParliamentaryTimeBox(table, 0, Stance.CONS, BoxType.OPENING, 10, 1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("순서는 양수만 가능합니다");
        }

        @Test
        void 시간은_양수만_가능하다() {
            ParliamentaryTable table = new ParliamentaryTable();
            assertThatThrownBy(() -> new ParliamentaryTimeBox(table, 1, Stance.CONS, BoxType.OPENING, 0, 1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("시간은 양수만 가능합니다");
        }
    }
}
