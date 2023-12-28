public class PlayerAnswer implements ComparableContent<PlayerAnswer> {
    public Integer answer;
    public Integer answerCount;

    public PlayerAnswer(int answer, int answerCount) {
        this.answer = answer;
        this.answerCount = answerCount;
    }
    @Override
    public boolean isGreater(PlayerAnswer pContent) {
        if (answer.equals(pContent.answer)) {
            return answerCount > pContent.answerCount;
        }
        return answer > pContent.answer;
    }

    @Override
    public boolean isEqual(PlayerAnswer pContent) {
        if (answer.equals(pContent.answer)) {
            return answerCount.equals(pContent.answerCount);
        }
        return false;
    }

    @Override
    public boolean isLess(PlayerAnswer pContent) {
        if (answer.equals(pContent.answer)) {
            return answerCount < pContent.answerCount;
        }
        return answer < pContent.answer;
    }
}
