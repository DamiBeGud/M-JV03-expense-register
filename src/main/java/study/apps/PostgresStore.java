package study.apps;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.DuplicateKeyException;
class PostgresStore implements Store {
 final JdbcTemplate jdbc;PostgresStore(JdbcTemplate jdbc){this.jdbc=jdbc;}
 public void ready(){jdbc.queryForList("SELECT reference FROM expenses LIMIT 1");}
 public Expense add(Expense row){try{jdbc.update("INSERT INTO expenses(reference,category,amount_minor) VALUES(?,?,?)",row.reference(),row.category(),row.amountMinor());}catch(DuplicateKeyException e){throw new Problem(409,"duplicate_reference");}return row;}
 public Expense get(String reference){var rows=jdbc.query("SELECT reference,category,amount_minor FROM expenses WHERE reference=?",(r,n)->new Expense(r.getString(1),r.getString(2),r.getLong(3)),reference);return rows.isEmpty()?null:rows.get(0);}
 public Map<String,Long> totals(){Map<String,Long> result=new TreeMap<>();jdbc.query("SELECT category,SUM(amount_minor) FROM expenses GROUP BY category",r->{result.put(r.getString(1),r.getLong(2));});return result;}
}
