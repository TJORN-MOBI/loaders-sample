# Arbitrary loaders made simple
This sample app shows how to use [Loaders library][LoaderLibrary]. The main sample code is located in [MainActivity](app/src/main/java/mobi/tjorn/loadertest/MainActivity.java) class.

The problem with tasks is that they are not attached to an Android context.  As a result, developers have to write error-prone code to re-attach tasks to context objects on device configuration changes.  Loaders, on the other hand, automatically re-attached to their context objects.  But implementing a loader is not a simple task.  

[Loaders library][LoaderLibrary] attempts to simplify the task of implementing loaders by adding lifecycle management to core Android loader classes.  The code snipped below shows that incorporating a loader into an application is as simple as using an AsyncTask.
```java
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
```
TestLoader is defined in the same file:
```java
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
```

[LoaderLibrary]: https://github.com/TJORN-MOBI/loaders "LoadersLibrary"
