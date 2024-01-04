public class PlayerAnswer implements ComparableContent<PlayerAnswer> {
    /**
     * Die Antwort die der Spieler gegeben hat
     */
    public Integer answer;
    /**
     * Die wievielte Antwort die gegebene Antwort war
     */
    public Integer answerCount;

    /**
     * Konstruktor der eine Spielerantwort erstellt
     * @param answer Die gegebene Antwort (0: X gewinnt; 1: O gewinnt)
     * @param answerCount Die wievielte Antwort im aktuellen Spiel diese Antwort ist
     */
    public PlayerAnswer(int answer, int answerCount) {
        this.answer = answer;
        this.answerCount = answerCount;
    }

    /**
     * Implementation der Interface-Methode isGreater:
     * Wenn festgestellt wird, dass das Objekt, von dem die Methode aufgerufen
     * wird, bzgl. der gewuenschten Ordnungsrelation groesser als das Objekt
     * pContent ist, wird true geliefert. Sonst wird false geliefert.
     * @param pContent
     *          das mit dem aufrufenden Objekt zu vergleichende Objekt vom
     *          Typ ContentType
     * @return true, wenn das aufrufende Objekt groesser ist als pContent im Hinblick auf die answer und wenn sie dort gleich ist,
     *          im Hinblick auf den answerCount. Sonst false
     */
    @Override
    public boolean isGreater(PlayerAnswer pContent) {
        if (answer.equals(pContent.answer)) {
            return answerCount > pContent.answerCount;
        }
        return answer > pContent.answer;
    }

    /**
     * Implementation der Interface-Methode isEqual:
     * Wenn festgestellt wird, dass das Objekt, von dem die Methode aufgerufen
     * wird, bzgl. der gewuenschten Ordnungsrelation gleich gross wie das Objekt
     * pContent ist, wird true geliefert. Sonst wird false geliefert.
     * @param pContent
     *          das mit dem aufrufenden Objekt zu vergleichende Objekt vom
     *          Typ ContentType
     * @return true, wenn answer und answerCount gleich pContent sind. Sonst false
     */
    @Override
    public boolean isEqual(PlayerAnswer pContent) {
        if (answer.equals(pContent.answer)) {
            return answerCount.equals(pContent.answerCount);
        }
        return false;
    }

    /**
     * Implementation der Interface-Methode isLess:
     * Wenn festgestellt wird, dass das Objekt, von dem die Methode aufgerufen
     * wird, bzgl. der gewuenschten Ordnungsrelation kleiner als das Objekt
     * pContent ist, wird true geliefert. Sonst wird false geliefert.
     * @param pContent
     *          das mit dem aufrufenden Objekt zu vergleichende Objekt vom
     *          Typ ContentType
     * @return true, wenn das aufrufende Objekt kleiner ist als pContent im Hinblick auf die answer und wenn sie dort gleich ist,
     *          im Hinblick auf den answerCount. Sonst false
     */
    @Override
    public boolean isLess(PlayerAnswer pContent) {
        if (answer.equals(pContent.answer)) {
            return answerCount < pContent.answerCount;
        }
        return answer < pContent.answer;
    }
}
