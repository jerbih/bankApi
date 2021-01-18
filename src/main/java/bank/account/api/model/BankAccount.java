package bank.account.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name="BANK_ACCOUNT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class BankAccount implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1513789772183890241L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long number;
	
	private double balance;
	
	@OneToMany(mappedBy = "bankaccount")
	private List<Transaction> history = new ArrayList<>() ;
	
	
	
	
}
