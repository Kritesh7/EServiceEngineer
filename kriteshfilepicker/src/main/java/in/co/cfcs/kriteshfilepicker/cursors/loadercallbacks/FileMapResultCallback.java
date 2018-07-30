package in.co.cfcs.kriteshfilepicker.cursors.loadercallbacks;

import java.util.List;
import java.util.Map;

import in.co.cfcs.kriteshfilepicker.models.Document;
import in.co.cfcs.kriteshfilepicker.models.FileType;


public interface FileMapResultCallback {
    void onResultCallback(Map<FileType, List<Document>> files);
}

