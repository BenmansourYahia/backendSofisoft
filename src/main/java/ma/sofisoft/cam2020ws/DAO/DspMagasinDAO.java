package ma.sofisoft.cam2020ws.DAO;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ma.sofisoft.cam2020ws.entity.DspMagasin;
import ma.sofisoft.cam2020ws.entity.VueCaMagJourObj;
import ma.sofisoft.cam2020ws.model.MagasinModel;

public interface DspMagasinDAO extends JpaRepository<DspMagasin, Long> {

	@Query("select new DspMagasin(mag.numMagasin,mag.codeMagasin,mag.nomMagasin) from DspMagasin mag where ISNULL(mag.entrepot,0) = 0")
	List<DspMagasin> getMagasins();
}
