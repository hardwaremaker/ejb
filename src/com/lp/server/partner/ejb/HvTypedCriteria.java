/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.server.partner.ejb;

import java.util.List;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.transform.ResultTransformer;

/**
 * Verhaelt sich wie das normale org.hibernate.Criteria, nur dass die Methode
 * list() eine typisierte Liste liefert.
 * 
 * @author robert
 * 
 * @param <T>
 */
public class HvTypedCriteria<T> implements Criteria {

	private Criteria c;

	public HvTypedCriteria(Criteria c) {
		this.c = c;
	}

	@Override
	public HvTypedCriteria<T> add(Criterion arg0) {
		c.add(arg0);
		return this;
	}

	@Override
	public HvTypedCriteria<T> addOrder(Order arg0) {
		c.addOrder(arg0);
		return this;
	}

	@Override
	public Criteria createAlias(String arg0, String arg1)
			throws HibernateException {
		return c.createAlias(arg0, arg1);
	}

	@Override
	public Criteria createAlias(String arg0, String arg1, int arg2)
			throws HibernateException {
		return c.createAlias(arg0, arg1, arg2);
	}

	@Override
	public Criteria createCriteria(String arg0)
			throws HibernateException {
		return c.createCriteria(arg0);
	}

	@Override
	public Criteria createCriteria(String arg0, int arg1)
			throws HibernateException {
		return c.createCriteria(arg0, arg1);
	}

	@Override
	public Criteria createCriteria(String arg0, String arg1)
			throws HibernateException {
		return c.createCriteria(arg0, arg1);
	}

	@Override
	public Criteria createCriteria(String arg0, String arg1, int arg2)
			throws HibernateException {
		return c.createCriteria(arg0, arg1, arg2);
	}

	@Override
	public String getAlias() {
		return c.getAlias();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> list() throws HibernateException {
		return (List<T>) c.list();
	}

	@Override
	public ScrollableResults scroll() throws HibernateException {
		return c.scroll();
	}

	@Override
	public ScrollableResults scroll(ScrollMode arg0) throws HibernateException {
		return c.scroll(arg0);
	}

	@Override
	public HvTypedCriteria<T> setCacheMode(CacheMode arg0) {
		c.setCacheMode(arg0);
		return this;
	}

	@Override
	public HvTypedCriteria<T> setCacheRegion(String arg0) {
		c.setCacheRegion(arg0);
		return this;
	}

	@Override
	public HvTypedCriteria<T> setCacheable(boolean arg0) {
		c.setCacheable(arg0);
		return this;
	}

	@Override
	public HvTypedCriteria<T> setComment(String arg0) {
		c.setComment(arg0);
		return this;
	}

	@Override
	public HvTypedCriteria<T> setFetchMode(String arg0, FetchMode arg1)
			throws HibernateException {
		c.setFetchMode(arg0, arg1);
		return this;
	}

	@Override
	public HvTypedCriteria<T> setFetchSize(int arg0) {
		c.setFetchSize(arg0);
		return this;
	}

	@Override
	public HvTypedCriteria<T> setFirstResult(int arg0) {
		c.setFirstResult(arg0);
		return this;
	}

	@Override
	public HvTypedCriteria<T> setFlushMode(FlushMode arg0) {
		c.setFlushMode(arg0);
		return this;
	}

	@Override
	public HvTypedCriteria<T> setLockMode(LockMode arg0) {
		c.setLockMode(arg0);
		return this;
	}

	@Override
	public HvTypedCriteria<T> setLockMode(String arg0, LockMode arg1) {
		c.setLockMode(arg0, arg1);
		return this;
	}

	@Override
	public HvTypedCriteria<T> setMaxResults(int arg0) {
		c.setMaxResults(arg0);
		return this;
	}

	@Override
	public HvTypedCriteria<T> setProjection(Projection arg0) {
		c.setProjection(arg0);
		return this;
	}

	@Override
	public HvTypedCriteria<T> setResultTransformer(ResultTransformer arg0) {
		c.setResultTransformer(arg0);
		return this;
	}

	@Override
	public HvTypedCriteria<T> setTimeout(int arg0) {
		c.setTimeout(arg0);
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T uniqueResult() throws HibernateException {
		return (T) c.uniqueResult();
	}

}
