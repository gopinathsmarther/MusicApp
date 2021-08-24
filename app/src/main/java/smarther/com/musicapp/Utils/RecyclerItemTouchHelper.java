package smarther.com.musicapp.Utils;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import smarther.com.musicapp.Adapter.QueueSongAdapter;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
            private QueueSongAdapter mAdapter;

            public RecyclerItemTouchHelper(QueueSongAdapter adapter){
                super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0);
                mAdapter = adapter;
            }

            @Override
            public boolean onMove(final RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                mAdapter.onMove(recyclerView,viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            mAdapter.onSwiped(viewHolder, direction);
    }
        }