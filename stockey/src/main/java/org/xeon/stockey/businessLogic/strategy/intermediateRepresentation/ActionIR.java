package org.xeon.stockey.businessLogic.strategy.intermediateRepresentation;

/**
 * Action的中间表示
 * Created by Sissel on 2016/6/19.
 */
public class ActionIR
{
    public int indents;
    public String calculations;

    public ActionIR(int indents, String calculations)
    {
        this.indents = indents;
        this.calculations = calculations;
    }

    public String toFinalRepresentation()
    {
        return calculations;
    }
}
