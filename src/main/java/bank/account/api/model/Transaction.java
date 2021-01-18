package bank.account.api.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Transaction implements Serializable {
	
	public enum TransactionType {
		DEPOSIT,
		WITHDRAWAL
	}
	
	/**
	 * serial Version UID
	 */
	private static final long serialVersionUID = 433368997048406655L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private double amount;
	private LocalDateTime date;
	private String comment;
	@Enumerated(EnumType.STRING)
	private TransactionType transactionType;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "account_id", nullable = false, insertable=false, updatable=false)
	private BankAccount bankaccount;
}
