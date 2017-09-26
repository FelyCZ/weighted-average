package cz.fely.weightedaverage.backup;

import android.app.backup.BackupAgentHelper;
import android.app.backup.FileBackupHelper;
import android.app.backup.SharedPreferencesBackupHelper;
import android.preference.PreferenceManager;

import java.io.File;

import cz.fely.weightedaverage.activities.MainActivity;

import static cz.fely.weightedaverage.db.Database.DB_NAME;

public class BackupAgent extends BackupAgentHelper {

    static final String File_Name_Of_Prefrences = PreferenceManager.getDefaultSharedPreferencesName(MainActivity.context);
    SharedPreferencesBackupHelper helper = new SharedPreferencesBackupHelper(this, File_Name_Of_Prefrences);

    static final String PREFS_BACKUP_KEY = "prefs";
    static final String DB_BACKUP_KEY = "db";

    @Override
    public void onCreate() {
        addHelper(PREFS_BACKUP_KEY, helper);
        addHelper(DB_BACKUP_KEY, new FileBackupHelper(this, DB_NAME));
    }

    @Override
    public File getFilesDir(){
        File path = getDatabasePath(DB_NAME);
        return path.getParentFile();
    }
}
