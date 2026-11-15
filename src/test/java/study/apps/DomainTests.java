package study.apps;
import java.util.*;import org.junit.jupiter.api.Test;import static org.junit.jupiter.api.Assertions.*;
class DomainTests{static class Fake implements Store {Map<String,Expense> rows=new HashMap<>();public void ready(){} public Expense add(Expense r){if(rows.containsKey(r.reference()))throw new Problem(409,"duplicate");rows.put(r.reference(),r);return r;}public Expense get(String id){return rows.get(id);}public Map<String,Long> totals(){Map<String,Long> r=new HashMap<>();for(Expense e:rows.values())r.merge(e.category(),e.amountMinor(),Long::sum);return r;}}
 @Test void businessBoundariesAndConflicts(){Fake store=new Fake();Domain d=new Domain(store);d.add(new ExpenseInput("a","food",700L));d.add(new ExpenseInput("b","food",300L));assertEquals(1000L,store.totals().get("food"));assertThrows(Problem.class,()->d.add(new ExpenseInput("a","other",1L)));assertEquals(700,d.get("a").amountMinor());assertThrows(Problem.class,()->d.add(new ExpenseInput("c","food",-1L)));}
}
