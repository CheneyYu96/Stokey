package org.xeon.stockey.businessLogic.strategy.intermediateRepresentation;

import java.util.List;

/**
 * 条件的中间表示形式
 * Created by Sissel on 2016/6/18.
 */
public class ConditionIR
{
    public int indents; // 1, 2, 3, ..., and each indent would be replaced by 4 spaces
    public String calculations;
    public String boolExpr;

    public ConditionIR(int indents, String calculations, String boolExpr)
    {
        this.indents = indents;
        this.calculations = calculations;
        this.boolExpr = boolExpr;
    }

    public String toFinalRepresentation()
    {
        StringBuilder sb = new StringBuilder();

        sb.append(calculations);

        for (int i = 0; i < indents; i++)
        {
            sb.append(" ");
        }
        sb.append("if ");
        sb.append(boolExpr);
        sb.append(":\n");

        return sb.toString();
    }

    public static ConditionIR andConditionIR(ConditionIR firstIR, ConditionIR secondIR)
    {
        firstIR.calculations += "\n";
        firstIR.calculations += secondIR.calculations;
        firstIR.boolExpr = "(" + firstIR.boolExpr + " and " + secondIR.boolExpr + ")";

        return firstIR;
    }

    public static ConditionIR orConditionIR(ConditionIR firstIR, ConditionIR secondIR)
    {
        firstIR.calculations += "\n";
        firstIR.calculations += secondIR.calculations;
        firstIR.boolExpr = "(" + firstIR.boolExpr + " or " + secondIR.boolExpr + ")";

        return firstIR;
    }

    public static ConditionIR notConditionIR(ConditionIR firstIR)
    {
        firstIR.boolExpr = "not (" + firstIR.boolExpr + ")";

        return firstIR;
    }
}
