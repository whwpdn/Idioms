package jewoo.idioms;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class MainActivity extends AppCompatActivity{

    //
    private static final String SAMPLE_PATH = "Samples";

    //private SharedPreferences spf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        checkFirstRun();

        //
        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();
        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Idioms");
        spec.setContent(R.id.tabIdioms);
        spec.setIndicator("Idioms");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Patterns");
        spec.setContent(R.id.tabPatterns);
        spec.setIndicator("Patterns");
        host.addTab(spec);

        //tab 3
        spec = host.newTabSpec("Listening");
        spec.setContent(R.id.tabListening);
        spec.setIndicator("Listening");
        host.addTab(spec);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // copy sample files
    private void CopyAssets() {
        AssetManager assetManager = getResources().getAssets();//getAssets();

        String[] files = null;
        String mkdir = null ;
        try {
            files = assetManager.list(SAMPLE_PATH);

        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        for(int i=0; i<files.length; i++) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(SAMPLE_PATH+"/"+files[i]);
                //in = assetManager.open(files[i]);

                //폴더생성
                String str = Environment.getExternalStorageState();
                if ( str.equals(Environment.MEDIA_MOUNTED)) {
                    mkdir = getExternalFilesDir(null).getAbsolutePath()+"/"+SAMPLE_PATH+"/";
                    //mkdir = "/sdcard/elecgal/templet/" ;
                } else {
                    Environment.getExternalStorageDirectory();
                }
                File mpath = new File(mkdir);
                if(! mpath.isDirectory()) {
                    mpath.mkdirs();
                }
                //

                //out = new FileOutputStream("/sdcard/elecgal/templet/" + files[i]);
                out = new FileOutputStream(mkdir + files[i]);
                copyFile(in, out);
            } catch(Exception e) {
                Log.e("tag", e.getMessage());
            }
            finally{
                if(in != null){
                    try{
                        in.close();
                        in = null;
                    }
                    catch(IOException e){

                    }
                }
                if(out != null){
                    try{
                        out.flush();
                        out.close();
                        out = null;
                    }catch(IOException e){

                    }
                }
            }

        }
    }

    // copy file method
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }

    }

    //
    private void checkFirstRun() {

        final String PREFS_NAME = "installed";
        final String PREF_VERSION_CODE_KEY = "4";
        final int DOESNT_EXIST = -1;


        // Get current version code
        int currentVersionCode = 0;
        try {
            currentVersionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            // handle exception
            e.printStackTrace();
            return;
        }

        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {

            // This is just a normal run
            return;

        } else if (savedVersionCode == DOESNT_EXIST) {

            // TODO This is a new install (or the user cleared the shared preferences)
            CopyAssets();

        } else if (currentVersionCode > savedVersionCode) {

            // TODO This is an upgrade
            CopyAssets();

        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).commit();

    }
}
