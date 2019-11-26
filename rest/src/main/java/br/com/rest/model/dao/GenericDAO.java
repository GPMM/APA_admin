package br.com.rest.model.dao;

import java.util.List;

import javax.persistence.EntityManager;

public class GenericDAO<T> {
	
	protected EntityManager em;
	protected Class<T> t;
	
	GenericDAO(Class<T> t, EntityManager em){
		this.t = t;
		this.em = em;
	}
	
	public List<T> findAll(){
		@SuppressWarnings("unchecked")
		List<T> lista = em.createQuery("from " + t.getName()).getResultList();
		
		return lista;
	}
	
	public T find(Integer id){
		T t1 = em.find(t, id);
		return t1;
	}
	
	public void incluir(T entidade) {
		em.persist(entidade);
	}
	
	public void excluir(T entidade) {
		em.remove(entidade);
	}
	
	public void alterar(T entidade) {
		em.merge(entidade);
	}
}
