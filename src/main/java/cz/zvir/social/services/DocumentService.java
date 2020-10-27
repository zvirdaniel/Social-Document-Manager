package cz.zvir.social.services;

import cz.zvir.social.exceptions.SocialException;
import cz.zvir.social.models.Document;
import cz.zvir.social.models.Like;
import cz.zvir.social.repositories.DocumentRepository;
import cz.zvir.social.requests.DocumentRequest;
import cz.zvir.social.specifications.DocumentSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {
	private final DocumentRepository repository;
	private final UserService userService;

	public Document get(final String id) {
		return repository.findById(id).orElseThrow(() -> new SocialException("Document does not exist!"));
	}

	public List<Document> getAll() {
		return repository.findAll();
	}

	public List<Document> getLikedDocuments(final long userId) {
		return repository.findAll(DocumentSpecification.likedByUser(userId));
	}

	public Document create(final String id, final DocumentRequest request) {
		if (repository.existsById(id)) {
			throw new SocialException("Document with this ID already exists!");
		}
		Document document = new Document();
		document.setId(id);
		document.setContent(request.getContent());
		document.setUser(userService.get(request.getUser()));
		return repository.save(document);
	}

	public Document update(final String id, final DocumentRequest request) {
		Document document = this.get(id);
		document.setContent(request.getContent());
		document.setUser(userService.get(request.getUser()));
		return repository.save(document);
	}

	@Transactional
	public void delete(final String id) {
		repository.deleteById(id);
	}

	public Document like(final String documentId, final long userId) {
		Document document = this.get(documentId);
		document.getLikes().add(new Like(document, userService.get(userId)));
		return repository.save(document);
	}

	public Document unlike(final String documentId, final long userId) {
		Document document = this.get(documentId);
		document.getLikes().removeIf(like -> userId == like.getUser().getId());
		return repository.save(document);
	}
}
