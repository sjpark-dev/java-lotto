package lottogame.domain.lotto;

import lottogame.domain.Money;
import lottogame.domain.Rank;
import lottogame.domain.stats.LottoResults;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class LottosTest {
    @Test
    void 객체_생성() {
        LottoGenerator.generate();
        List<Lotto> lottoGroup = new ArrayList<>();
        lottoGroup.add(new Lotto(LottoGenerator.makeNumbers()));
        lottoGroup.add(new Lotto(LottoGenerator.makeNumbers()));
        lottoGroup.add(new Lotto(LottoGenerator.makeNumbers()));
        lottoGroup.add(new Lotto(LottoGenerator.makeNumbers()));

        Lottos lottos = new Lottos(lottoGroup);
        for (Lotto lotto : lottoGroup) {
            assertThat(lottos.values()).contains(lotto);
        }
    }

    @Test
    void 로또_매칭_테스트1() {
        matchTest(new String[]{"1, 2, 3, 4, 45, 44", "1, 2, 3, 43, 44, 45", "1, 2, 3, 4, 5, 7", "1, 2, 3, 4, 5, 44"},
                new WinningLotto("1, 2, 3, 4, 5, 6", "7"),
                "4000",
                new int[]{1, 1, 1, 1, 0});
    }

    @Test
    void 로또_매칭_테스트2() {
        matchTest(new String[]{
                        "8, 21, 23, 41, 42, 43", "3, 5, 11, 16, 32, 38", "7, 11, 16, 35, 36, 44",
                        "1, 8, 11, 31, 41, 42", "13, 14, 16, 38, 42, 45", "7, 11, 30, 40, 42, 43",
                        "2, 13, 22, 32, 38, 45", "23, 25, 33, 36, 39, 41", "1, 3, 5, 14, 22, 45",
                        "5, 9, 38, 41, 43, 44", "2, 8, 9, 18, 19, 21", "13, 14, 18, 21, 23, 35",
                        "17, 21, 29, 37, 42, 45", "3, 8, 27, 30, 35, 44"},
                new WinningLotto("1, 2, 3, 4, 5, 6", "7"),
                "14000",
                new int[]{1, 0, 0, 0, 0});
    }

    private void matchTest(String[] lottoNumbersGroup, WinningLotto winningLotto, String money, int[] expectedCount) {
        Lottos lottos = new Lottos(makeLottos(lottoNumbersGroup));
        LottoGame lottoGame = new LottoGame(lottos, new Money(money));
        LottoResults lottoResults = lottoGame.Results(winningLotto);
        Map<Rank, Integer> sameResult = new LinkedHashMap<>();

        sameResult.put(Rank.FIFTH, expectedCount[0]);
        sameResult.put(Rank.FOURTH, expectedCount[1]);
        sameResult.put(Rank.THIRD, expectedCount[2]);
        sameResult.put(Rank.SECOND, expectedCount[3]);
        sameResult.put(Rank.FIRST, expectedCount[4]);
        assertThat(lottoResults.values()).isEqualTo(sameResult);

        float expected = (5000 * expectedCount[0]
                + 50000 * expectedCount[1]
                + 1500000 * expectedCount[2]
                + 30000000 * expectedCount[3]
                + 2000000000 * expectedCount[4]) / Float.valueOf(money);
        assertThat(lottoResults.calculateYield(new Money(money))).isEqualTo(expected);
    }

    List<Lotto> makeLottos(String[] inputs) {
        List<Lotto> lottos = new ArrayList<>();
        for (String input : inputs) {
            String[] numbers = input.split(", ");
            List<LottoNumber> nums = Arrays.stream(numbers)
                    .map(x -> new LottoNumber(Integer.parseInt(x)))
                    .collect(Collectors.toList());
            lottos.add(new Lotto(nums));
        }
        return lottos;
    }
}
