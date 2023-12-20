package Interpreter;

public class RestartServerExpression implements Expression {
    private Expression stopExpression;
    private Expression startExpression;

    public RestartServerExpression(Expression stop, Expression start) {
        this.stopExpression = stop;
        this.startExpression = start;
    }
    @Override
    public void interpret(Context context) {
        stopExpression.interpret(context);
        startExpression.interpret(context);
    }
}
