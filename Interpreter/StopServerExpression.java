package Interpreter;

public class StopServerExpression implements Expression {
    @Override
    public void interpret(Context context) {
        context.stopServer();
    }
}
