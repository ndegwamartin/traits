package com.olympuskid.tr8ts.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.olympuskid.tr8ts.R;
import com.olympuskid.tr8ts.db.dao.DaoMaster;
import com.olympuskid.tr8ts.db.dao.DaoMaster.DevOpenHelper;
import com.olympuskid.tr8ts.db.dao.DaoSession;
import com.olympuskid.tr8ts.db.dao.TraitDao;
import com.olympuskid.tr8ts.db.dao.TraitDao.Properties;
import com.olympuskid.tr8ts.db.models.Trait;
import com.olympuskid.tr8ts.models.GeneratedTextItem;
import com.olympuskid.tr8ts.utils.objects.Tr8;

public class TraitManager {
	private SQLiteDatabase db;

	private DaoMaster daoMaster;
	private DaoSession daoSession;
	private TraitDao traitDao;
	private int tokenCount = 0;

	private boolean isGenerating;
	private int progressBarStatus = 0;
	Context ctx;

	List<String> nouns;
	List<String> pronouns;

	private final Locale locale = Locale.US;

	private Integer cStat = 0;

	private Integer cTraitsCount = 0;

	public static enum Polarity {

		POSITIVE, NEGATIVE, CUSTOM, ALL

	}

	public int getTokenCount() {
		return tokenCount;
	}

	public void setTokenCount(int tokenCount) {
		this.tokenCount = tokenCount;
	}

	public TraitManager(Context context, boolean populateNouns) {
		initializeDB(context);

		// rating feedback management
		nouns = new ArrayList<String>();
		nouns.add("person");
		nouns.add("human being");
		nouns.add("individual");
		nouns.add("being");
		nouns.add("character");
		nouns.add("human");
		nouns.add("fellow");
		nouns.add("soul");
		nouns.add("mortal");
		nouns.add("creature");
		nouns.add("homo sapien");
		nouns.add("earthling");
		nouns.add("biped");
		nouns.add("spirit");

		pronouns = new ArrayList<String>();

		pronouns.add("acutely");
		pronouns.add("absolutely");
		pronouns.add("admittedly");
		pronouns.add("authentically");
		pronouns.add("awfully");
		pronouns.add("categorically");
		pronouns.add("candidly");
		pronouns.add("conceivably");
		pronouns.add("conclusively");
		pronouns.add("completely");
		pronouns.add("entirely");
		pronouns.add("extreemly");
		pronouns.add("exceedingly");
		pronouns.add("exceptionally");
		pronouns.add("extraordinarily");
		pronouns.add("authentically");
		pronouns.add("genuinely");
		pronouns.add("honestly");
		pronouns.add("highly");
		pronouns.add("hugely");
		pronouns.add("unquestionably");
		pronouns.add("veritably");
		pronouns.add("undoubtedly");
		pronouns.add("immensely");
		pronouns.add("indubitably");
		pronouns.add("verily");
		pronouns.add("literaly");
		pronouns.add("sincerely");
		pronouns.add("unfeignedly");
		pronouns.add("incontrovertibly");
		pronouns.add("incontestably");
		pronouns.add("irrefutably");
		pronouns.add("incontestably");
		pronouns.add("indisputably");
		pronouns.add("intensely");
		pronouns.add("markedly");
		pronouns.add("noticably");
		pronouns.add("mightily");
		pronouns.add("obviously");
		pronouns.add("pretty");
		pronouns.add("profoundly");
		pronouns.add("real");
		pronouns.add("really");
		pronouns.add("remarkably");
		pronouns.add("totally");
		pronouns.add("tremendously");
		pronouns.add("truly");
		pronouns.add("truthfully");
		pronouns.add("frankly");
		pronouns.add("certifiably");
		pronouns.add("perfectly");
		pronouns.add("plainly");
		pronouns.add("significantly");
		pronouns.add("sincerely");
		pronouns.add("supremely");
		pronouns.add("thoroughly");
		pronouns.add("very");
		pronouns.add("virtually");
		pronouns.add("unmistakably");
		pronouns.add("unequivocally");
		pronouns.add("sheerly");
		pronouns.add("transparently");
		pronouns.add("utterly");
		pronouns.add("undebatably");
		pronouns.add("undeniably");
		pronouns.add("unassailably");
		pronouns.add("unmistakably");
		pronouns.add("unumbiguously");

	}

