package mobi.tjorn.loadertest;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

import mobi.tjorn.content.common.SimpleResult;
import mobi.tjorn.support.content.loaders.SimpleResultTaskLoader;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<SimpleResult<String>>, View.OnClickListener {
    private static final int LOADER_TEXT = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.loaderResult);
        findViewById(R.id.initLoader).setOnClickListener(this);
        findViewById(R.id.restartLoader).setOnClickListener(this);
        findViewById(R.id.destroyLoader).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "initLoader " + LOADER_TEXT + " BEGIN");
        textView.setText(R.string.loading);
        getSupportLoaderManager().initLoader(LOADER_TEXT, null, this);
        Log.d(TAG, "initLoader " + LOADER_TEXT + " END");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.initLoader:
                Log.d(TAG, "initLoader");
                getSupportLoaderManager().initLoader(LOADER_TEXT, null, this);
                break;
            case R.id.restartLoader:
                Log.d(TAG, "restartLoader");
                getSupportLoaderManager().restartLoader(LOADER_TEXT, null, this);
                break;
            case R.id.destroyLoader:
                Log.d(TAG, "destroyLoader");
                textView.setText(R.string.loader_destroyed);
                getSupportLoaderManager().destroyLoader(LOADER_TEXT);
                break;
        }
    }

    @Override
    public Loader<SimpleResult<String>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: " + id);
        switch (id) {
            case LOADER_TEXT:
                textView.setText(R.string.loading);
                return new TestLoader(this);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<SimpleResult<String>> loader, SimpleResult<String> data) {
        Log.d(TAG, "onLoadFinished: " + loader.getId());
        switch (loader.getId()) {
            case LOADER_TEXT:
                if (data.hasError()) {
                    textView.setText(data.getError().getMessage());
                } else {
                    textView.setText(data.getData());
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<SimpleResult<String>> loader) {
        Log.d(TAG, "onLoaderReset: " + loader.getId());
        switch (loader.getId()) {
            case LOADER_TEXT:
                textView.setText(R.string.loader_reset);
                break;
        }
    }

    private static class TestLoader extends SimpleResultTaskLoader<String> {

        TestLoader(Context context) {
            super(context);
        }

        @Override
        public SimpleResult<String> loadInBackground() {
            try {
                Thread.currentThread().sleep(5000);
                return new SimpleResult<String>(new Date().toString());
            } catch (InterruptedException e) {
                return new SimpleResult<String>(e);
            }
        }
    }
}
