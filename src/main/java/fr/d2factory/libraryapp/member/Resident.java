package fr.d2factory.libraryapp.member;

import fr.d2factory.libraryapp.configuration.Constants;
import fr.d2factory.libraryapp.member.exception.InsufficientFundsException;

public class Resident extends Member {

	public Resident(int wallet) {
		super(wallet);
	}
	
	@Override
	public void payBook(int numberOfDays) {
		
		int moneyToPay;

		moneyToPay = (numberOfDays <= Constants.RESIDENT_MAX_DAYS_BEFORE_LATE)?
				numberOfDays * Constants.RESIDENT_PRICE_DAY_BEFORE_LATE
				:
				(numberOfDays - Constants.RESIDENT_MAX_DAYS_BEFORE_LATE)
				* Constants.RESIDENT_PRICE_DAY_AFTER_LATE 
				+ Constants.RESIDENT_PRICE_DAY_BEFORE_LATE * Constants.RESIDENT_MAX_DAYS_BEFORE_LATE;

		if (moneyToPay > this.getWallet()) {
			throw new InsufficientFundsException("Insufficient funds to pay");
		} else {
			this.setWallet(this.getWallet() - moneyToPay);
		}
	}



}
