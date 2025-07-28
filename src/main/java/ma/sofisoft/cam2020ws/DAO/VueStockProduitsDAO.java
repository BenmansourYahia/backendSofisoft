package ma.sofisoft.cam2020ws.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ma.sofisoft.cam2020ws.entity.VueStockProduits;


public interface VueStockProduitsDAO  {
	//extends JpaRepository<VueStockProduits, Integer>	
	/*
	public final static int TAG_STOCK_DIFF_ZERO = 1;
    public final static int TAG_STOCK_AVEC_ZERO = 2;
    public final static int TAG_STOCK_EQA_ZERO = 3;
    public final static int TAG_STOCK_INF_ZERO = 4;
    public final static int TAG_STOCK_SUP_ZERO = 5;
    public final static int TAG_STOCK_FORMULA_ZERO = 6;
    public final static int TAG_STOCK_TOUS_PRODUITS = 7;
    */
	
	@Query("SELECT vue FROM VueStockProduits vue where vue.quantite !=0 and vue.row between ?1 and ?2")
	List<VueStockProduits> getStockDiffZero(int from,int to);
	
	@Query("SELECT vue FROM VueStockProduits vue where vue.row between ?1 and ?2")
	List<VueStockProduits> getStockAvecZero(int from,int to);
	
	@Query("SELECT vue FROM VueStockProduits vue where vue.quantite = 0 and vue.row between ?1 and ?2")
	List<VueStockProduits> getStockEqalZero(int from,int to);
	
	@Query("SELECT vue FROM VueStockProduits vue where vue.quantite < 0 and vue.row between ?1 and ?2")
	List<VueStockProduits> getStockInfZero(int from,int to);
	
	@Query("SELECT vue FROM VueStockProduits vue where vue.quantite > 0 and vue.row between ?1 and ?2")
	List<VueStockProduits> getStockSupZero(int from,int to);
}
