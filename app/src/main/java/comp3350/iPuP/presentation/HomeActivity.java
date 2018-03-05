package comp3350.iPuP.presentation;

import comp3350.iPuP.R;
import comp3350.iPuP.application.Main;
import comp3350.iPuP.business.AccessUsers;
import comp3350.iPuP.objects.User;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class HomeActivity extends Activity {

    private AccessUsers accessUsers;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        copyDatabaseToDevice();

        Main.startUp();

        setContentView(R.layout.activity_home);

        accessUsers = new AccessUsers();
        user = null;

        //TODO: Remove following get user code after username edittext added to main activity
        user = accessUsers.createAndGetUser("Amanjyot");

        //TODO: Uncomment following code after username edittext added to main activity
        /*
        EditText editTextUN = (EditText) findViewById(R.id.username);
        final String username = editTextUN.getText().toString();

        editTextUN.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (!hasFocus)
                {
                    // get new user if username not same on focus lost
                    if (user == null || !username.equals(user.getUsername()))
                    {
                        user = accessUsers.getUser(username);
                        Toast.makeText(this, "Loaded User: " + username + "!", Toast.LENGTH_LONG).show();
                        if (user == null)
                        {
                            user = createAndGetUser(username);
                            Toast.makeText(this, "Created User: " + username + "!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
        */
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    private void copyDatabaseToDevice() {
        final String DB_PATH = "db";

        String[] assetNames;
        Context context = getApplicationContext();
        File dataDirectory = context.getDir(DB_PATH, Context.MODE_PRIVATE);
        AssetManager assetManager = getAssets();

        try {

            assetNames = assetManager.list(DB_PATH);
            for (int i = 0; i < assetNames.length; i++) {
                assetNames[i] = DB_PATH + "/" + assetNames[i];
            }

//            System.out.println("First Asset Name: "+assetNames[0]);

            copyAssetsToDirectory(assetNames, dataDirectory);

            Main.setDBPathName(dataDirectory.toString() + "/" + Main.dbName);

        } catch (IOException ioe) {
            Messages.warning(this, "Unable to access application data: " + ioe.getMessage());
        }
    }

    public void copyAssetsToDirectory(String[] assets, File directory) throws IOException {
        AssetManager assetManager = getAssets();

        for (String asset : assets) {
            String[] components = asset.split("/");
            String copyPath = directory.toString() + "/" + components[components.length - 1];
            char[] buffer = new char[1024];
            int count;

            File outFile = new File(copyPath);

            if (!outFile.exists()) {
                InputStreamReader in = new InputStreamReader(assetManager.open(asset));
                FileWriter out = new FileWriter(outFile);

                count = in.read(buffer);
                while (count != -1) {
                    out.write(buffer, 0, count);
                    count = in.read(buffer);
                }

                out.close();
                in.close();
            }
        }
    }

    public void buttonParkerOnClick(View v)
    {
        if (user == null)
        {
            highlightEmptyUsername();
        } else {
            Intent parkerIntent = new Intent(HomeActivity.this, AvailableParkingSpots.class);
            parkerIntent.putExtra("parcel_user", user);
            HomeActivity.this.startActivity(parkerIntent);
        }
    }

    public void buttonHostOnClick(View v)
    {
        if (user == null)
        {
            highlightEmptyUsername();
        } else {
            Intent hostMenuIntent = new Intent(HomeActivity.this, HostMenuActivity.class);
            hostMenuIntent.putExtra("parcel_user", user);
            HomeActivity.this.startActivity(hostMenuIntent);
        }
    }

    //TODO: Finish the following highlightEmptyUsername method
    public void highlightEmptyUsername()
    {
        Toast.makeText(this, "NO USERNAME ENTERED!!!", Toast.LENGTH_LONG).show();
    }

}
