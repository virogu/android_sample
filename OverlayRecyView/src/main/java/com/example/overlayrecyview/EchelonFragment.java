package com.example.overlayrecyview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by 钉某人
 * github: https://github.com/DingMouRen
 * email: naildingmouren@gmail.com
 * 梯形布局
 */
public class EchelonFragment extends Fragment {
    View item;
    private RecyclerView mRecyclerView;
    private EchelonLayoutManager mLayoutManager;
    private int count = 8;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_echelon, container, false);
        item = inflater.inflate(R.layout.item, container, false);
        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        rootView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                mRecyclerView.getAdapter().notifyDataSetChanged();
                mRecyclerView.smoothScrollToPosition(count - 1);
            }
        });
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        mLayoutManager = new EchelonLayoutManager(getContext());
        //mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(new MyAdapter());

    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.textView.setText(position + 1 + "");
        }


        @Override
        public int getItemCount() {
            return count;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ConstraintLayout layout;
            TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                layout = itemView.findViewById(R.id.item_contain);
                textView = itemView.findViewById(R.id.textView3);
            }
        }
    }

}
