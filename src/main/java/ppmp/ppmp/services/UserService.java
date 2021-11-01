package ppmp.ppmp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ppmp.ppmp.domain.User;
import ppmp.ppmp.exceptions.UsernameAlreadyExist;
import ppmp.ppmp.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;

	public User saveUser(User newUser) {
		try {
			newUser.setPassword(bcryptPasswordEncoder.encode(newUser.getPassword()));

			// username has to be unique

			// password and confirm password is same
			newUser.setConfirmPassword("");
			return userRepository.save(newUser);
		} catch (Exception e) {
			throw new UsernameAlreadyExist("username " + newUser.getUsername() + " already exist.");
		}

	}

}
