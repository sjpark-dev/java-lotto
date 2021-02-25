package lottogame.domain.stats;

import java.util.Objects;

public class Yield {
    private final float yield;

    public Yield(float yield) {
        this.yield = yield;
    }

    public static Yield of(int totalWinningAmount, Money money) {
        float yield = (float) totalWinningAmount / money.value();
        return new Yield(yield);
    }

    public float value() {
        return yield;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Yield yield1 = (Yield) o;
        return Float.compare(yield1.yield, yield) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(yield);
    }
}
