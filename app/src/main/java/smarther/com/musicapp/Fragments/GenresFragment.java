package smarther.com.musicapp.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import smarther.com.musicapp.Adapter.ArtistsAdapter;
import smarther.com.musicapp.Adapter.GenresAdapter;
import smarther.com.musicapp.Model.GenresModel;
import smarther.com.musicapp.R;
import smarther.com.musicapp.Utils.MusicUtils;

public class GenresFragment extends Fragment {
    RecyclerView genres_recycler;

    public GenresFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_genres, container, false);
        genres_recycler = (RecyclerView)view.findViewById(R.id.genres_recycler);
        genres_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false));
        genres_recycler.setHasFixedSize(true);
        genres_recycler.setNestedScrollingEnabled(false);
        GenresAdapter genresAdapter=new GenresAdapter(getActivity(),makePlaylistCursor(getActivity()));
        genres_recycler.setAdapter(genresAdapter);
        return view;
    }
    public  static List<GenresModel>   makePlaylistCursor(final Context context)
    {
        List<GenresModel>genresModelList=new ArrayList<>();
        Cursor cursor=context.getContentResolver().query(MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI, new String[]{BaseColumns._ID, MediaStore.Audio.GenresColumns.NAME}, null, null, MediaStore.Audio.Genres.DEFAULT_SORT_ORDER);
        if (cursor != null && cursor.moveToFirst()) {
            do {

                final long id = cursor.getLong(0);

                final String name = cursor.getString(1);

                final int songCount = MusicUtils.getSongCountForGenres(context, id);

                if (songCount > 0) {
                    final GenresModel playlist = new GenresModel(id, name, songCount);
                    genresModelList.add(playlist);
                } else {

                }

            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        return genresModelList;
    }
}
