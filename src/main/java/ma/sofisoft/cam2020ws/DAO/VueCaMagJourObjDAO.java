package ma.sofisoft.cam2020ws.DAO;

import java.util.Date;
import java.util.List;

import ma.sofisoft.cam2020ws.model.DashboardModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ma.sofisoft.cam2020ws.entity.VueCaMagJourObj;
import ma.sofisoft.cam2020ws.model.MagasinModel;
import org.springframework.data.repository.query.Param;

public interface VueCaMagJourObjDAO extends JpaRepository<VueCaMagJourObj, String> {

	@Query("select new VueCaMagJourObj(vue.numMagasin,"
			+ " vue.codeMagasin, "
			+ "vue.nomMagasin, "
			+ "SUM(vue.montantTTC), "
			+ "SUM(vue.quantite), "
			+ "SUM(vue.nombreTickets), "
			+ "SUM(isnull(vue.compteur,0)), "
			+ "SUM(vue.objectif)) "
			+ "from VueCaMagJourObj vue "
			+ "where vue.jourVente between ?1 and ?2 "
			+ "group by vue.codeMagasin, vue.nomMagasin,vue.numMagasin")
	List<VueCaMagJourObj> getInfosByDateGroupedByMagasin(Date debut,Date fin);

	@Query("select new VueCaMagJourObj(vue.numMagasin,"
			+ "vue.codeMagasin, "
			+ "vue.nomMagasin, "
			+ "SUM(vue.montantTTC), "
			+ "SUM(vue.quantite), "
			+ "SUM(vue.nombreTickets), "
			+ "SUM(isnull(vue.compteur,0)), "
			+ "SUM(vue.objectif)) "
			+ "from VueCaMagJourObj vue "
			+ "where vue.jourVente between ?1 and ?2 and vue.codeMagasin = ?3 "
			+ "group by vue.codeMagasin, vue.nomMagasin,vue.numMagasin")
	VueCaMagJourObj getInfosByDateAndMagasin(Date debut,Date fin,String codeMagasin);

	@Query("select new VueCaMagJourObj(vue.numMagasin,"
			+ "vue.codeMagasin, "
			+ "vue.nomMagasin, "
			+ "SUM(vue.montantTTC), "
			+ "SUM(vue.quantite), "
			+ "SUM(vue.nombreTickets), "
			+ "SUM(isnull(vue.compteur,0)), "
			+ "SUM(vue.objectif)) "
			+ "from VueCaMagJourObj vue "
			+ "group by vue.codeMagasin, vue.nomMagasin,vue.numMagasin")
	List<VueCaMagJourObj> getMagasinsInfos();

	@Query("select new VueCaMagJourObj(vue.jourVente,"
			+ "isnull(SUM(vue.montantTTC),0),"
			+ "isnull(SUM(vue.quantite),0),"
			+ "isnull(SUM(vue.nombreTickets),0),"
			+ "isnull(SUM(vue.objectif),0),"
			+ "isnull(SUM(vue.compteur),0)) "
			+ "from VueCaMagJourObj vue "
			+ "where jourVente between ?1 and ?2 "
			+ "group by vue.jourVente "
			+ "order by vue.jourVente")
	List<VueCaMagJourObj> getInfosByDateGroupedByVenteDay(Date debut, Date fin);

	@Query("select new VueCaMagJourObj(isnull(SUM(vue.montantTTC),0),"
			+ "isnull(SUM(vue.quantite),0),"
			+ "isnull(SUM(vue.nombreTickets),0),"
			+ "isnull(SUM(vue.objectif),0),"
			+ "isnull(SUM(vue.compteur),0)) "
			+ "from VueCaMagJourObj vue "
			+ "where jourVente between ?1 and ?2 ")
	VueCaMagJourObj getInfosByDateNotGrouped(Date debut, Date fin);

	@Query("select new ma.sofisoft.cam2020ws.model.DashboardModel(" +
			"vue.codeMagasin, " +
			"vue.nomMagasin, " +
			"SUM(vue.montantTTC), " +
			"SUM(vue.quantite), " +
			"SUM(vue.nombreTickets), " +
			"SUM(vue.montantTTC)/SUM(vue.quantite), " +
			"SUM(vue.montantTTC)*100 / sum(vue.montantTTC), " +
			"SUM(vue.quantite)/SUM(vue.nombreTickets), " +
			"SUM(vue.montantTTC)/SUM(vue.nombreTickets)) " +
			"from VueCaMagJourObj vue " +
			"group by vue.codeMagasin, vue.nomMagasin")
	List<DashboardModel> getStatsParMagasin();

	@Query("select new ma.sofisoft.cam2020ws.model.DashboardModel(" +
			"vue.codeMagasin, " +
			"vue.nomMagasin, " +
			"SUM(vue.montantTTC), " +
			"SUM(vue.quantite), " +
			"SUM(vue.nombreTickets), " +
			"SUM(vue.montantTTC)/SUM(vue.quantite), " +
			"SUM(vue.montantTTC)*100 / sum(vue.montantTTC), " +
			"SUM(vue.quantite)/SUM(vue.nombreTickets), " +
			"SUM(vue.montantTTC)/SUM(vue.nombreTickets)) " +
			"from VueCaMagJourObj vue " +
			"where vue.codeMagasin = :codeMagasin " +
			"group by vue.codeMagasin, vue.nomMagasin")
	List<DashboardModel> getStatsParMagasinAndCode(@Param("codeMagasin") String codeMagasin);


	@Query("select new VueCaMagJourObj(vue.jourVente, "
			+ "isnull(SUM(vue.montantTTC),0),"
			+ "isnull(SUM(vue.quantite),0),"
			+ "isnull(SUM(vue.nombreTickets),0),"
			+ "isnull(SUM(vue.objectif),0),"
			+ "isnull(SUM(vue.compteur),0)) "
			+ "from VueCaMagJourObj vue "
			+ "where vue.jourVente between ?1 and ?2 and vue.codeMagasin = ?3 "
			+ "group by vue.jourVente "
			+ "order by vue.jourVente")
	List<VueCaMagJourObj> getInfosByDateGroupedByVenteDayAndMagasin(Date debut, Date fin, String codeMagasin);


	@Query("select new ma.sofisoft.cam2020ws.model.DashboardModel(" +
			"vue.codeMagasin, " +
			"vue.nomMagasin, " +
			"SUM(vue.montantTTC), " +
			"SUM(vue.quantite), " +
			"SUM(vue.nombreTickets), " +
			"SUM(vue.montantTTC)/NULLIF(SUM(vue.quantite),0), " +
			"SUM(vue.montantTTC)*100 / NULLIF(sum(vue.montantTTC),0), " +
			"SUM(vue.quantite)/NULLIF(SUM(vue.nombreTickets),0), " +
			"SUM(vue.montantTTC)/NULLIF(SUM(vue.nombreTickets),0)) " +
			"from VueCaMagJourObj vue " +
			"where vue.codeMagasin in :codesMagasins " +
			"group by vue.codeMagasin, vue.nomMagasin")
	List<DashboardModel> getStatsParMagasins2(@Param("codesMagasins") List<String> codesMagasins);

}