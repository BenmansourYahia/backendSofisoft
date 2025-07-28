package ma.sofisoft.cam2020ws.DAO;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ma.sofisoft.cam2020ws.entity.VuePrdsId;
import ma.sofisoft.cam2020ws.entity.VuePrdsVendusParDate;

public interface VuePrdsVendusParDateDAO extends JpaRepository<VuePrdsVendusParDate, VuePrdsId> {

	@Query("select new VuePrdsVendusParDate(vue.numProduit, vue.codeProduitGen,vue.designation,sum(vue.quantite),sum(vue.total),vue.prixVente)  "
			+ "from VuePrdsVendusParDate vue "
			+ "where vue.date between ?1 and ?2 and vue.numMagasin =?3 "
			+ "group by vue.numProduit,vue.codeProduitGen,vue.designation,vue.prixVente")
	List<VuePrdsVendusParDate> getByDateAndMagasin(Date debut,Date fin,int numMagasin);
	
	@Query("select new VuePrdsVendusParDate(vue.numProduit,vue.codeProduitGen,vue.codeProduitDims,vue.designation,sum(vue.quantite),sum(vue.total),vue.prixVente,vue.libelleDim1,vue.libelleDim2)  "
			+ "from VuePrdsVendusParDate vue "
			+ "where vue.date between ?1 and ?2 and vue.numMagasin =?3 "
			+ "group by vue.numProduit,vue.codeProduitGen,vue.codeProduitDims,vue.designation,vue.prixVente,vue.libelleDim1,vue.libelleDim2 "
			+ "order by vue.numProduit,vue.prixVente")
	List<VuePrdsVendusParDate> getPrdsWithDimsByDateAndMagasin(Date debut, Date fin, int numMagasin);
	
	@Query("select new VuePrdsVendusParDate(vue.numProduit,vue.codeProduitGen,vue.codeProduitDims,vue.designation,sum(vue.quantite),sum(vue.total),vue.prixVente,vue.libelleDim1,vue.libelleDim2)  "
			+ "from VuePrdsVendusParDate vue "
			+ "where vue.date between ?1 and ?2 and vue.numMagasin =?3 and vue.numProduit = ?4 "
			+ "group by vue.numProduit,vue.codeProduitGen,vue.codeProduitDims,vue.designation,vue.prixVente,vue.libelleDim1,vue.libelleDim2")
	List<VuePrdsVendusParDate> getByDateAndMagasinAndNumProduit(Date debut, Date fin, int numMagasin, long numProduit);
}
