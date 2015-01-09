package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Blob;
import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;

import config.Setting;
import beans.ChartBean;
import beans.ProfileBean;

//import org.json.simple.JSONObject;
public class HibernateUtilities {
	private static final Logger log = LoggerFactory
			.getLogger(HibernateUtilities.class);

	private static SessionFactory sessfactory;
	private static ServiceRegistry serviceRegistry;

	public static SessionFactory getFactory() throws Exception {
//		File cfg = new File("hibernate.cfg.xml");
		if (sessfactory == null) {
			System.out.println("Working Directory = " +
		              System.getProperty("user.dir"));
			try {
				Configuration configuration = new Configuration();
			    configuration.configure();
			    serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
			            configuration.getProperties()).build();
			    sessfactory = configuration.buildSessionFactory(serviceRegistry);
			} catch (Exception e) {
				log.error("Initial SessionFactory creation failed." + e);
				throw new IllegalStateException(
						"Initial Session Factory creation failed.");
			} catch (Throwable ex) {
				System.err.println("Failed to create sessionFactory object."
						+ ex);
				throw new ExceptionInInitializerError(ex);
			}
			return sessfactory;
		} else {
			return sessfactory;
		}
	}

	public static List searchResults() throws Exception {
		List data = null;
		Query query = null;
		Session session = sessfactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			query = session.createQuery("FROM ProfileBean order by user");
			data = query.list();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			System.out.println(e);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex);
		} finally {
			session.close();
		}
		return data;
	}

	public static void addRecord(String sfirstName, String slastName,
			String email, Blob dataBlob) {
		Session session = sessfactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			ProfileBean data = new ProfileBean(sfirstName, slastName, email,
					dataBlob);
			session.save(data);
			tx.commit();

		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public static String getId() {
		String empId = "";
		Session session = sessfactory.openSession();
		Query query = null;
		Transaction tx = null;
		try {
			String hql = "select max(id) from ProfileBean";
			tx = session.beginTransaction();
			query = session.createQuery(hql);
			System.out.println("HibernateUtilities@getId"+query.list());
			empId = ((Integer) (query.list().get(0))).toString();
			tx.commit();

		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return empId;
	}

	/**
	 * Update employee profile according to it's id.
	 * 
	 * @param employeeProfile
	 * @return 1 if success, 0 if failed
	 */
	public static int editNode(ProfileBean employeeProfile) {
		Session session = sessfactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.update(employeeProfile);
			tx.commit();
			return 1;
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return 0;
	}

	/**
	 * @param sfirstName
	 * @param slastName
	 * @param email
	 * @param image
	 * @param empId
	 * @deprecated use {@link #saveOrUpdateEmployee} instead.
	 */
	@Deprecated
	public static void editNode(String sfirstName, String slastName,
			String email, Blob image, int empId) {
		Session session = sessfactory.openSession();
		Query query = null;
		Transaction tx = null;
		// String hql =
		// "update ProfileBean set name = :sname where node = :empnode";
		String hql = "update ProfileBean set first_name = :sfirstName, last_name = :slastName, email = :email, image = :image where emp_id like :empId";

		try {
			tx = session.beginTransaction();
			query = session.createQuery(hql);
			query.setParameter("sfirstName", sfirstName);
			query.setParameter("slastName", slastName);
			query.setParameter("email", email);
			query.setParameter("image", image);
			query.setParameter("empId", empId);

			query.executeUpdate();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public static void deleteRecord(String nodeid) throws Exception {
		Session session = sessfactory.openSession();
		Query query = null;
		Transaction tx = null;
		String hql = "delete from ProfileBean where node like :nodeid";
		try {
			query = session.createQuery(hql);
			tx = session.beginTransaction();
			query.setParameter("nodeid", nodeid + "%");
			query.executeUpdate();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

	}

	public static List searchId() throws Exception {
		List<String> data = new ArrayList<String>();
		List id = null;
		List first = null;
		List last = null;
		Query query1 = null;
		Query query2 = null;
		Query query3 = null;
		String toAdd = "";
		Session session = sessfactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			query1 = session
					.createQuery("select id from ProfileBean order by id");
			query2 = session
					.createQuery("select firstName from ProfileBean order by id");
			query3 = session
					.createQuery("select lastName from ProfileBean order by id");
			id = query1.list();
			first = query2.list();
			last = query3.list();
			for (int x = 0; x < id.size(); x++) {
				toAdd = id.get(x).toString() + ":" + first.get(x).toString()
						+ ":" + last.get(x).toString();
				data.add(toAdd);
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return data;
	}

	// ////////////////////////////////////
	public static List getEditedData(String id) {
		Session session = sessfactory.openSession();
		List data = null;
		// Query query = null;
		Transaction tx = null;
		// JSONObject json = new JSONObject();

		Query query1 = null;
		Query query2 = null;
		Query query3 = null;
		Query query4 = null;
		try {
			tx = session.beginTransaction();

			// query =
			// session.createQuery("select firstName, lastName, email, img from ProfileBean where id = :id");
			// query.setParameter("id", Integer.parseInt(id));
			
			query1 = session
					.createQuery("select firstName from ProfileBean where id= :id");
			query2 = session
					.createQuery("select lastName from ProfileBean where id= :id");
			query3 = session
					.createQuery("select email from ProfileBean where id= :id");
			query4 = session
					.createQuery("select img from ProfileBean where id= :id");

			// query.setParameter("node", node);
			query1.setParameter("id", Integer.parseInt(id));
			query2.setParameter("id", Integer.parseInt(id));
			query3.setParameter("id", Integer.parseInt(id));
			query4.setParameter("id", Integer.parseInt(id));
			// data = query.list();
			data = query1.list();
			data.add(query2.list());
			data.add(query3.list());
			data.add(query4.list());

			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return data;
	}

	public static List searchName(int id) throws Exception {
		List data = null;
		Query query = null;
		Session session = sessfactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			query = session.createQuery("FROM ProfileBean where id = " + id);
			data = query.list();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return data;
	}

	/**
	 * @param id
	 * @return Employee ProfileBean if success, null if failed
	 */
	public static ProfileBean searchEmployeeById(int id) {
		Session session = sessfactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			ProfileBean emp = (ProfileBean) session.get(ProfileBean.class, id);
			tx.commit();
			if (null != emp) {
				// success
				return emp;
			}
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}
	
	/**
	 * @param id
	 * @return Employee ProfileBean if success, null if failed
	 */
	public static ProfileBean searchEmployeeById(String id) {
		try {
			return searchEmployeeById(Integer.parseInt(id));
		} catch (NumberFormatException e) {
			System.err.println("searchEmployeeById: input not an integer!");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @return
	 */
	public static List<ProfileBean> getAllEmployee(){
		return getAllEmployee(Setting.ServerSetting.queryLimit);
	}
	
	/**
	 * @param limitQueries, <= 0 for no limit
	 * @return
	 */
	public static List<ProfileBean> getAllEmployee(int limitQueries){

		Session session = sessfactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			// Quereis
			Criteria crit = session.createCriteria(ProfileBean.class);
			if(limitQueries>0)crit.setMaxResults(limitQueries);
			List<ProfileBean>  empList = crit.list();
			tx.commit();
			if (null != empList) {
				// A success query would return not null result even no match
				return empList;
			}
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	/**
	 * @param pb
	 * @return id(>0) if success, 0 failed
	 */
	public static int saveOrUpdateEmployee(ProfileBean pb) {
		Session session = sessfactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			int id = -1;
			// If this is a new employee profileBean.
			if (pb.getId() <= 0) {
				id = (Integer) session.save(pb);
			} else {
				session.saveOrUpdate(pb);
				id = pb.getId();
			}
			tx.commit();
			return id;
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return 0;
	}

	/**
	 * @param empid
	 * @return 1 if success, 0 if failed,, -1 if chart of this id not found.
	 */
	public static int deleteEmployeeById(int empid) {
		Session session = sessfactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			ProfileBean bean = (ProfileBean) session
					.get(ProfileBean.class, empid);
			if(null == bean){
				return -1;
			}
			session.delete(bean);
			tx.commit();
			if (null != bean) {
				// success
				return 1;
			}
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return 0;
	}

	public static int deleteEmployeeById(String empid) {
		try {
			return deleteEmployeeById(Integer.parseInt(empid));
		} catch (NumberFormatException e) {
			System.err.println("deleteEmployeeById: input not an integer!");
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Search char with uuid in UUID column.
	 * 
	 * @param uuid
	 * @return ChartBean if success, null if failed
	 */
	public static ChartBean searchChartByUUID(String uuid) {
		Session session = sessfactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			ChartBean chartBean = (ChartBean) session
					.get(ChartBean.class, uuid);
			tx.commit();
			if (null != chartBean) {
				// success
				return chartBean;
			}
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	/**
	 * @param chartBean
	 * @return 1 if success, 0 failed
	 */
	public static int saveOrUpdateChart(ChartBean chartBean) {
		Session session = sessfactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(chartBean);
			tx.commit();
			// success
			return 1;
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return 0;
	}
	
	public static List<ChartBean> getAllChart(){
		return getAllChart(Setting.ServerSetting.queryLimit);
	}

	/**
	 * @param limitQueries
	 * @return
	 */
	public static List<ChartBean> getAllChart(int limitQueries) {
		Session session = sessfactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			// Quereis
			Criteria crit = session.createCriteria(ChartBean.class);
			crit.setMaxResults(limitQueries);
			List<ChartBean>  chartList = crit.list();
			tx.commit();
			if (null != chartList) {
				// A success query would return not null result even no match
				return chartList;
			}
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}
	
	/**
	 * Count how many chart have given name.
	 * If name=null specified, it return all chart count.
	 * @param name chart name or set to null for all chart count.
	 * @return how many chart have this name. or -1 for failed.
	 */
	public static long countCharBytName(String name){
		Session session = sessfactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			// Quereis
			Criteria crit = session.createCriteria(ChartBean.class);
			crit.setProjection(Projections.rowCount());
			if(null != name){
//				crit.add(Restrictions.eqOrIsNull(Setting.ChartAlias.ChartField_Name, name));
				crit.add( Restrictions.eq("Setting.ChartAlias.ChartField_Name", name));
			}
			Long result=(Long)crit.uniqueResult();
			return result;
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return -1;
	}
	

	/**
	 * getAllChartByOwnerId with default query limit
	 * @param ownerId should not be null, or it would consider no ownerId
	 * @return list of ChartBean created by this ownerId, null if failed
	 */
	public static List<ChartBean> getAllChartByOwnerId(String ownerId){
		return getAllChartByOwnerId(ownerId, Setting.ServerSetting.queryLimit);
	}
	

	/**
	 * @param ownerId should not be null, or it would consider all ownerId
	 * @param limitQueries
	 * @return list of ChartBean created by this ownerId, null if failed
	 */
	public static List<ChartBean> getAllChartByOwnerId(String ownerId, int limitQueries) {
		Session session = sessfactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			// Quereis
			Criteria crit = session.createCriteria(ChartBean.class);
			crit.setMaxResults(limitQueries);
			if(null != ownerId){
				crit.add(Restrictions.eqOrIsNull(Setting.ChartAlias.ChartField_OwnerId, ownerId));
			}
			List<ChartBean>  chartList = crit.list();
			tx.commit();
			if (null != chartList) {
				// A success query would return not null result even no match
				return chartList;
			}
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}
	
	/**
	 * @param uuid
	 * @return 1 if success, 0 if failed,, -1 if chart of this id not found.
	 */
	public static int deleteChartByUUID(String uuid) {
		Session session = sessfactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			ChartBean chartBean = (ChartBean) session
					.get(ChartBean.class, uuid);
			if(null == chartBean){
				return -1;
			}
			session.delete(chartBean);
			tx.commit();
			if (null != chartBean) {
				// success
				return 1;
			}
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return 0;
	}
	
}
