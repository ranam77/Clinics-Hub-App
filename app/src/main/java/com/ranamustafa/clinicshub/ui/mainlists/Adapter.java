
package com.ranamustafa.clinicshub.ui.mainlists;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.ranamustafa.clinicshub.R;
import com.ranamustafa.clinicshub.data.api.ApiClient;
import com.ranamustafa.clinicshub.data.model.ListModel;
import com.ranamustafa.clinicshub.ui.main.MainActivity;
import com.ranamustafa.clinicshub.utils.AppLogger;

import java.util.Objects;

public class Adapter extends ListAdapter<ListModel, Adapter.ProjectViewHolder> {
    private final OnProjectClickListener listener;
    private static final String TAG = Adapter.class.getSimpleName();
    public interface OnProjectClickListener {
        void onProjectClick(ListModel project);
    }
    private static final DiffUtil.ItemCallback<ListModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<ListModel>() {
                @Override
                public boolean areItemsTheSame(@NonNull ListModel oldItem,@NonNull ListModel newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull ListModel oldItem,@NonNull ListModel newItem) {
                    return Objects.equals(
                            oldItem.getName(), newItem.getName())
                            && Objects.equals(
                            oldItem.getImageUrl(), newItem.getImageUrl());
                }
            };

    public Adapter(OnProjectClickListener listener ) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listmodel_item, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder,int position) {
        ListModel project = getItem(position);
        holder.bind(project, listener);
    }

    static class ProjectViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvProjectName;
        private final ImageView ivProject;
        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProjectName = itemView.findViewById(R.id.tvProjectName);
            ivProject = itemView.findViewById(R.id.ivProject);
        }

        void bind(ListModel project, OnProjectClickListener listener) {
            try {
                tvProjectName.setText(project.getName());
                String imageUrl = ApiClient.IMAGE_BASE_URL + project.getImageUrl();
                Glide.with(ivProject)
                        .load(imageUrl)
                        .centerCrop()
                        .into(ivProject);
                itemView.setOnClickListener(v -> listener.onProjectClick(project));
            }catch (Exception e)
            {
                AppLogger.logError(TAG, "bind",e);
            }
        }
    }
}
