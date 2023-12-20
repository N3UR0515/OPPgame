package Interpreter;

public class StartServerExpression implements Expression {
    @Override
    public void interpret(Context context) {
        context.startServer();
    }
}
