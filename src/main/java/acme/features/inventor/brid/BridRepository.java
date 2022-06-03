package acme.features.inventor.brid;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.artifact.Artifact;
import acme.entities.brid.Brid;
import acme.framework.repositories.AbstractRepository;
import acme.systemSetting.SystemSetting;

@Repository
public interface BridRepository extends AbstractRepository{
	
	@Query("select c from Brid c where c.id = :id")
	Brid findOneBridById(int id);

	@Query("select c from Brid c")
	Collection<Brid> findManyBrid();
	
	@Query("select a from Artifact a LEFT JOIN Brid c ON c.artefact=a WHERE c IS NULL")
	List<Artifact> findArtifactList();
	
	@Query("select a from Artifact a where a.id = :id")
	Artifact findArtifactById(int id);
	
	@Query("select c from Brid c where c.code = :code")
	Brid findAnyBridByCode(String code);
	
	@Query("select s from SystemSetting s")
	SystemSetting findSystemSetting();
	
}
