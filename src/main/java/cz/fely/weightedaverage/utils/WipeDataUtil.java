package cz.fely.weightedaverage.utils;

import android.app.Application;

import java.io.File;

final public class WipeDataUtil extends Application {

    private static WipeDataUtil instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static WipeDataUtil getInstance() {
        return instance;
    }

    public void clearApplicationData() {
        File cacheDirectory = getCacheDir();
        File applicationDirectory = new File(cacheDirectory.getParent());
        if (applicationDirectory.exists()) {
            String[] fileNames = applicationDirectory.list();
            for (String fileName : fileNames) {
                if (!fileName.equals("lib")) {
                    deleteFile(new File(applicationDirectory, fileName));
                }
            }
        }
        Runtime.getRuntime().exit(0);
    }

    public static boolean deleteFile(File file) {
        boolean deletedAll = true;
        if (file != null) {
            if (file.isDirectory()) {
                String[] list = file.list();
                for (int i = 0; i < list.length; i++) {
                    deletedAll = deleteFile(new File(file, list[i])) && deletedAll;
                }
            } else {
                deletedAll = file.delete();
            }
        }
        return deletedAll;
    }
}
