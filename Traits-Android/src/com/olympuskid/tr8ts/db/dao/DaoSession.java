package com.olympuskid.tr8ts.db.dao;

import java.util.Map;

import android.database.sqlite.SQLiteDatabase;

import com.olympuskid.tr8ts.db.models.Trait;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

	private final DaoConfig traitDaoConfig;

	private final TraitDao traitDao;

	public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> daoConfigMap) {
		super(db);

		traitDaoConfig = daoConfigMap.get(TraitDao.class).clone();
		traitDaoConfig.initIdentityScope(type);

		traitDao = new TraitDao(traitDaoConfig, this);

		registerDao(Trait.class, traitDao);
	}

	public void clear() {
		traitDaoConfig.getIdentityScope().clear();
	}

	public TraitDao getTraitDao() {
		return traitDao;
	}

}
