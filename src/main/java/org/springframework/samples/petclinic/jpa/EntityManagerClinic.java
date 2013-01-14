package org.springframework.samples.petclinic.jpa;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.samples.petclinic.Clinic;
import org.springframework.samples.petclinic.Owner;
import org.springframework.samples.petclinic.Pet;
import org.springframework.samples.petclinic.PetType;
import org.springframework.samples.petclinic.Vet;
import org.springframework.samples.petclinic.Visit;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataAccessException;

/**
 * JPA implementation of the Clinic interface using EntityManager.
 *
 * <p>The mappings are defined in "orm.xml" located in the META-INF directory.
 *
 * @author Mike Keith
 * @author Rod Johnson
 * @author Sam Brannen
 * @author Michael Isvy
 * @since 22.4.2006
 */
@Repository
@Transactional
public class EntityManagerClinic implements Clinic {

	@PersistenceContext
	private EntityManager em;


	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public Collection<Vet> getVets() {
		return this.em.createQuery("SELECT vet FROM Vet vet ORDER BY vet.lastName, vet.firstName").getResultList();
	}

	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public Collection<PetType> getPetTypes() {
		return this.em.createQuery("SELECT ptype FROM PetType ptype ORDER BY ptype.name").getResultList();
	}

	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public Collection<Owner> findOwners(String lastName) {
		Query query = this.em.createQuery("SELECT owner FROM Owner owner WHERE owner.lastName LIKE :lastName");
		query.setParameter("lastName", lastName + "%");
		return query.getResultList();
	}

	@Transactional(readOnly = true)
	public Owner findOwner(int id) {
		return this.em.find(Owner.class, id);
	}

	@Transactional(readOnly = true)
	public Pet findPet(int id) {
		return this.em.find(Pet.class, id);
	}

	public void storeOwner(Owner owner) {
		this.em.merge(owner);

	}

	public void storePet(Pet pet) {
		this.em.merge(pet);
	}

	public void storeVisit(Visit visit) {
		this.em.merge(visit);
	}

	public void deletePet(int id) throws DataAccessException {
		Pet pet = findPet(id);
		this.em.remove(pet);
	}

}