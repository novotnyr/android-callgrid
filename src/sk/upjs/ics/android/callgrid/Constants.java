package sk.upjs.ics.android.callgrid;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.os.Bundle;

public interface Constants {
	public static final String[] NO_PROJECTION = null;
	
	public static final String[] ALL_COLUMNS = null;
	
	public static final String NO_SELECTION = null;

	public static final String[] NO_SELECTION_ARGS = null;

	public static final String NO_SORT_ORDER = null;
	
	public static final String NO_GROUP_BY = null;
	
	public static final String NO_HAVING = null;
	
	public static final String AUTOGENERATED_ID = null;
	
	public static final CursorFactory DEFAULT_CURSOR_FACTORY = null;
	
	public static final String NO_NULL_COLUMN_HACK = null;

	public static final Cursor NO_CURSOR = null;
	
	public static final Bundle NO_BUNDLE = null;
}
