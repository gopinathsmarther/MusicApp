package smarther.com.musicapp.Fragments;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import smarther.com.musicapp.Adapter.FolderAdapter;
import smarther.com.musicapp.Adapter.SongsAdapter;
import smarther.com.musicapp.Model.FolderModel;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Utils.Global;
import smarther.com.musicapp.Utils.SessionManager;

public class FoldersFragment extends Fragment {

    public static File file;
    public static List<FolderModel> fileList;
    public static File list[] = null;
    public static String root_sd = "";
    public static RecyclerView folder_recycler;
    SessionManager sessionManager;
    public static FoldersFragment foldersFragment;
    public FoldersFragment() {

    }
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foldersFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_folders, container, false);
        folder_recycler = (RecyclerView)view.findViewById(R.id.folder_recycler);
        folder_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false));
        folder_recycler.setHasFixedSize(true);
        folder_recycler.setNestedScrollingEnabled(false);
        fileList = new ArrayList<>();
        sessionManager=new SessionManager(getActivity());
        root_sd = Environment.getExternalStorageDirectory().toString();

        file = new File(root_sd);
//        System.out.println("aji root_sd "+root_sd);
//        System.out.println("aji file "+file);
        load_folder(file);
        return view;
    }
    public static void load_folder(File file)
    {
        list = file.listFiles(new Global.AudioFilter());
        fileList.clear();
        for (int i = 0; i < list.length; i++) {
            String name=list[i].getName();
            int count = getAudioFileCount(list[i].getAbsolutePath());
            System.out.println("aji folder count "+count);
            FolderModel folderModel=new FolderModel();
            folderModel.setName(name);
            folderModel.setSongs(String.valueOf(count));
            folderModel.setPath(list[i].getAbsolutePath());
//            System.out.println("aji name "+name);
//            System.out.println("aji list[i].getAbsolutePath() "+list[i].getAbsolutePath());


            if (count != 0)
                fileList.add(folderModel);
        }
        FolderAdapter songsAdapter=new FolderAdapter(foldersFragment.getActivity(),fileList);
        folder_recycler.setAdapter(songsAdapter);
    }
    public static int getAudioFileCount(final String dirPath) {

        String selection = MediaStore.Audio.Media.DATA + " like ?";
        String[] projection = {MediaStore.Audio.Media.DATA};
        String[] selectionArgs = {dirPath + "%"};
        Cursor cursor = foldersFragment.getActivity().getApplicationContext().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);

        if (cursor != null){
            int temp=cursor.getCount();
            cursor.close();
            return temp;
        }
        else {
            return 0;
        }
    }


}
