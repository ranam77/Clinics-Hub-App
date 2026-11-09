
package com.ranamustafa.clinicshub.ui.projects;

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
import com.ranamustafa.clinicshub.data.model.Project;

import java.util.Objects;

public class ProjectsAdapter
        extends ListAdapter<Project, ProjectsAdapter.ProjectViewHolder> {
    private final OnProjectClickListener listener;
    public interface OnProjectClickListener {
        void onProjectClick(Project project);
    }
    private static final DiffUtil.ItemCallback<Project> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Project>() {
                @Override
                public boolean areItemsTheSame(@NonNull Project oldItem,@NonNull Project newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Project oldItem,@NonNull Project newItem) {
                    return Objects.equals(
                            oldItem.getName(), newItem.getName())
                            && Objects.equals(
                            oldItem.getImageUrl(), newItem.getImageUrl());
                }
            };

    public ProjectsAdapter(OnProjectClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_project, parent, false);

        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder,int position) {
        Project project = getItem(position);
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

        void bind(Project project, OnProjectClickListener listener) {

            tvProjectName.setText(project.getName());
            String imageUrl = ApiClient.IMAGE_BASE_URL + project.getImageUrl();
            Glide.with(ivProject)
                    .load(imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.notfound)
                    .error(R.drawable.notfound)
                    .into(ivProject);

            itemView.setOnClickListener(v ->
                    listener.onProjectClick(project));
        }
    }
}
