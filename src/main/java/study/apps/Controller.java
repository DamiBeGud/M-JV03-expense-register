package study.apps;
import java.util.*;
import org.springframework.web.bind.annotation.*;
record ExpenseInput(String reference,String category,Long amountMinor){}
record Expense(String reference,String category,long amountMinor){}
interface Store {void ready();Expense add(Expense row);Expense get(String reference);Map<String,Long> totals();}
class Domain {
 final Store store;Domain(Store store){this.store=store;}
 Expense add(ExpenseInput input){return store.add(new Expense(Rules.id(input.reference()),Rules.text(input.category(),40),Rules.amount(input.amountMinor())));}
 Expense get(String reference){Expense row=store.get(Rules.id(reference));if(row==null)throw new Problem(404,"not_found");return row;}
}
@RestController
class Controller {
 final Domain domain;Controller(Domain domain){this.domain=domain;}
 @GetMapping("/health/live")Map<String,String> live(){return Map.of("status","ok");}
 @GetMapping("/health/ready")Map<String,String> ready(){domain.store.ready();return Map.of("status","ready");}
 @PostMapping("/expenses")@ResponseStatus(org.springframework.http.HttpStatus.CREATED)Expense add(@RequestBody ExpenseInput input){return domain.add(input);}
 @GetMapping("/expenses/{reference}")Expense get(@PathVariable String reference){return domain.get(reference);}
 @GetMapping("/totals")Map<String,Long> totals(){return domain.store.totals();}
}
