package it.unifi.ing.swam.dao;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.unifi.ing.swam.model.User;
import it.unifi.ing.swam.model.UserRole;

@RequestScoped
public class UserDao {

	
	@PersistenceContext
	private EntityManager entityManager;
	
	public void save(User user) {
		if(user.getId() != null)
			this.entityManager.merge(user);
		else
			this.entityManager.persist(user);
	}
	
	public void delete(User user) {
		this.entityManager.remove(
				this.entityManager.contains(user) ? user : this.entityManager.merge(user));
	}
	
	public User findById(Long administratorId) {
		return this.entityManager.find(User.class, administratorId);
	}
	
	public boolean existsAdministrator() {
		List<User> result = this.entityManager
				.createQuery("SELECT a "
							+ "FROM User a where a.userRole=:adminRole", User.class)
				.setParameter("adminRole", UserRole.Admin)
				.getResultList();
		
		if(result.isEmpty())
			return false;
		
		return true;
	}

	public int getHistory(Long id){
		int result = this.entityManager.createQuery("SELECT count ( b ) " +
							"FROM Booking b where b.registeredUserOwner.id=:id", Long.class)
							.setParameter("id", id).getSingleResult().intValue();
		return result;
	}
	
	public User login(String username, String password) {
		List<User> result = this.entityManager
				.createQuery("SELECT a "
							+ "FROM User a "
							+ "WHERE a.username = :username AND a.password = :password",
							User.class)
				.setParameter("username", username)
				.setParameter("password", password)
				.setMaxResults(1)
				.getResultList();
		
		if(result.isEmpty()) {
			return null;
		}
		return result.get(0);
	}
}
