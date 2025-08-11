package ma.sofisoft.cam2020ws;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ma.sofisoft.cam2020ws.model.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.reflect.TypeToken;

import ma.sofisoft.cam2020ws.DAO.CfgUtilisateurDAO;
import ma.sofisoft.cam2020ws.DAO.DspMagasinDAO;
import ma.sofisoft.cam2020ws.DAO.LineVenteDirectDAO;
import ma.sofisoft.cam2020ws.DAO.ParametresDAO;
import ma.sofisoft.cam2020ws.DAO.StkPrdDimDAO;
import ma.sofisoft.cam2020ws.DAO.StkProdGenDAO;
import ma.sofisoft.cam2020ws.DAO.StkVenteDirectDAO;
import ma.sofisoft.cam2020ws.DAO.VueCaMagJourObjDAO;
import ma.sofisoft.cam2020ws.DAO.VuePrdsVendusParDateDAO;
import ma.sofisoft.cam2020ws.entity.CfgUtilisateur;
import ma.sofisoft.cam2020ws.entity.DspMagasin;
import ma.sofisoft.cam2020ws.entity.LineVenteDirect;
import ma.sofisoft.cam2020ws.entity.Parametres;
import ma.sofisoft.cam2020ws.entity.StkPrdDim;
import ma.sofisoft.cam2020ws.entity.StkProduitsGenerique;
import ma.sofisoft.cam2020ws.entity.StkVenteDirecte;
import ma.sofisoft.cam2020ws.entity.VueCaMagJourObj;
import ma.sofisoft.cam2020ws.entity.VuePrdsVendusParDate;
import ma.sofisoft.cam2020ws.entity.VueStockProduits;
import ma.sofisoft.cam2020ws.helper.LineVenteAdapter;

@RestController
@RequestMapping(value = "/*")
public class CamController {
	
	Logger logger = LoggerFactory.getLogger(CamController.class);
	public final static int TAG_STOCK_DIFF_ZERO = 1;
    public final static int TAG_STOCK_AVEC_ZERO = 2;
    public final static int TAG_STOCK_EQAL_ZERO = 3;
    public final static int TAG_STOCK_INF_ZERO = 4;
    public final static int TAG_STOCK_SUP_ZERO = 5;
    public final static int TAG_STOCK_FORMULA_ZERO = 6;
    public final static int TAG_STOCK_TOUS_PRODUITS = 7;
	
	@Autowired
	StkVenteDirectDAO venteDao;
	
	//@Autowired
	//VueStockProduitsDAO vueStockDao;
	
	@Autowired
	LineVenteDirectDAO lineVenteDao;
	
	@Autowired
	StkProdGenDAO genDao;
	
	@Autowired
	StkPrdDimDAO dimDao;
	
	@Autowired
	ParametresDAO paramDao;
	
	@Autowired
	DspMagasinDAO magasinDao;
	
	@Autowired
	VueCaMagJourObjDAO vueCaMagJourDAO;
	
	@Autowired
	VuePrdsVendusParDateDAO vuePrdsDao;
	
	@Autowired
	CfgUtilisateurDAO cfgUserDao;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	private VueCaMagJourObjDAO vueCaMagJourObjDAO;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	@RequestMapping(value = "/")
	public String racine() {
		return "welcome to cam web Service ^_^ ";
	}
	
	public Gson getGson() {
		String DATE_FORMAT = "MM-dd-yyyy'T'HH:mm:ssZ";
		return new GsonBuilder()
             .setDateFormat(DATE_FORMAT)
             .enableComplexMapKeySerialization()
             .create();
	}
	
	@RequestMapping(value = "/bestSalesPrds",method = RequestMethod.POST)
	public MyResponse bestSales(@RequestBody HashMap<String, Object> map){
		MyResponse myResponse = new MyResponse();
		List<BestSalePrdModel> listBestSale = new ArrayList<>();
	    try {
	    	int numMagasin = (Integer) map.get("numMagasin");
	    	Date debut = sdf.parse((String) map.get("debut"));
	    	Date fin = sdf.parse((String) map.get("fin"));
	    	logger.info("map : "+map);
	    	List<LineVenteDirect> listLineVente = new ArrayList<>();
			List<StkVenteDirecte> listVente = venteDao.getByDateMvtAndNumMagasin(debut,fin,Long.valueOf(numMagasin));
			logger.info("listVente : "+listVente.size());
			for(StkVenteDirecte vente:listVente) {
				listLineVente.addAll(lineVenteDao.getByNUmMvtAndNumMagasin(vente.getId().getNumMvt(), (int) vente.getId().getNumMagasin()));
			}
			logger.info("listLineVente : "+listLineVente.size());
			for(LineVenteDirect lineVente:listLineVente) {
				if(listBestSale.size()==0) {
					BestSalePrdModel bestSale = new BestSalePrdModel();
					bestSale.setNumProduit(lineVente.getNumProduit());
					bestSale.setQuantite(lineVente.getQuantite().intValue());
					bestSale.setPrixVente(lineVente.getPrixVente().doubleValue());
					
					StkProduitsGenerique gen = genDao.findByNumProduit(bestSale.getNumProduit());
					bestSale.setCodeProduit(gen.getCodeProduit());
					bestSale.setDesignation(gen.getDesignation());
					listBestSale.add(bestSale);
				}
				else {
					boolean isExist = false;
					for(BestSalePrdModel bestSale:listBestSale) {
						if(bestSale.getNumProduit() == lineVente.getNumProduit()) {
							isExist = true;
							break;
						}
					}
					if(!isExist) {
						BestSalePrdModel bestSale = new BestSalePrdModel();
						bestSale.setNumProduit(lineVente.getNumProduit());
						bestSale.setQuantite(lineVente.getQuantite().intValue());
						bestSale.setPrixVente(lineVente.getPrixVente().doubleValue());
						
						StkProduitsGenerique gen = genDao.findByNumProduit(bestSale.getNumProduit());
						bestSale.setCodeProduit(gen.getCodeProduit());
						bestSale.setDesignation(gen.getDesignation());
						listBestSale.add(bestSale);
					}else {
						for(BestSalePrdModel bestSale:listBestSale) {
							if(bestSale.getNumProduit() == lineVente.getNumProduit()) {
								bestSale.setQuantite(bestSale.getQuantite()+lineVente.getQuantite().intValue());
							}
						}
					}
				}
			}
			Type typeOf = new TypeToken<List<BestSalePrdModel>>() {}.getType();
			String data = getGson().toJson(listBestSale,typeOf);
			myResponse.setData(data);
			myResponse.setSuccess(true);
		} catch (Exception e) {		
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			myResponse.setSuccess(false);
			myResponse.setException(e);
			myResponse.setMessage(e.getMessage());
		}
		
		return myResponse;
	}
	
