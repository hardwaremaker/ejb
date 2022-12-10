package com.lp.server.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class DtoAssemblerFactory<E, D> {
	public D internalCreateDto(E entity, Class<D> cls) {
		D dto = createEntity(cls) ;
		if(entity != null) {
			setDto(entity, dto) ;
		}
		return dto ;
	}
	
	public D[] internalCreateDtos(Collection<E> entities, Class<D> cls) {
		return (D[]) internalCreateDtosList(entities, cls).toArray() ;
	}

	public List<D> internalCreateDtosList(Collection<E> entities, Class<D> dtoClass) {
		List<D> list = new ArrayList<D>() ;
		if(entities != null) {
			for (E entity : entities) {
				list.add(internalCreateDto(entity, dtoClass)) ;
			}
		}
		
		return list ;
	}
	
	
	protected D createEntity(Class<D> cls) {
		try {
			return cls.newInstance() ;
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}
		
		return null ;
	}
	
	/**
	 * Das Dto mit den Entity-Werten setzen</b>
	 * <p>Es wird ein Default-Dto erzeugt
	 * 
	 * @param entity
	 * @param dto
	 * @return das neu aus der Entity gesetzte Dto
	 */
	public abstract D setDto(E entity, D dto) ;
	
	public abstract E setEntity(E entity, D dto) ;
}
