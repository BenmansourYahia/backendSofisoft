package ma.sofisoft.cam2020ws.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ma.sofisoft.cam2020ws.entity.DspMagasin;
import ma.sofisoft.cam2020ws.entity.Parametres;

public interface ParametresDAO extends JpaRepository<Parametres,String> {

	@Query("select p from Parametres p where p.id.module = ?1")
	List<Parametres> findByModule(String module);
}