	@RequestMapping(value = "/getLineVentes",method = RequestMethod.POST)
	public List<LineVenteDirect> Linevente(@RequestBody HashMap<String, Object> map){
	    try {
	    	int numMvt = (Integer) map.get("numMvt");
	    	int numMagasin = (Integer) map.get("numMagasin");
	    	logger.info("numMvt : "+numMvt+" numMagasin : "+numMagasin);
	    
			return lineVenteDao.getByNUmMvtAndNumMagasin(numMvt,numMagasin);
		} catch (Exception e) {		
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		
		return null;
	}
	
	@RequestMapping(value = "/getLineVentes2",method = RequestMethod.GET)
	public ResponseEntity<?> getLinesvente(@RequestBody HashMap<String, Object> map){
		List<StkVenteDirecte>  list = null;
	    try {
	    	int numMvt = 1;
	    	int numMagasin = 2;
	    	logger.info("numMvt : "+numMvt+" numMagasin : "+numMagasin);
	    	list = venteDao.findByNumMvtAndNumMagasin(numMvt, numMagasin);
	    	return new ResponseEntity<List<StkVenteDirecte>>(list, HttpStatus.OK); 
			//return lineVenteDao.getByNUmMvtAndNumMagasin(numMvt,numMagasin);
		} catch (Exception e) {		
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			return new ResponseEntity<List<StkVenteDirecte>>(list, HttpStatus.INTERNAL_SERVER_ERROR); 
			
		}
	}
	
	@RequestMapping(value = "/getPrdsVendus",method = RequestMethod.POST)
	public MyResponse getPrdsVendus(@RequestBody HashMap<String, Object> map){
		MyResponse myResponse = new MyResponse();
	    try {
	    	logger.info("map : "+map);

	    	int numMagasin = (Integer) map.get("numMagasin");
	    	Date debut = sdf.parse((String) map.get("debut"));
	    	Date fin = sdf.parse((String) map.get("fin"));
	    	List<VuePrdsVendusParDate> list = vuePrdsDao.getPrdsWithDimsByDateAndMagasin(debut, fin, numMagasin);
	    	Type typeOf = new TypeToken<List<VuePrdsVendusParDate>>() {}.getType();
			String data = getGson().toJson(list,typeOf);
			myResponse.setData(data);
			myResponse.setSuccess(true);
	    	//return new ResponseEntity<List<VuePrdsVendusParDate>>(list, HttpStatus.OK); 
		}catch (Exception e) {		
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			myResponse.setSuccess(false);
			myResponse.setException(e);
			myResponse.setMessage(e.getMessage());
			//return new ResponseEntity<List<VuePrdsVendusParDate>>(list, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return myResponse;
	}	
	
	@RequestMapping(value = "/getDimsPrdVendus",method = RequestMethod.POST)
	public MyResponse getDimsPrdVendus(@RequestBody HashMap<String, Object> map){
		MyResponse myResponse = new MyResponse();
	    try {
	    	logger.info("map : "+map);

	    	int numMagasin = (Integer) map.get("numMagasin");
	    	int numProduit = (Integer) map.get("numProduit");
	    	Date debut = sdf.parse((String) map.get("debut"));
	    	Date fin = sdf.parse((String) map.get("fin"));
	    	logger.info("numProduit : "+numProduit);
	    	List<VuePrdsVendusParDate> list = vuePrdsDao.getByDateAndMagasinAndNumProduit(debut, fin, numMagasin, Long.valueOf(numProduit));
	    	Type typeOf = new TypeToken<List<VuePrdsVendusParDate>>() {}.getType();
			String data = getGson().toJson(list,typeOf);
			myResponse.setData(data);
			myResponse.setSuccess(true);
	    	//return new ResponseEntity<List<VuePrdsVendusParDate>>(list, HttpStatus.OK); 
		}catch (Exception e) {		
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			myResponse.setSuccess(false);
			myResponse.setException(e);
			myResponse.setMessage(e.getMessage());
			//return new ResponseEntity<List<VuePrdsVendusParDate>>(list, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return myResponse;
	}	
	
	@RequestMapping(value = "/getParam",method = RequestMethod.POST)
	public MyResponse param() {
		MyResponse response = new MyResponse();
		response.setDataObject(paramDao.findByModule("ANDROID_UPDATE_APP"));
		return response;
	}
	
	@RequestMapping(value = "/getMagasins",method = RequestMethod.POST)
	public MyResponse Magasins() {
	    MyResponse response = new MyResponse();
		try {
			Gson gson = new Gson();
			Type listType = new TypeToken<List<DspMagasin>>() {}.getType();
			String data = gson.toJson(magasinDao.findAll(), listType);
			response.setData(data);
			response.setSuccess(true);
	    
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			response.setSuccess(false);
			response.setException(e);
		}
		
		return response;
	}
	
	@RequestMapping(value = "/getMagasinsInfoByDate", method = RequestMethod.POST)
	public MyResponse getMagasinsInfoByDate(@RequestBody HashMap map) {
		 MyResponse reponse = new MyResponse();
		 
		 try {
			 logger.info("map : "+map);
			 boolean withDate = (boolean) map.get("withDate");
			 if(withDate) {
				 Date debut = sdf.parse((String) map.get("debut"));
				 Date fin = sdf.parse((String) map.get("fin"));
				 				 				 
				 Type typeOf = new TypeToken<List<VueCaMagJourObj>>() {}.getType();
				 String data = getGson().toJson(vueCaMagJourDAO.getInfosByDateGroupedByMagasin(debut, fin),typeOf);
				 reponse.setData(data);
				 reponse.setSuccess(true);
			 }else {				
				 Type typeOf = new TypeToken<List<VueCaMagJourObj>>() {}.getType();
				 String data = getGson().toJson(vueCaMagJourDAO.getMagasinsInfos(),typeOf);
				 reponse.setData(data);
				 reponse.setSuccess(true);
			 }			
			 			 			 
		 }catch(Exception ex) {
			 ex.printStackTrace();
			 logger.error(ex.getMessage(),ex);
			 reponse.setSuccess(false);
			 reponse.setException(ex);
		 }
		 return reponse;
	}
	
	@RequestMapping(value = "/getMagasinsInfos", method = RequestMethod.POST)
	public List<VueCaMagJourObj> getMagasinsInfos(@RequestBody HashMap map) {
		 logger.info("map : "+map);
		 boolean withDate = (boolean) map.get("withDate");
		if(withDate) {			 
			try {
				Date debut = sdf.parse((String) map.get("debut"));
				Date fin = sdf.parse((String) map.get("fin"));			
				 return vueCaMagJourDAO.getInfosByDateGroupedByMagasin(debut, fin);
			} catch (Exception e) {
				e.printStackTrace();
				return new ArrayList<VueCaMagJourObj>();
			}
		 }
		
		return new ArrayList<VueCaMagJourObj>();
	}
	
	@RequestMapping(value = "/getComparePeriode", method = RequestMethod.POST)
	public MyResponse getComparePeriode(@RequestBody HashMap map) {
		 MyResponse reponse = new MyResponse();
	
		 try {
			 logger.info("map : "+map); 
			 Date dateDebut_1 = sdf.parse((String) map.get("dateDebut_1"));
			 Date dateFin_1 = sdf.parse((String) map.get("dateFin_1"));
			 Date dateDebut_2 = sdf.parse((String) map.get("dateDebut_2"));
			 Date dateFin_2 = sdf.parse((String) map.get("dateFin_2"));
			 String codeMagasin = (String) map.get("codeMagasin");
			 
			 List<VueCaMagJourObj> listVues = new ArrayList<>();
			 listVues.add(vueCaMagJourDAO.getInfosByDateAndMagasin(dateDebut_1, dateFin_1,codeMagasin));
			 listVues.add(vueCaMagJourDAO.getInfosByDateAndMagasin(dateDebut_2, dateFin_2,codeMagasin));
			 logger.info("listVueCompart: "+listVues);
			 Type typeOf = new TypeToken<List<VueCaMagJourObj>>() {}.getType();
			 String data = getGson().toJson(listVues,typeOf);
			 reponse.setData(data);
			 reponse.setSuccess(true);
		 }catch(Exception ex) {
			 ex.printStackTrace();
			 reponse.setSuccess(false);
			 reponse.setException(ex);
			 reponse.setMessage(ex.getMessage());
		 }
		 
		 return reponse;
	}
	
	@RequestMapping(value = "/getInfosByDate", method = RequestMethod.POST)
	public MyResponse getInfosByDate(@RequestBody HashMap map) {
	    MyResponse response = new MyResponse();
		
	    try {
			logger.info("map : "+map);
			Date debut = sdf.parse((String) map.get("debut"));
			Date fin = sdf.parse((String) map.get("fin"));
			logger.info("debut : "+debut+" fin : "+fin);
			
			Calendar cStart = Calendar.getInstance();
            Calendar cEnd = Calendar.getInstance();
            Calendar cal = Calendar.getInstance();
			if(fin.getTime()-debut.getTime()<7*24*3600*1000l) {
				
				cal.setTime(debut);
				cStart.setTime(debut);
	            cStart.add(Calendar.YEAR, -1);
	            cStart.set(Calendar.WEEK_OF_YEAR, cal.get(Calendar.WEEK_OF_YEAR));
				cStart.setFirstDayOfWeek(Calendar.MONDAY);

	            cEnd.setTime(cStart.getTime());
	            cEnd.add(Calendar.DAY_OF_WEEK,6);
	            cEnd.set(Calendar.HOUR_OF_DAY, 23);
	            cEnd.set(Calendar.MINUTE, 59);
	            cEnd.set(Calendar.SECOND, 59);
	            logger.info("is week periode ! "+cal.get(Calendar.WEEK_OF_YEAR));
				logger.info("date debut Prec : "+sdf.format(cStart.getTime())+" date fin Prec : "+sdf.format(cEnd.getTime()));

			}else {
				logger.info("is not a week periode !");
				cStart = Calendar.getInstance();
	            cEnd = Calendar.getInstance();
	            cStart.setTime(debut);
	            cEnd.setTime(fin);
	            cStart.add(Calendar.YEAR, -1);
	            cEnd.add(Calendar.YEAR, -1);
				logger.info("date debut Prec : "+sdf.format(cStart.getTime())+" date fin Prec : "+sdf.format(cEnd.getTime()));
			}
			
			VueCaMagJourObj vueCaCompare = vueCaMagJourDAO.getInfosByDateNotGrouped(cStart.getTime(), cEnd.getTime());
			
			List<VueCaMagJourObj> listVueVentes = vueCaMagJourDAO.getInfosByDateGroupedByVenteDay(debut, fin);
			Type typeOf = new TypeToken<List<VueCaMagJourObj>>() {}.getType();
			String data = getGson().toJson(listVueVentes,typeOf);
			HashMap<String, String> obj = new HashMap<>();
			obj.put("data", data);
			obj.put("objCompare", getGson().toJson(vueCaCompare));
			response.setData(obj.toString());
			response.setSuccess(true);
			
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			response.setSuccess(false);
			response.setException(e);
		}	
		return response;
	}
	
	@RequestMapping(value = "/getInfosDay", method = RequestMethod.POST)
	public MyResponse getInfosDay(@RequestBody HashMap map) {
	    MyResponse response = new MyResponse();
		
	    try {
			logger.info("map : "+map);
			Date debut = sdf.parse((String) map.get("debut"));
			Date fin = sdf.parse((String) map.get("fin"));
			
			List<StkVenteDirecte> listVentes = venteDao.getVentesByDate(debut, fin);
			List<VenteModel> listVenteModel = new ArrayList<>();
			for(StkVenteDirecte vente:listVentes) {
				VenteModel v = new VenteModel();
				v.setDateMvt(vente.getDateMvt());
				v.setMontantTtc(vente.getMontantTtc());
				v.setHour(vente.getHour());
				listVenteModel.add(v);
			}
			
			Calendar cStart = Calendar.getInstance();
            Calendar cEnd = Calendar.getInstance();
            Calendar cal = Calendar.getInstance();
			cal.setTime(debut);
			cStart.setTime(debut);
            cStart.add(Calendar.YEAR, -1);
            cStart.set(Calendar.WEEK_OF_YEAR, cal.get(Calendar.WEEK_OF_YEAR));
            cStart.set(Calendar.DAY_OF_WEEK, cal.get(Calendar.DAY_OF_WEEK));

            cEnd.setTime(cStart.getTime());
            cEnd.set(Calendar.HOUR_OF_DAY, 23);
            cEnd.set(Calendar.MINUTE, 59);
            cEnd.set(Calendar.SECOND, 59);
			logger.info("date debut Prec : "+sdf.format(cStart.getTime())+" date fin Prec : "+sdf.format(cEnd.getTime()));
			
			List<VueCaMagJourObj> vueList = vueCaMagJourDAO.getInfosByDateGroupedByMagasin(debut, fin);
			VueCaMagJourObj vueCaCompare = vueCaMagJourDAO.getInfosByDateNotGrouped(cStart.getTime(), cEnd.getTime());
			
			Type typeOf = new TypeToken<List<VenteModel>>() {}.getType();
			Type typeOf2 = new TypeToken<List<VueCaMagJourObj>>() {}.getType();
			String data = getGson().toJson(listVenteModel,typeOf);
			HashMap<String, String> obj = new HashMap<>();
			obj.put("data", data);
			obj.put("objCompare", getGson().toJson(vueCaCompare));
			obj.put("vueInfos",getGson().toJson(vueList,typeOf2));
			response.setData(obj.toString());
			response.setSuccess(true);
			//response.setDataObject(vueCaMagJourDAO.getInfosByDateGroupedByMagasin(debut, fin));
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			response.setSuccess(false);
			response.setException(e);
		}	
		return response;
	}
	
	@RequestMapping(value = "/getDims", method = RequestMethod.POST)
	public StkPrdDim getDims(@RequestBody HashMap mapRequest) {
		String barCode = (String) mapRequest.get("barCode");
		logger.info("barCode : "+barCode);
		return dimDao.findByCodeBarre(barCode);
	}
	
	public HashMap<String, Object> isHadDims(HashMap mapRequest) {
		HashMap<String, Object> map = new HashMap<>();
		StkProduitsGenerique gen = null;
		logger.info("mapRequest : "+mapRequest);
		boolean isByBarcode = (boolean) mapRequest.get("isByBarcode");
		if(isByBarcode) {
			String barCode = (String) mapRequest.get("barecode");
			logger.info("codeProduit : "+barCode);
			StkPrdDim dim = dimDao.findByCodeBarre(barCode);
			if(dim!=null) gen = genDao.findByNumProduit(dim.getNumProduit());
			
		}else {
			String codeProduit = (String) mapRequest.get("codeProduit");
			logger.info("codeProduit : "+codeProduit);
			StkPrdDim dim = dimDao.findByCodeProduit(codeProduit);
			if(dim!=null) gen = genDao.findByNumProduit(dim.getNumProduit());
		}
		
		if(gen == null) {
			logger.info("gen is null");
			map.put("isHadDims",false);
			return map;
		}
		
		List<StkPrdDim> listDims = dimDao.findByNumProduit(gen.getNumProduit());
		if(listDims !=null) logger.info("listDims size : "+listDims.size());
		if(listDims!=null && listDims.size() == 1 && gen.getCodeProduit().equals(listDims.get(0).getCodeProduit())) {
			map.put("isHadDims",false);
		}else {
			map.put("isHadDims", true);
			map.put("listDims", listDims);
			map.put("numProduit", gen.getNumProduit());
		}
		
		map.put("designation", gen.getDesignation());
		return map;
	}
	
	@RequestMapping(value = "/StockByProduct", method = RequestMethod.POST)
	public MyResponse searchProductStock(@RequestBody HashMap map) {
		MyResponse response = new MyResponse();
		
		HashMap<String, Object> isHadDimsMap = isHadDims(map);
		boolean isHadDims = (boolean) isHadDimsMap.get("isHadDims");
		if(isHadDims) {
			int numProduit = (int) isHadDimsMap.get("numProduit");
			String designation = (String) isHadDimsMap.get("designation");
			response = getProductStockForDims(numProduit,designation);
		}else {
			boolean isByBarcode = (boolean) map.get("isByBarcode");
			if(isByBarcode) {
				String barCode = (String) map.get("barecode");
				response = getProductStockByBarcode(barCode);
			}else {
				String codeProduit = (String) map.get("codeProduit");
				String designation = (String) isHadDimsMap.get("designation");
				response = getProductStockForGen(codeProduit,designation);
			}			
		}
		return response;
	}
	
	public MyResponse getProductStockForGen(String codeProduit,String designation) {
		MyResponse response = new MyResponse();
		
		RowMapper<StockModel> ParametreRowMapper = new RowMapper<StockModel>() {
			@Override
			public StockModel mapRow(ResultSet resultSet, int arg1) throws SQLException {
				StockModel obj = new StockModel();
				
				obj.setQuantite(resultSet.getInt("quantite"));
				obj.setMagasin(resultSet.getString("designation"));
				obj.setDesignation(resultSet.getString("nomProduit"));
				return obj;
			}
		};
		String query = "select depot.designation," + 
				"isnull(PRD_DIM_DEPOT.QUANTITE,0) as quantite, " + 
				"stk_produits_generique.designation nomProduit "+
				"from   depot, " + 
				"PRD_DIM_DEPOT," + 
				"stk_prd_dim," + 
				"stk_produits_generique " + 
				"where depot.CODE_DEPOT = PRD_DIM_DEPOT.CODE_DEPOT and " + 
				"stk_produits_generique.num_produit = stk_prd_dim.num_produit and " + 
				"stk_prd_dim.num_dims = prd_dim_depot.num_dims and " + 
				"(STK_PRD_DIM.code_produit = ? or stk_produits_generique.designation = ?)";
		logger.info("query : "+query);
		
		List<StockModel> stockModel = jdbcTemplate.query(query, new Object[] { codeProduit,codeProduit }, ParametreRowMapper);
		Type typeOf = new TypeToken<List<StockModel>>() {}.getType();
		String data = getGson().toJson(stockModel,typeOf);
		HashMap<String, Object> map = new HashMap<>();
		map.put("data", data);
		map.put("isHadDims", false);
		map.put("designation", designation);
		typeOf = new TypeToken<HashMap<String, Object>>() {}.getType();
		response.setData(getGson().toJson(map,typeOf));
		response.setSuccess(true);
		return response;
	}
	
	public MyResponse getProductStockForDims(int numProduit,String designation) {
		MyResponse response = new MyResponse();
		RowMapper<StockModel> ParametreRowMapper = new RowMapper<StockModel>() {
			@Override
			public StockModel mapRow(ResultSet resultSet, int arg1) throws SQLException {
				StockModel obj = new StockModel();
				
				obj.setNumProduit(resultSet.getInt("numProduit"));
				obj.setCodeProduit(resultSet.getString("CODE_PRODUIT"));
				obj.setDims1(resultSet.getString("Couleur"));
				obj.setDims2(resultSet.getString("Taille"));
				obj.setMagasin(resultSet.getString("DESIGNATION"));
				obj.setQuantite(resultSet.getInt("Quantite"));
				obj.setCodeDepot(resultSet.getInt("CODE_DEPOT"));
				obj.setNumDim1(resultSet.getInt("NUM_DIM1"));
				obj.setNumDim2(resultSet.getInt("NUM_DIM2"));
				return obj;
			}
		};
		String query = "select stk_prd_dim.num_produit numProduit, " + 
				"	   code_produit CODE_PRODUIT, " + 
				"	   stk_dim1.NUM_DIM1, " + 
				"	   stk_dim2.NUM_DIM2, " + 
				"	   stk_dim1.libelle Couleur, " + 
				"	   stk_dim2.libelle Taille, " + 
				"	   designation DESIGNATION," + 
				"	    depot.CODE_DEPOT," + 
				"	   isnull(prd_dim_depot.quantite,0) Quantite " + 
				"from   stk_prd_dim, stk_dim1, " + 
				"		stk_dim2, prd_dim_depot, " + 
				"		depot " + 
				"where stk_prd_dim.num_dim1 *= stk_dim1.num_dim1 and" + 
				"		stk_prd_dim.num_dim2 *= stk_dim2.num_dim2 and" + 
				"		stk_prd_dim.num_dims *= prd_dim_depot.num_dims and " + 
				"		depot.code_depot *= prd_dim_depot.code_depot and " + 
				"		stk_prd_dim.num_produit = ? order by 3,4,6";
		logger.info("numProduit : "+numProduit+ " -- query : "+query);
		
		List<StockModel> stockModel = jdbcTemplate.query(query, new Object[] { numProduit }, ParametreRowMapper);
		Type typeOf = new TypeToken<List<StockModel>>() {}.getType();
		String data = getGson().toJson(stockModel,typeOf);
		HashMap<String, Object> map = new HashMap<>();
		map.put("data", data);
		map.put("isHadDims", true);
		map.put("designation", designation);
		typeOf = new TypeToken<HashMap<String, Object>>() {}.getType();
		response.setData(getGson().toJson(map,typeOf));
		response.setSuccess(true);
		return response;
	}
	
	public MyResponse getProductStockByBarcode(String barcode) {
		MyResponse response = new MyResponse();
		RowMapper<StockModel> ParametreRowMapper = new RowMapper<StockModel>() {

			@Override
			public StockModel mapRow(ResultSet resultSet, int arg1) throws SQLException {
				StockModel obj = new StockModel();
				
				obj.setQuantite(resultSet.getInt("quantite"));
				obj.setMagasin(resultSet.getString("designation"));
				obj.setDesignation(resultSet.getString("nomProduit"));
				return obj;
			}
		};	
		//String barcode = (String) map.get("barecode");
		
		String query = "select depot.designation," + 
				"isnull(PRD_DIM_DEPOT.QUANTITE,0) as quantite, " + 
				"stk_produits_generique.designation nomProduit "+
				"from   depot, " + 
				"PRD_DIM_DEPOT," + 
				"stk_prd_dim," + 
				"stk_produits_generique " + 
				"where depot.CODE_DEPOT = PRD_DIM_DEPOT.CODE_DEPOT and " + 
				"stk_produits_generique.num_produit = stk_prd_dim.num_produit and " + 
				"stk_prd_dim.num_dims = prd_dim_depot.num_dims and " + 
				"STK_PRD_DIM.CODE_BARRE = ?";
		logger.info("query : "+query);
		
		List<StockModel> stockModel = jdbcTemplate.query(query, new Object[] { barcode }, ParametreRowMapper);
		Type typeOf = new TypeToken<List<StockModel>>() {}.getType();
		String data = getGson().toJson(stockModel,typeOf);
		HashMap<String, Object> map = new HashMap<>();
		map.put("data", data);
		map.put("isHadDims", false);
		typeOf = new TypeToken<HashMap<String, Object>>() {}.getType();
		response.setData(getGson().toJson(map,typeOf));
		response.setSuccess(true);
		return response;
	}
	
	/*
	@RequestMapping(value = "/GlobalStock2", method = RequestMethod.POST)
	public MyResponse globalStock2(@RequestBody HashMap map) {
		MyResponse response = new MyResponse();
		
		try {
			logger.info("map "+map);
			int from = (int) map.get("from");
			int to = (int) map.get("to");
			int stockBy = (int) map.get("stockBy");
			List<VueStockProduits> stockList = new ArrayList<>();
			
			switch(stockBy) {
			case TAG_STOCK_AVEC_ZERO:
				logger.info("TAG_STOCK_AVEC_ZERO");
				stockList = vueStockDao.getStockAvecZero(from, to);
				break;
			case TAG_STOCK_DIFF_ZERO:
				logger.info("TAG_STOCK_DIFF_ZERO");
				stockList = vueStockDao.getStockDiffZero(from, to);
				break;
			case TAG_STOCK_EQAL_ZERO:
				logger.info("TAG_STOCK_EQA_ZERO");
				stockList = vueStockDao.getStockEqalZero(from, to);
				break;
			case TAG_STOCK_INF_ZERO:
				logger.info("TAG_STOCK_INF_ZERO");
				stockList =vueStockDao.getStockInfZero(from, to);
				break;
			case TAG_STOCK_SUP_ZERO:
				logger.info("TAG_STOCK_SUP_ZERO");
				stockList = vueStockDao.getStockSupZero(from, to);
				break;
			case TAG_STOCK_TOUS_PRODUITS:
				break;
			case TAG_STOCK_FORMULA_ZERO:
				break;
			default:
				logger.info("default");
				stockList = vueStockDao.getStockAvecZero(from, to);
				break;
			}
			
			Type typeOf = new TypeToken<List<VueStockProduits>>() {}.getType();
			String data = getGson().toJson(stockList,typeOf);
			response.setData(data);
			response.setSuccess(true);
			
		}catch(Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage(),ex);
			response.setMessage(ex.getMessage());
			response.setException(ex);
			response.setSuccess(false);
		}

		return response;
	}
	*/
	
	@RequestMapping(value = "/GlobalStock", method = RequestMethod.POST)
	public MyResponse globalStock(@RequestBody HashMap map) {
		
		RowMapper<StockModel> ParametreRowMapper = new RowMapper<StockModel>() {

			@Override
			public StockModel mapRow(ResultSet resultSet, int arg1) throws SQLException {
				StockModel obj = new StockModel();			
				obj.setQuantite(resultSet.getInt("quantite"));
				obj.setDesignation(resultSet.getString("DESIGNATION"));
				obj.setCodeProduit(resultSet.getString("CODE_PRODUIT"));				
				return obj;
			}
		};
		
		MyResponse response = new MyResponse();
		logger.info("map "+map);
		int from = (int) map.get("from");
		int to = (int) map.get("to");
		int stockBy = (int) map.get("stockBy");
		String chaine = "";
		switch(stockBy) {
		case TAG_STOCK_AVEC_ZERO:
			logger.info("TAG_STOCK_AVEC_ZERO");
			chaine = "";
			break;
		case TAG_STOCK_DIFF_ZERO:
			logger.info("TAG_STOCK_DIFF_ZERO");
			chaine = " having sum(p_depot.QUANTITE) != 0 ";
			break;
		case TAG_STOCK_EQAL_ZERO:
			logger.info("TAG_STOCK_EQA_ZERO");
			chaine = " having sum(p_depot.QUANTITE) = 0 ";
			break;
		case TAG_STOCK_INF_ZERO:
			logger.info("TAG_STOCK_INF_ZERO");
			chaine = " having sum(p_depot.QUANTITE) < 0 ";
			break;
		case TAG_STOCK_SUP_ZERO:
			logger.info("TAG_STOCK_SUP_ZERO");
			chaine = " having sum(p_depot.QUANTITE) > 0 ";
			break;
		case TAG_STOCK_TOUS_PRODUITS:
			chaine = "";
			break;
		case TAG_STOCK_FORMULA_ZERO:
			logger.info("TAG_STOCK_FORMULA_ZERO");
			chaine = (String) map.get("chaine");
			break;
		default:
			logger.info("default");
			chaine = "";
			break;
		}
				
		String query = "select MyDerivedTable.DESIGNATION ," + 
				"MyDerivedTable.CODE_PRODUIT ," + 
				"MyDerivedTable.QUANTITE " +
				"from (select gen.DESIGNATION," + 
				"dims.CODE_PRODUIT," + 
				"sum(p_depot.QUANTITE) QUANTITE," + 
				"ROW_NUMBER() OVER(ORDER BY gen.DESIGNATION ) AS ROW " + 
				"from PRD_DIM_DEPOT p_depot," + 
				"STK_PRD_DIM dims," + 
				"STK_PRODUITS_GENERIQUE gen " +
				"where p_depot.NUM_DIMS = dims.NUM_DIMS and " + 
				"gen.NUM_PRODUIT = p_depot.NUM_PRODUIT "+
				"group by dims.CODE_PRODUIT,gen.DESIGNATION,dims.num_dims "
				+chaine
				+ " ) AS MyDerivedTable " + 
				" WHERE MyDerivedTable.Row BETWEEN ? AND ?";
		
		logger.info("query : "+query);
		List<StockModel> stockModel = jdbcTemplate.query(query, new Object[] { from,to },ParametreRowMapper);
		Type typeOf = new TypeToken<List<StockModel>>() {}.getType();
		String data = getGson().toJson(stockModel,typeOf);
		response.setData(data);
		response.setSuccess(true);
		return response;
	}
	@GetMapping("/dashboardMagasins")
	public List<DashboardModel> getDashboardMagasins(@RequestParam(required = false) String codeMagasin) {
		List<DashboardModel> stats;
		if (codeMagasin != null && !codeMagasin.isEmpty()) {
			stats = vueCaMagJourObjDAO.getStatsParMagasinAndCode(codeMagasin);
		} else {
			stats = vueCaMagJourObjDAO.getStatsParMagasin();
		}

		// Calcul de la moyenne du CA (montant_ttc)
		double sommeCA = stats.stream().mapToDouble(DashboardModel::getMontantTTC).sum();
		double moyenneCA = stats.isEmpty() ? 0 : sommeCA / stats.size();

		// Affecte la moyenne comme objectif et calcule le taux d'atteinte pour chaque magasin
		for (DashboardModel d : stats) {
			d.setObjectif(moyenneCA);
			d.setTauxObjectif(moyenneCA > 0 ? d.getMontantTTC() / moyenneCA * 100 : 0);
		}
		return stats;
	}


	@GetMapping("/evolutionCA")
	public List<EvolutionCAModel> getEvolutionCA(
			@RequestParam String dateDebut,
			@RequestParam String dateFin,
			@RequestParam(required = false) String codeMagasin) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date debut = sdf.parse(dateDebut);
		Date fin = sdf.parse(dateFin);

		List<VueCaMagJourObj> ventes;
		if (codeMagasin != null && !codeMagasin.isEmpty()) {
			ventes = vueCaMagJourObjDAO.getInfosByDateGroupedByVenteDayAndMagasin(debut, fin, codeMagasin);
		} else {
			ventes = vueCaMagJourObjDAO.getInfosByDateGroupedByVenteDay(debut, fin);
		}

		List<EvolutionCAModel> evolution = new ArrayList<>();
		for (VueCaMagJourObj v : ventes) {
			evolution.add(new EvolutionCAModel(v.getJourVente(), v.getMontantTTC()));
		}
		return evolution;
	}
	@PostMapping("/compareMagasins")
	public CompareResponse compareMagasins(@RequestBody List<String> codesMagasins) {
		List<DashboardModel> stats = vueCaMagJourObjDAO.getStatsParMagasins2(codesMagasins);

		// Classement par CA décroissant
		stats.sort((a, b) -> Double.compare(b.getMontantTTC(), a.getMontantTTC()));

		// Calcul des écarts
		double maxCA = stats.stream().mapToDouble(DashboardModel::getMontantTTC).max().orElse(0);
		double minCA = stats.stream().mapToDouble(DashboardModel::getMontantTTC).min().orElse(0);
		double ecartCA = maxCA - minCA;

		double maxTickets = stats.stream().mapToDouble(DashboardModel::getNombreTickets).max().orElse(0);
		double minTickets = stats.stream().mapToDouble(DashboardModel::getNombreTickets).min().orElse(0);
		double ecartTickets = maxTickets - minTickets;

		double maxQuantite = stats.stream().mapToDouble(DashboardModel::getQuantite).max().orElse(0);
		double minQuantite = stats.stream().mapToDouble(DashboardModel::getQuantite).min().orElse(0);
		double ecartQuantite = maxQuantite - minQuantite;

		CompareResponse response = new CompareResponse();
		response.setMagasins(stats);
		response.setClassement(stats); // déjà trié
		response.setEcartCA(ecartCA);
		response.setEcartTickets(ecartTickets);
		response.setEcartQuantite(ecartQuantite);

		return response;
	}


	@RequestMapping(value = "/Login", method = RequestMethod.POST)
	public MyResponse Login(@RequestBody HashMap map) {
		MyResponse response = new MyResponse();
		
		try {
			logger.info("map : "+map);
			String nom = (String) map.get("nom");
			String motPasse = (String) map.get("motPasse");
			
			CfgUtilisateur cfg = cfgUserDao.findByNomAndMotPasse(nom, motPasse);
			if(cfg != null) {
				List<DspMagasin> listDspMag = magasinDao.getMagasins();
				List<MagasinModel> listModelMag = new ArrayList<>();
				for(DspMagasin mag:listDspMag) {
					MagasinModel magModel = new MagasinModel(mag.getNumMagasin(), mag.getCodeMagasin(), mag.getNomMagasin());
					listModelMag.add(magModel);
				}
				UserModel user = new UserModel(cfg.getId().getNumUtilisateur(),
						cfg.getId().getNumMagasin(),
						cfg.getNom(),
						cfg.getMotPasse(),
						cfg.getNumVendeur());
				List<Parametres> listParam = paramDao.findByModule("ANDROID_APP");
				for(Parametres param:listParam) {
					if(param.getId().getParametre()!=null && param.getId().getParametre().trim().equals("IMAGE_LOGO")) {
						try {
							String imagePath = param.getValeur();
							File f = new File(imagePath);
							InputStream targetStream = new FileInputStream(f);						
							byte[] bytes = StreamUtils.copyToByteArray(targetStream);
							String imageBase64 = new String(Base64.getEncoder().encode(bytes), "UTF-8");
							logger.info("imageBase64 "+imageBase64);
							param.setValeur(imageBase64);
						}catch(FileNotFoundException ex) {
							logger.error(ex.getMessage(),ex);
							//ex.printStackTrace();
							param.setValeur(null);
						}
						
					}
				}
				HashMap<String, Object> data = new HashMap<>();
				data.put("user", user);
				data.put("mags", listModelMag);
				data.put("param", listParam);
				response.setData(getGson().toJson(data));
				response.setSuccess(true);
			}else {
				response.setSuccess(false);
				response.setMessage("nom utilisateur ou mot de passe est incorrect !");
			}
		}catch(Exception ex) {
			ex.printStackTrace();
			response.setSuccess(false);
			response.setMessage(ex.getMessage());
			response.setException(ex);
		}		
		
		return response;
	}
	
	@RequestMapping(value = "/Magasins", method = RequestMethod.POST)
	public MyResponse getMagasins() {
		MyResponse response = new MyResponse();
		try {
			List<DspMagasin> listDspMag = magasinDao.getMagasins();
			logger.info("listSize : "+listDspMag.size());
			List<MagasinModel> listModelMag = new ArrayList<>();
			for(DspMagasin mag:listDspMag) {
				MagasinModel magModel = new MagasinModel(mag.getNumMagasin(), mag.getCodeMagasin(), mag.getNomMagasin());
				listModelMag.add(magModel);
			}
			HashMap<String, Object> data = new HashMap<>();
			data.put("mags", listModelMag);
			response.setData(getGson().toJson(data));
			response.setSuccess(true);
			
		}catch(Exception ex) {
			logger.error(ex.getMessage(),ex);
			response.setSuccess(false);
			response.setMessage(ex.getMessage());
			response.setException(ex);
		}		
		
		return response;
	}
	
	@RequestMapping(value = "/checkServer", method = RequestMethod.POST)
	public Integer checkServer(@RequestBody Integer number) {
		logger.info("checkServer "+ number*2);
		return number*2;
	}

}
