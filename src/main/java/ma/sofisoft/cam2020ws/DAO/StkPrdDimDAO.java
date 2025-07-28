package ma.sofisoft.cam2020ws.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ma.sofisoft.cam2020ws.entity.StkPrdDim;

public interface StkPrdDimDAO extends JpaRepository<StkPrdDim, Long> {
	
	@Query("select dim from StkPrdDim dim where dim.stkProduitsGenerique.numProduit = ?1  ")
	List<StkPrdDim> findByNumProduit(int numProduit);
	
	StkPrdDim findByCodeBarre(String codeBarre);
	
	StkPrdDim findByCodeProduit(String codeProduit);
}
