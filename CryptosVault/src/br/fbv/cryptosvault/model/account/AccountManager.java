package br.fbv.cryptosvault.model.account;

import android.content.Context;
import br.fbv.cryptosvault.model.exception.InvalidEmailException;
import br.fbv.cryptosvault.model.exception.InvalidPasswordException;
import br.fbv.cryptosvault.model.exception.UserNotFoundException;
import br.fbv.cryptosvault.model.objects.User;
import br.fbv.cryptosvault.model.persistence.repositories.UserRepository;

/**
 * Project: Cryptos Vault Class: AccountManager REVISION HISTORY Date Developer Comment ---------- -------------------------------------------- ---------------------------------------------- 12/11/2011 "Rogério Peixoto" <rcbpeixoto@gmail.com> Initial Draft ---------- -------------------------------------------- ----------------------------------------------
 */
public class AccountManager {

	/**
	 * @uml.property  name="instance"
	 * @uml.associationEnd  
	 */
	private static AccountManager instance;

	private Context context;
	/**
	 * @uml.property  name="user"
	 * @uml.associationEnd  
	 */
	private User user;
	/**
	 * @uml.property  name="userRepository"
	 * @uml.associationEnd  
	 */
	private UserRepository userRepository;
	/**
	 * @uml.property  name="firstAccess"
	 */
	private boolean firstAccess;

	private AccountManager(Context context) {
		this.context = context;
		userRepository = UserRepository.getInstance(context);
		try {
			user = userRepository.getUser();
		} catch (UserNotFoundException e) {
			firstAccess = true;
		}
	}

	public static AccountManager getInstance(Context context) {
		if (instance == null) {
			instance = new AccountManager(context);
		}
		return instance;
	}
	
	public void reset() {
		userRepository.deleteAll();
	}

	/**
	 * @return
	 * @uml.property  name="firstAccess"
	 */
	public boolean isFirstAccess() {
		return this.firstAccess;
	}

	public void registerAccount(String email, String password)
			throws InvalidEmailException, InvalidPasswordException {
		if (!email.contains("@") || email.equals(""))
			throw new InvalidEmailException();
		if (password.equals(""))
			throw new InvalidPasswordException();
		else {
			User newUser = new User(email, password);
			userRepository.insert(newUser);
			firstAccess = false;
		}
	}

	public boolean login(String password) throws InvalidPasswordException,
			UserNotFoundException {
		if (password.equals(userRepository.getUser().getPassword().toString())) {
			return true;
		} else {
			throw new InvalidPasswordException();
		}
	}
}
