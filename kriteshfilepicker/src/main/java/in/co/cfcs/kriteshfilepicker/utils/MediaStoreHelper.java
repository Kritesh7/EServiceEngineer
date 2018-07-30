package in.co.cfcs.kriteshfilepicker.utils;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import java.util.Comparator;
import java.util.List;

import in.co.cfcs.kriteshfilepicker.FilePickerConst;
import in.co.cfcs.kriteshfilepicker.cursors.DocScannerTask;
import in.co.cfcs.kriteshfilepicker.cursors.loadercallbacks.FileMapResultCallback;
import in.co.cfcs.kriteshfilepicker.cursors.loadercallbacks.FileResultCallback;
import in.co.cfcs.kriteshfilepicker.cursors.loadercallbacks.PhotoDirLoaderCallbacks;
import in.co.cfcs.kriteshfilepicker.models.Document;
import in.co.cfcs.kriteshfilepicker.models.FileType;
import in.co.cfcs.kriteshfilepicker.models.PhotoDirectory;


public class MediaStoreHelper {

  public static void getPhotoDirs(FragmentActivity activity, Bundle args, FileResultCallback<PhotoDirectory> resultCallback) {
    if(activity.getSupportLoaderManager().getLoader(FilePickerConst.MEDIA_TYPE_IMAGE)!=null)
      activity.getSupportLoaderManager().restartLoader(FilePickerConst.MEDIA_TYPE_IMAGE, args, new PhotoDirLoaderCallbacks(activity, resultCallback));
    else
      activity.getSupportLoaderManager().initLoader(FilePickerConst.MEDIA_TYPE_IMAGE, args, new PhotoDirLoaderCallbacks(activity, resultCallback));
  }

  public static void getVideoDirs(FragmentActivity activity, Bundle args, FileResultCallback<PhotoDirectory> resultCallback) {
    if(activity.getSupportLoaderManager().getLoader(FilePickerConst.MEDIA_TYPE_VIDEO)!=null)
      activity.getSupportLoaderManager().restartLoader(FilePickerConst.MEDIA_TYPE_VIDEO, args, new PhotoDirLoaderCallbacks(activity, resultCallback));
    else
      activity.getSupportLoaderManager().initLoader(FilePickerConst.MEDIA_TYPE_VIDEO, args, new PhotoDirLoaderCallbacks(activity, resultCallback));
  }

  public static void getDocs(FragmentActivity activity,
                             List<FileType> fileTypes,
                             Comparator<Document> comparator,
                             FileMapResultCallback fileResultCallback)
  {
    new DocScannerTask(activity, fileTypes, comparator, fileResultCallback).execute();
  }
}