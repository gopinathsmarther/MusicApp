package smarther.com.musicapp.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import smarther.com.musicapp.Fragments.AlbumFragment;
import smarther.com.musicapp.Fragments.ArtistsFragment;
import smarther.com.musicapp.Fragments.FoldersFragment;
import smarther.com.musicapp.Fragments.GenresFragment;
import smarther.com.musicapp.Fragments.PlaylistFragment;
import smarther.com.musicapp.Fragments.SongsFragment;
import smarther.com.musicapp.Fragments.SuggestedFragment;

public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

            case 0: return new SuggestedFragment();
            case 1: return new SongsFragment();
            case 2: return new AlbumFragment();
            case 3: return new ArtistsFragment();
            case 4: return new PlaylistFragment();
            case 5: return new FoldersFragment();
            case 6: return new GenresFragment();
            default: return new SuggestedFragment();
            }
        }

        @Override
        public int getCount()
        {
            return 7;
        }       
    }