	private void initializeDB(Context context) {
		initDB(context);
	}

	private void initDB(Context context) {
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "traits-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		traitDao = daoSession.getTraitDao();
		ctx = context;
	}

	public void clearDb() {

		traitDao.deleteAll(); // clear db;
	}

	public void closeDB() {

		if (db != null) {
			db.close();
		}
	}

	public int setAsyncTrait(Tr8 tr8)

	{
		long traitsCount;
		long tokenCount;
		try {
			addTrait(tr8, false);
			traitsCount = this.getTraitsCount();
			tokenCount = this.getTokenCount();
			progressBarStatus = traitsCount != tokenCount && traitsCount < tokenCount ? (Math.round((traitsCount * 100) / tokenCount)) : 100;

			return progressBarStatus;

		} catch (Exception e) {

			Log.d("TraitManager :: setAsyncTrait", e.getMessage());

			return 0;

		}
	}

	public int setTraits(List<String> tokens) {
		long traitsCount;
		long tokenCount;
		if (!isGenerating) {
			tokenCount = tokens.size();
			for (int i = 0; i < tokens.size(); i++) {
				// addTrait(tokens.get(i).toString());
				traitsCount = this.getTraitsCount();
				tokenCount = this.getTokenCount() * 5;
				progressBarStatus = traitsCount != tokenCount && traitsCount < tokenCount ? (Math.round(traitsCount / tokenCount) * 100) : 100;
			}

		}

		return progressBarStatus;
	}

	// A gender of 3 to signify no gender??
	public void addTrait(Tr8 trai8, boolean isCustom) {

		try {
			Trait trait = new Trait(null, trai8.getName(), trai8.getPolarity(), 3, isCustom, new Date(), null);
			traitDao.insert(trait);
			Log.d("Traits App", "Inserted new trait " + trai8.getName() + ", ID: " + trait.getId());
		} catch (Exception e) {
			Log.d("444 Traits App", e.getMessage());
		}

	}

	public List<Trait> getTraits() {
		return traitDao.queryBuilder().orderAsc(Properties.Id).list();

	}

	public int genrateAsync(String token)

	{
		long traitsCount;
		long tokenCount;
		// addTrait(token);
		traitsCount = this.getTraitsCount();
		tokenCount = this.getTokenCount();
		progressBarStatus = traitsCount != tokenCount && traitsCount < tokenCount ? (Math.round((traitsCount * 100) / tokenCount)) : 100;

		return progressBarStatus;

	}

	public GeneratedTextItem generate(String name, Polarity polarity, boolean isCustom) {
		cStat = 0;
		cTraitsCount = 0;
		if (name == null || name.trim().length() == 0) {

			return new GeneratedTextItem(false, getStringResource(R.string.please_type_a_name_first_in_box));
		}

		List<String> traitList = new ArrayList<String>();
		char[] chars = name.toUpperCase(locale).toCharArray();

		String theTrait = "";
		for (Character c : chars) {
			if (c != ' ') {
				c = Character.toLowerCase(c);
				boolean isX = c.equals('x');
				int offsetStart = !isX ? 0 : 1;
				int offsetEnd = !isX ? 1 : 2;

				theTrait = getTrait(c, polarity, isCustom);

				traitList.add(theTrait != null ? theTrait.substring(offsetStart, offsetEnd).toUpperCase(Locale.US) + "-" + theTrait.substring(!isX ? 1 : 2)
						: String.valueOf(c));

			} else {
				traitList.add(" ");
			}
		}

		// String urTraits = "\"" + name + "\" means you are: \n \n";
		String urTraits = name.toUpperCase(locale) + "\n \n";
		for (String trait : traitList) {
			urTraits = urTraits + trait + "\n";
		}

		return new GeneratedTextItem(true, urTraits);
	}

