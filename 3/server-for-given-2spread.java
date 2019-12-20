

public interface IFormula {
}

public class Constant implements IFormula {
  double number;
  public Constant(double number) {
    this.number = number;
  }
}
public class Reference implements IFormula {
  ICell ref;
  public Reference(ICell ref) {
    this.ref = ref;
  }
}

public abstract class ArithmeticFunction implements IFormula {
  IFormula f1;
  IFormula f2;
  public ArithmeticFunction(IFormula f1, IFormula f2) {
    this.f1 = f1;
    this.f2 = f2;
  }
  public abstract IFormula Operation();
}

public class Addition extends ArithmeticFunction {

  public Addition(IFormula f1, IFormula f2) {
    super(f1, f2);
  }

  @Override
  public IFormula Operation() {
    if (f1 instanceof Constant && f2 instanceof Constant) {
      return new Constant(f1.number + f2.number);
    }
    else if (f1 instanceof Reference && f2 instanceof Constant) {
      Addition add1 = new Addition(f1.ref, f2);
      return add1.Operation();
    }
    else if (f2 instanceof Reference && f1 instanceof Constant) {
      Addition add1 = new Addition(f1, f2.ref);
      return add1.Operation();
    }
    else if (f1 instanceof Reference && f2 instanceof Reference) {
      Addition add1 = new Addition(f1.ref, f2.ref);
      return add1.Operation();
    }
    else {
      System.out.println("cannot perform addition on the two Formulas!");
    }
  }
}
public class Multiplication extends ArithmeticFunction {
  public Multiplication(IFormula f1, IFormula f2) {
    super(f1, f2);
  }

  @Override
  public IFormula Operation() {
    if (f1 instanceof Constant && f2 instanceof Constant) {
      return new Constant(f1.number * f2.number);
    }
    else if (f1 instanceof Reference && f2 instanceof Constant) {
      Multiplication mul1 = new Multiplication(f1.ref, f2);
      return mul1.Operation();
    }
    else if (f2 instanceof Reference && f1 instanceof Constant) {
      Multiplication mul1 = new Multiplication(f1, f2.ref);
      return mul1.Operation();
    }
    else if (f1 instanceof Reference && f2 instanceof Reference) {
      Multiplication mul1 = new Multiplication(f1.ref, f2.ref);
      return mul1.Operation();
    }
    else {
      System.out.println("cannot perform multiplication on the two Formulas!");
    }
  }
}

public class Position {
  int x;
  int y;
  public Position(int x, int y) {
    this.x = x;
    this.y = y;
  }
}

public interface ICell {
  public void ModifyF(IFormula f1);
}
public class Cell implements ICell {
  Position p;
  IFormula f;
  public Cell(Position p, IFormula f) {
    this.p = p;
    this.f = f;
  }
  public void ModifyF(IFormula f1) {
    this.f = f1;
  }
}



public interface ISpreadSheet {
}
public class SpreadSheet implements ISpreadSheet {
  ICell[][] table;
  public SpreadSheet(int row, int column) {
    this.table = new ICell[row][column];

  }
}

