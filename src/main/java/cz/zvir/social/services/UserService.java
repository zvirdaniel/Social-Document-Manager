package cz.zvir.social.services;

import cz.zvir.social.models.User;
import cz.zvir.social.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository repository;

	public User get(final long id) {
		return repository.findById(id).orElseThrow();
	}

	public List<User> getAll() {
		return repository.findAll();
	}

	public User create(final String name) {
		User user = new User();
		user.setName(name);
		return repository.save(user);
	}

	public User update(final long id, final String name) {
		User user = this.get(id);
		user.setName(name);
		return repository.save(user);
	}

	@Transactional
	public void delete(final long id) {
		repository.deleteById(id);
	}
}
