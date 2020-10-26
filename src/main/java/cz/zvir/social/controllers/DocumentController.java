package cz.zvir.social.controllers;

import cz.zvir.social.requests.DocumentRequest;
import cz.zvir.social.responses.DocumentResponse;
import cz.zvir.social.services.DocumentService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentController {
	private final DocumentService service;

	@GetMapping
	@ApiOperation("Returns all available documents.")
	public List<DocumentResponse> getAll() {
		return service.getAll().stream().map(DocumentResponse::new).collect(Collectors.toList());
	}

	@GetMapping("/id/{id}")
	@ApiOperation("Returns single document by document ID.")
	public DocumentResponse getById(@PathVariable final String id) {
		return new DocumentResponse(service.get(id));
	}

	@GetMapping("/liked-documents")
	@ApiOperation("Returns all liked documents for given user ID.")
	public List<DocumentResponse> getLikedDocuments(@RequestParam final long userId) {
		return service.getLikedDocuments(userId).stream().map(DocumentResponse::new).collect(Collectors.toList());
	}

	@PostMapping("/id/{id}")
	@ApiOperation("Creates new document.")
	public DocumentResponse create(@PathVariable final String id, @RequestBody @Validated final DocumentRequest request) {
		return new DocumentResponse(service.create(id, request));
	}

	@PatchMapping("/id/{id}")
	@ApiOperation("Updates document with given document ID.")
	public DocumentResponse update(@PathVariable final String id, @RequestBody @Validated final DocumentRequest request) {
		return new DocumentResponse(service.update(id, request));
	}

	@DeleteMapping("/id/{id}")
	@ApiOperation("Deletes document with given document ID.")
	public void delete(@PathVariable final String id) {
		service.delete(id);
	}

	@PatchMapping("/like/{id}")
	@ApiOperation("Likes document with given document ID for user with given user ID.")
	public DocumentResponse like(@PathVariable(name = "id") final String documentId, @RequestParam final long userId) {
		return new DocumentResponse(service.like(documentId, userId));
	}

	@PatchMapping("/unlike/{id}")
	@ApiOperation("Removes all likes from document with given document ID for user with given user ID.")
	public DocumentResponse unlike(@PathVariable(name = "id") final String documentId, @RequestParam final long userId) {
		return new DocumentResponse(service.unlike(documentId, userId));
	}
}
