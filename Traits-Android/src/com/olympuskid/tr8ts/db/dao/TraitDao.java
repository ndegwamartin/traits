package com.olympuskid.tr8ts.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.olympuskid.tr8ts.db.models.Trait;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/**
 * DAO for table trait.
 */
public class TraitDao extends AbstractDao<Trait, Long> {

	public static final String TABLENAME = "trait";

	/**
	 * Properties of entity Trait.<br/>
	 * Can be used for QueryBuilder and for referencing column names.
	 */
	public static class Properties {
		public final static Property Id = new Property(0, Long.class, "id", true, "_id");
		public final static Property Name = new Property(1, String.class, "name", false, "NAME");
		public final static Property Polarity = new Property(2, int.class, "polarity", false, "POLARITY");
		public final static Property Gender = new Property(3, Integer.class, "gender", false, "GENDER");
		public final static Property IsCustom = new Property(4, Boolean.class, "isCustom", false, "IS_CUSTOM");
		public final static Property CreatedAt = new Property(5, java.util.Date.class, "createdAt", false, "CREATED_AT");
		public final static Property UpdatedAt = new Property(6, java.util.Date.class, "updatedAt", false, "UPDATED_AT");
	};

	public TraitDao(DaoConfig config) {
		super(config);
	}

	public TraitDao(DaoConfig config, DaoSession daoSession) {
		super(config, daoSession);
	}

	/** Creates the underlying database table. */
	public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
		String constraint = ifNotExists ? "IF NOT EXISTS " : "";
		db.execSQL("CREATE TABLE " + constraint + "'trait' (" + //
				"'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
				"'NAME' TEXT NOT NULL ," + // 1: name
				"'POLARITY' INTEGER NOT NULL ," + // 2: polarity
				"'GENDER' INTEGER," + // 3: gender
				"'IS_CUSTOM' INTEGER," + // 4: isCustom
				"'CREATED_AT' INTEGER," + // 5: createdAt
				"'UPDATED_AT' INTEGER);"); // 6: updatedAt
	}

	/** Drops the underlying database table. */
	public static void dropTable(SQLiteDatabase db, boolean ifExists) {
		String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'trait'";
		db.execSQL(sql);
	}

	/** @inheritdoc */
	@Override
	protected void bindValues(SQLiteStatement stmt, Trait entity) {
		stmt.clearBindings();

		Long id = entity.getId();
		if (id != null) {
			stmt.bindLong(1, id);
		}
		stmt.bindString(2, entity.getName());
		stmt.bindLong(3, entity.getPolarity());

		Integer gender = entity.getGender();
		if (gender != null) {
			stmt.bindLong(4, gender);
		}

		Boolean isCustom = entity.getIsCustom();
		if (isCustom != null) {
			stmt.bindLong(5, isCustom ? 1l : 0l);
		}

		java.util.Date createdAt = entity.getCreatedAt();
		if (createdAt != null) {
			stmt.bindLong(6, createdAt.getTime());
		}

		java.util.Date updatedAt = entity.getUpdatedAt();
		if (updatedAt != null) {
			stmt.bindLong(7, updatedAt.getTime());
		}
	}

	/** @inheritdoc */
	@Override
	public Long readKey(Cursor cursor, int offset) {
		return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
	}

	/** @inheritdoc */
	@Override
	public Trait readEntity(Cursor cursor, int offset) {
		Trait entity = new Trait( //
				cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
				cursor.getString(offset + 1), // name
				cursor.getInt(offset + 2), // polarity
				cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // gender
				cursor.isNull(offset + 4) ? null : cursor.getShort(offset + 4) != 0, // isCustom
				cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)), // createdAt
				cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)) // updatedAt
		);
		return entity;
	}

	/** @inheritdoc */
	@Override
	public void readEntity(Cursor cursor, Trait entity, int offset) {
		entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
		entity.setName(cursor.getString(offset + 1));
		entity.setPolarity(cursor.getInt(offset + 2));
		entity.setGender(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
		entity.setIsCustom(cursor.isNull(offset + 4) ? null : cursor.getShort(offset + 4) != 0);
		entity.setCreatedAt(cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)));
		entity.setUpdatedAt(cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)));
	}

	/** @inheritdoc */
	@Override
	protected Long updateKeyAfterInsert(Trait entity, long rowId) {
		entity.setId(rowId);
		return rowId;
	}

	/** @inheritdoc */
	@Override
	public Long getKey(Trait entity) {
		if (entity != null) {
			return entity.getId();
		} else {
			return null;
		}
	}

	/** @inheritdoc */
	@Override
	protected boolean isEntityUpdateable() {
		return true;
	}

}
