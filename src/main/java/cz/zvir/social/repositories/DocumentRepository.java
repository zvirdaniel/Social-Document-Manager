package cz.zvir.social.repositories;

import cz.zvir.social.models.Document;
import cz.zvir.social.repositories.base.CommonRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends CommonRepository<Document, String> {
}
