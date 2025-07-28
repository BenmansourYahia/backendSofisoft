package ma.sofisoft.cam2020ws.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.sofisoft.cam2020ws.entity.CfgUtilisateur;
import ma.sofisoft.cam2020ws.entity.CfgUtilisateurId;

public interface CfgUtilisateurDAO extends JpaRepository<CfgUtilisateur, CfgUtilisateurId>{

	CfgUtilisateur findByNomAndMotPasse(String nom,String motPasse);
}
