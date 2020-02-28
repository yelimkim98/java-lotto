package lotto.controller;

import lotto.domain.Lotto;
import lotto.domain.LottoNumber;
import lotto.domain.Lottos;
import lotto.domain.LottosFactory;
import lotto.domain.Money;
import lotto.domain.ResultStatistic;
import lotto.domain.WinningInformation;
import lotto.view.IllegalUserInputException;
import lotto.view.InputView;
import lotto.view.OutputView;

public class ManualLottoController {

    public static void run() {
        try {
            runWithoutExceptionCatch();
        } catch (Exception e) {
            OutputView.printExceptionMessage(e);
        }
    }

    private static void runWithoutExceptionCatch() {
        Money money = new Money(InputView.getMoneyForLotto());
        LottosFactory.validateMoneyIsEnough(money);
        int amountOfManualLottos = getAmountOfManualLottos(money);

        Lottos lottos = LottosFactory.createLottosManual(
            money,
            InputView.getManualLottos(amountOfManualLottos)
        );

        int amountOfAutoLottos = lottos.getAmountOfLottos() - amountOfManualLottos;
        OutputView.printPurchasedLottos(amountOfManualLottos, amountOfAutoLottos, lottos);

        WinningInformation winningInformation = getWinningInformation();

        ResultStatistic result = ResultStatistic.calculate(lottos, winningInformation);
        OutputView.printResultStatistic(result, money);
    }

    private static int getAmountOfManualLottos(Money money) {
        int amountOfManualLottos = InputView.getManualLottoAmount();

        if (amountOfManualLottos > LottosFactory.getHowMuchCanBuyLottoWith(money)) {
            throw new IllegalUserInputException(
                "수동으로 구매할 로또의 갯수가 전체 로또의 갯수보다 클 수 없습니다."
            );
        }
        return amountOfManualLottos;
    }

    private static WinningInformation getWinningInformation() {
        Lotto winningLotto = Lotto.createLottoManual(InputView.getWinningLotto());
        LottoNumber bonus = LottoNumber.of(InputView.getBonusLottoNumber());
        return new WinningInformation(winningLotto, bonus);
    }
}
