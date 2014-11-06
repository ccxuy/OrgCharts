package utilities;

import java.io.FileInputStream;
import java.sql.Blob;
import java.util.List;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import beans.ChartBean;
import beans.ProfileBean;

//import org.json.simple.JSONObject;
public class HibernateUtilities {
	private static final Logger log = Logger
			.getLogger(HibernateUtilities.class);

	private static SessionFactory sessfactory;

	public static SessionFactory getFactory() throws Exception {
		if (sessfactory == null) {
			try {
				sessfactory = new Configuration().configure(
						"/hibernate.cfg.xml").buildSessionFactory();
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
			System.out.println(query.list());
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
	 * @param employeeProfile
	 * @return 1 if success, 0 if failed
	 */
	public static int editNode(ProfileBean employeeProfile){
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
	 * @deprecated use {@link #editNode()} instead.
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
			// System.out.println("I am here hql");
			// System.out.println(hql);
			// System.out.println("I am here hql");
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
			System.out.println("in here");
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
		// System.out.println("here");
		// System.out.println(data);
		// System.out.println("here");
		return data;
	}

	public static List searchName(int id) throws Exception {
		List data = null;
		Query query = null;
		System.out.println("I am here 1");
		Session session = sessfactory.openSession();
		Transaction tx = null;
		System.out.println("I am here");
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
			if(null != emp){
				//success
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
	 * Search char with uuid in UUID column.
	 * @param uuid
	 * @return ChartBean if success, null if failed
	 */
	public static ChartBean searchChartByUUID(String uuid) {
		Session session = sessfactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			ChartBean chartBean = (ChartBean) session.get(ChartBean.class, uuid);
			tx.commit();
			if(null != chartBean){
				//success
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
			//success
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
}
