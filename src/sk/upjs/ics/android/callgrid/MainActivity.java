package sk.upjs.ics.android.callgrid;

import static android.webkit.WebView.SCHEME_TEL;
import static sk.upjs.ics.android.callgrid.Constants.NO_BUNDLE;
import static sk.upjs.ics.android.callgrid.Constants.NO_CURSOR;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements LoaderCallbacks<Cursor>, OnItemClickListener {

	private static final int LOADER_ID_GRIDVIEW = 0;
	private SimpleCursorAdapter adapter;
	private GridView callLogGridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getSupportLoaderManager().initLoader(LOADER_ID_GRIDVIEW, NO_BUNDLE, this);
		
		String[] from = { CallLog.Calls.NUMBER };
	
		int[] to = { R.id.grid_item_text } ;
		adapter = new SimpleCursorAdapter(this, R.layout.grid_item, NO_CURSOR, from, to);
		adapter.setViewBinder(new CallLogCellViewBinder());
		
		callLogGridView = (GridView) findViewById(R.id.callLogGridView);
		callLogGridView.setAdapter(adapter);
		callLogGridView.setOnItemClickListener(this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
		if(loaderId == LOADER_ID_GRIDVIEW) {
			CursorLoader cursorLoader = new CursorLoader(this);
			cursorLoader.setUri(CallLog.Calls.CONTENT_URI);
			cursorLoader.setSortOrder(CallLog.Calls.DATE + " DESC");
			return cursorLoader;
		}
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		adapter.changeCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> cursor) {
		adapter.changeCursor(NO_CURSOR);
	}
	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, CallLog.Calls._ID + "=" + id, null, null);
		if(cursor.moveToNext()) {
			String phoneNumber = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
			Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(SCHEME_TEL + phoneNumber));
			startActivity(callIntent);
		}
		cursor.close();		
	}		
	
	private static class CallLogCellViewBinder implements ViewBinder {
		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			if(view instanceof TextView) {
				TextView textView = (TextView) view;
				
				int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
				Resources resources = view.getResources();
				switch(type) {
				case CallLog.Calls.INCOMING_TYPE:
					textView.setBackgroundColor(resources.getColor(R.color.incomingCallBackground));
					textView.setTextColor(resources.getColor(R.color.incomingCallForeground));
					break;
				case CallLog.Calls.OUTGOING_TYPE:
					textView.setBackgroundColor(resources.getColor(R.color.outgoingCallBackground));
					textView.setTextColor(resources.getColor(R.color.outgoingCallForeground));
					
					break;
				case CallLog.Calls.MISSED_TYPE:
					textView.setBackgroundColor(resources.getColor(R.color.missedCallBackground));
					textView.setTextColor(resources.getColor(R.color.missedCallForeground));
					break;
				}
			}
			return false;
		}
		
	}

}
