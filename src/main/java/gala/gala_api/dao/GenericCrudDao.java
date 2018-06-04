package gala.gala_api.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * This class conducts database actions for genric entities in the database.
 */
@Component
@Repository
public abstract class GenericCrudDao<Entity> implements CrudRepository<Entity, Long> {

    @Autowired
    @PersistenceContext
    protected EntityManager entityManager;


    private Class<Entity> entityClass;

    public GenericCrudDao(Class<Entity> entityClass) {
        this.entityClass = entityClass;
    }

	@Override
	public <S extends Entity> S save(S entity) {
        this.entityManager.persist(entity);
        
		return entity;
	}

	@Override
	public <S extends Entity> Iterable<S> saveAll(Iterable<S> entities) {
        for (S e : entities) {
            this.entityManager.persist(e);
        }
		return entities;
	}

	@Override
	public Optional<Entity> findById(Long id) {
        Entity result = this.entityManager.find(this.entityClass, id);
        if (result == null) {
            return Optional.empty();
        } else {
            return Optional.of(result);
        }
	}

    @Override
	public boolean existsById(Long id) {
		Entity result = this.entityManager.find(this.entityClass, id);
        return result != null;
	}

    @Override
	public Iterable<Entity> findAll() {
		return this.entityManager.createQuery("SELECT * FROM " + this.entityClass.getName() + ";").getResultList();
	}

	@Override
	public Iterable<Entity> findAllById(Iterable<Long> ids) {
        List<Entity> allEntitys = new ArrayList<Entity>();
        
        for (Long id : ids) {
            Optional<Entity> result = this.findById(id);
            if (result.isPresent()) {
                allEntitys.add(result.get());
            }
        }

        return allEntitys;
	}

    @Override
	public long count() {
		return this.entityManager.createQuery("SELECT Count(1) FROM " + this.entityClass.getName() + ";").getFirstResult();
	}

	@Override
	public void deleteById(Long id) {
        Optional<Entity> Entity = this.findById(id);
        if (Entity.isPresent()) {
            this.entityManager.remove(Entity.get());
        }
	}

	@Override
	public void delete(Entity entity) {
		this.entityManager.remove(entity);
	}

	@Override
	public void deleteAll(Iterable<? extends Entity> entities) {
		for (Entity e : entities) {
            this.entityManager.remove(e);
        }
	}

	@Override
	public void deleteAll() {
		this.entityManager.createQuery("DELETE FROM " + this.entityClass.getName()).executeUpdate();
	}

}