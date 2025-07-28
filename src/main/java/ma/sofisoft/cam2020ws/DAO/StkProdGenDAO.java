package ma.sofisoft.cam2020ws.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ma.sofisoft.cam2020ws.entity.StkProduitsGenerique;

public interface StkProdGenDAO extends JpaRepository<StkProduitsGenerique, Integer> {

	//@Query("select gen from StkProduitsGenerique gen where gen.codeProduit = ?1")
	StkProduitsGenerique findByCodeProduit(String codeProduit);
	
	//@Query("select gen from StkProduitsGenerique gen where gen.numProduit = ?1")
	StkProduitsGenerique findByNumProduit(int numProduit);
}
