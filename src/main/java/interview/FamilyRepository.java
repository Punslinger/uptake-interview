package interview;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "families", path = "families")
public interface FamilyRepository extends PagingAndSortingRepository<Family, Long>
{
	Optional<Family> findByName(@Param("name") String name);
}