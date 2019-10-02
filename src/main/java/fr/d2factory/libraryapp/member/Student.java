package fr.d2factory.libraryapp.member;

import fr.d2factory.libraryapp.configuration.Constants;
import fr.d2factory.libraryapp.member.exception.InsufficientFundsException;;

public class Student extends Member {

	private boolean firstYear;

	public boolean isFirstYear() {
		return firstYear;
	}

	public Student(int wallet, boolean firstYear) {
		super(wallet);
		this.firstYear = firstYear;
		MaxDaysToKeepBook = Constants.STUDENT_MAX_DAYS_BEFORE_LATE;
	}

	@Override
	public void payBook(int numberOfDays) {
		int moneyToPay;

		moneyToPay = (numberOfDays <= Constants.STUDENT_MAX_DAYS_BEFORE_LATE)
				? numberOfDays * Constants.STUDENT_PRICE_BEFORE_LATE
				: (numberOfDays - Constants.STUDENT_MAX_DAYS_BEFORE_LATE) * Constants.STUDENT_PRICE_DAY_AFTER_LATE
						+ Constants.STUDENT_MAX_DAYS_BEFORE_LATE * Constants.STUDENT_PRICE_BEFORE_LATE;

		if (this.isFirstYear()) {
			moneyToPay -= (numberOfDays > Constants.STUDENT_NUMBER_DAYS_FIRST_YEAR_FREE
					? Constants.STUDENT_NUMBER_DAYS_FIRST_YEAR_FREE
					: numberOfDays) * 10;
		}

		if (this.getWallet() > moneyToPay) {
			this.setWallet(this.getWallet() - moneyToPay);
		} else {
			throw new InsufficientFundsException("Insufficient funds to pay");
		}
	}

}