	public String getTrait(Character c, Polarity polarity, boolean isCustom) {

		Trait trait = null;
		c = Character.toLowerCase(c);
		String searchKeyPrefix = c.equals('x') ? "ex" : String.valueOf(c);

		if (polarity != Polarity.ALL) {
			if (isCustom) {
				trait = traitDao.queryBuilder().where(Properties.Name.like(searchKeyPrefix + "%"))
						.where(Properties.Polarity.eq(polarity == Polarity.POSITIVE ? "1" : "-1")).where(Properties.IsCustom.eq(true)).orderRaw("RANDOM()")
						.limit(1).unique();
			} else {
				trait = traitDao.queryBuilder().where(Properties.Name.like(searchKeyPrefix + "%"))
						.where(Properties.Polarity.eq(polarity == Polarity.POSITIVE ? "1" : "-1")).orderRaw("RANDOM()").limit(1).unique();

			}

			/*
			 * QueryBuilder<Trait> qb = traitDao.queryBuilder(); qb.where(Properties.Name.like(searchKeyPrefix + "%"), qb.and(Properties.Polarity.eq(polarity ==
			 * Polarity.POSITIVE ? "1" : "-1"),Properties.Name.like(searchKeyPrefix + "%")), qb.or(Properties.CreatedAt.gt(1970),
			 * qb.and(Properties.UpdatedAt.eq(1970), Properties.IsCustom.ge(10))));
			 */

		} else {

			if (isCustom) {

				trait = traitDao.queryBuilder().where(Properties.Name.like(searchKeyPrefix + "%")).where(Properties.IsCustom.eq(true)).orderRaw("RANDOM()")
						.limit(1).unique();
			} else {
				trait = traitDao.queryBuilder().where(Properties.Name.like(searchKeyPrefix + "%")).orderRaw("RANDOM()").limit(1).unique();
			}
		}

		if (trait != null) {
			incrementCharStat(trait);
		}
		return trait != null ? trait.getName() : null;

	}

	private void incrementCharStat(Trait trait) {
		if (trait.getPolarity() != 0 && trait.getPolarity() == 1) {
			cStat++;
		}

		cTraitsCount++;

	}

	public long getTraitsCount() {

		return traitDao.queryBuilder().count();
	}

	public Integer getCharacterStat() {
		Integer percentage = Math.round(((Float.valueOf(cStat.toString()) / Float.valueOf(cTraitsCount.toString())) * 100));// * 10) / 10.0;
		return percentage;
	}

	public List<String> getNouns() {
		return nouns;
	}

	public List<String> getPronouns() {
		return pronouns;
	}

	public String getVerb(boolean isGoodAverage) {
		Trait trait = traitDao.queryBuilder().where(Properties.Polarity.eq(isGoodAverage ? "1" : "-1")).orderRaw("RANDOM()").limit(1).unique();
		return trait.getName();
	}

	private String getStringResource(int id) {
		return ctx.getResources().getString(id);
	}

	public List<Trait> getCustomTraits(String searchKeyPrefix) {
		if (searchKeyPrefix != null && searchKeyPrefix.trim().length() > 0) {
			return traitDao.queryBuilder().where(Properties.Name.like(searchKeyPrefix.trim() + "%")).where(Properties.IsCustom.eq(true))
					.orderAsc(Properties.Name).list();
		} else {
			return traitDao.queryBuilder().where(Properties.IsCustom.eq(true)).orderAsc(Properties.Name).list();
		}
	}

	public boolean deleteTrait(Long id) {
		try {
			traitDao.deleteByKey(id);

			return true;
		} catch (Exception e) {
			Log.e("Failed while trying to delete trait", e.getMessage());
			return false;
		}
	}

	public boolean updateTrait(Trait trait) {
		try {
			traitDao.update(trait);

			return true;
		} catch (Exception e) {
			Log.e("Failed while trying to update trait", e.getMessage());
			return false;
		}
	}

	public Trait getTrait(Long id) {
		return traitDao.load(id);
	}

	public boolean traitAlreadyExists(Tr8 trait) {

		return traitDao.queryBuilder().where(Properties.Name.eq(trait.getName())).where(Properties.Polarity.eq(trait.getPolarity())).unique() == null ? false
				: true;
	}

}
