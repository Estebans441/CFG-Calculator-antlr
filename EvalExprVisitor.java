import java.util.HashMap;
import java.util.Map;

public class EvalExprVisitor extends ExprBaseVisitor<Integer> {
    Map<String, Integer> symTab;

    public EvalExprVisitor() {
        this.symTab = new HashMap<>();
    }

    @Override
    public Integer visitEnt(ExprParser.EntContext ctx) {
        return Integer.valueOf(ctx.ENT().getText());
    }

    @Override
    public Integer visitSumRes(ExprParser.SumResContext ctx) {
        try {
            if (ctx.op.getType() == ExprParser.Sum)
                return visit(ctx.expr(0)) + visit(ctx.expr(1));

            return visit(ctx.expr(0)) - visit(ctx.expr(1));
        } catch (Exception e) {
            System.out.println("Error en la expresión: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public Integer visitMulDiv(ExprParser.MulDivContext ctx) {
        try {
            if (ctx.op.getType() == ExprParser.Mul)
                return visit(ctx.expr(0)) * visit(ctx.expr(1));

            return visit(ctx.expr(0)) / visit(ctx.expr(1));
        } catch (Exception e) {
            System.out.println("Error en la expresión: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public Integer visitParen(ExprParser.ParenContext ctx) {
        try {
            return visit(ctx.expr());
        } catch (Exception e) {
            System.out.println("Error en la expresión: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public Integer visitAsig(ExprParser.AsigContext ctx) {
        try {
            String id = ctx.ID().getText();
            Integer value = visit(ctx.expr());
            symTab.put(id, value);
            return value;
        } catch (Exception e) {
            System.out.println("Error en la expresión: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public Integer visitId(ExprParser.IdContext ctx) {
        try {
            String id = ctx.ID().getText();
            Integer value = symTab.get(id);
            if (value == null)
                System.out.println("!!! " + id + " no esta definido !!!");
            return value;
        } catch (Exception e) {
            System.out.println("Error en la expresión: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public Integer visitPr(ExprParser.PrContext ctx) {
        try {
            Integer res = null;
            for (int i = 0; i < ctx.expr().size(); i++) {
                res = visit(ctx.expr(i));
                System.out.printf("%-35s= %d\n", ctx.expr(i).getText(), res);
                //System.out.println(ctx.expr(i).getText() + "\t= " + res);
            }
            return res;
        } catch (Exception e) {
            System.out.println("Error en la expresión: " + e.getMessage());
            return 0;
        }
    }
}
