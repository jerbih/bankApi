package bank.account.api.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name="BANK_ACCOUNT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccount implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1513789772183890241L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private double balance;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL,
			fetch = FetchType.LAZY,
			mappedBy = "bankaccount")
	@Builder.Default
	@ToString.Exclude
	private Set<Transaction> history = new HashSet<>();

}
