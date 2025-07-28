package ma.sofisoft.cam2020ws.DAO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import ma.sofisoft.cam2020ws.entity.LineVenteDirect;
import ma.sofisoft.cam2020ws.entity.LineVenteDirectId;
import ma.sofisoft.cam2020ws.entity.StkProduitsGenerique;
import ma.sofisoft.cam2020ws.entity.StkVenteDirecte;

public interface LineVenteDirectDAO extends JpaRepository<LineVenteDirect, LineVenteDirectId> {
	
	/*@Query("select * from LineVenteDirect ,STK_VENTE_DIRECTE sv" + 
			"where sv.DATE_MVT between '2019-01-07 00:00:00.00' and '2019-02-07 00:00:00.00'\r\n" + 
			"and sv.NUM_MVT <> '0' and sv.NUM_MAGASIN = 1 ")
	List<LineVenteDirect> getProductsByDateAndMagasin(Date debut,Date fin,Long numMagasin);*/
	
	@Query("select new LineVenteDirect(lv.id,lv.numProduit,lv.numMvt, lv.quantite, lv.tauxRemise, lv.prixVente) from LineVenteDirect lv where lv.numMvt = ?1 and lv.id.numMagasin = ?2")
	List<LineVenteDirect> getByNUmMvtAndNumMagasin(int numMvt,int numMagasin);
	
	List<LineVenteDirect> findByNumMvt(int numMvt);
}
