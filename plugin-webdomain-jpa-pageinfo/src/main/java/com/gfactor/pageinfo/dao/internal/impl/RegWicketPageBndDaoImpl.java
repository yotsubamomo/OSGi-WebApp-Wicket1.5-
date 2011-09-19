/**
 * 
 */
package com.gfactor.pageinfo.dao.internal.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.support.JpaDaoSupport;

import com.gfactor.pageinfo.dao.RegWicketPageBndDao;
import com.gfactor.pageinfo.jpa.Bndpageinfo;


/**
 * @author momo
 *
 */
public class RegWicketPageBndDaoImpl extends JpaDaoSupport implements RegWicketPageBndDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());	

//	@PersistenceUnit
//	private EntityManagerFactory entityManagerFactory;
	
	@PersistenceContext
    private EntityManager em;
	
//	/**
//     * Constructor
//     */
//    @Autowired
//    public RegWicketPageBndDaoImpl(EntityManagerFactory entityManagerFactory) {
//    	System.out.println("to set entityManagerFactory ="+ entityManagerFactory);
//        super.setEntityManagerFactory(entityManagerFactory);
//    }
//    
    /**
     * get Bndpageinfo object by bndname,bndver and entry_point
     */
    
	public List<Bndpageinfo> findBndPageInfoList(String bndName, String bndVer,
			String entry_point) {
		
		
		return getJpaTemplate()
				.find("select r from Bndpageinfo r where r.bundle_name = ?1 and r.bundle_version =?2 and r.entry_point = ?3",
						bndName, bndVer, entry_point);
	}
	

	public void saveBndPageInfo(Bndpageinfo bndpageinfo) {
		logger.info("save user " + bndpageinfo); 
		getJpaTemplate().persist(bndpageinfo);
	}


	public Bndpageinfo update(Bndpageinfo bndpageinfo) {
		return getJpaTemplate().merge(bndpageinfo);
		
	}


	public void delete(Bndpageinfo bndpageinfo) {
		logger.info("delete on dao , obj = " + bndpageinfo);
		
		Bndpageinfo bndObj = getJpaTemplate().merge(bndpageinfo);
		
		logger.info("delete on dao , merg obj = " + bndObj);
		
		getJpaTemplate().remove(bndObj);

	}

	@Override
	public Bndpageinfo getBndPageInfo(String bndName, String bndVer,
			String entry_point) {
		logger.info("getBndPageInfo query value = " +bndName+","+bndVer+","+entry_point);
		Bndpageinfo bndRst = null;
		
		try{
			Query query = this.em.createNamedQuery("QueryByName_Ver_Entrypoint");
			query.setParameter("bndName", bndName);
			query.setParameter("bndVer", bndVer); 
			query.setParameter("bndEntryPoint", entry_point);
			bndRst = (Bndpageinfo) query.getSingleResult();
		}catch (Exception e) {
			logger.error("dao getBndPageInfo exception = " +e);
		}
		
		logger.info("bndRst ============> "+ bndRst);
		return bndRst;
	}

	@Override
	public Bndpageinfo findByEntry(Bndpageinfo bndpageinfo) {
		
		return getJpaTemplate().find(Bndpageinfo.class, bndpageinfo.getId());
	}


	@Override
	public void deleteExpiredBndPageInfo(Bndpageinfo bndpageinfo) {
		logger.info("deleteExpiredBndPageInfo start.....");
		try{
//			Query query = getJpaTemplate()
//					.getEntityManagerFactory()
//					.createEntityManager()
//					.createQuery(
			Query query = this.em.createQuery(
							"delete from Bndpageinfo o " +
							"where o.bundle_name = :bndName " +
							"and o.bundle_version = :bndVersion " +
							"and o.entry_point = :entryPoint " +
							"and o.id NOT in (:bndId) ");
			logger.info("em = "+ this.em);
//			Query query = em.createQuery("delete from Organization o "+ "where o.name like :name);
			query.setParameter("bndName", bndpageinfo.getBundle_name());
			query.setParameter("bndVersion", bndpageinfo.getBundle_version()); 
			query.setParameter("entryPoint", bndpageinfo.getEntry_point());
			query.setParameter("bndId", bndpageinfo.getId());
			
			int deleted = query.executeUpdate();
			logger.info("deleteExpiredBndPageInfo count = " + deleted);
			
		}catch (Exception e) {
			logger.error("dao getBndPageInfo exception = " +e);
			
		}
		
		
	}




}
