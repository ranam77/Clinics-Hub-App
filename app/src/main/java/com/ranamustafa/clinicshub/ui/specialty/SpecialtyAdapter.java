
package com.ranamustafa.clinicshub.ui.specialty;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ranamustafa.clinicshub.R;
import com.ranamustafa.clinicshub.data.api.ApiClient;
import com.ranamustafa.clinicshub.data.model.SpecialtyModel;
import com.ranamustafa.clinicshub.ui.mainlists.ViewModel;
import com.ranamustafa.clinicshub.utils.AppLogger;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class SpecialtyAdapter extends ListAdapter<SpecialtyModel, SpecialtyAdapter.SpecialtyViewHolder> {
    private final OnPickListener listener;
    private static final String TAG = SpecialtyAdapter.class.getSimpleName();
    public interface OnPickListener {
        void OnClick(SpecialtyModel project);
    }
    private static final DiffUtil.ItemCallback<SpecialtyModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<SpecialtyModel>() {
                @Override
                public boolean areItemsTheSame(@NonNull SpecialtyModel oldItem,@NonNull SpecialtyModel newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull SpecialtyModel oldItem,@NonNull SpecialtyModel newItem) {
                    return Objects.equals(
                            oldItem.getName(), newItem.getName())
                            && Objects.equals(
                            oldItem.getImageUrl(), newItem.getImageUrl());
                }
            };

    public SpecialtyAdapter(OnPickListener listener ) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public SpecialtyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listmodel_item, parent, false);
        return new SpecialtyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecialtyViewHolder holder,int position) {
        SpecialtyModel project = getItem(position);
        holder.bind(project, listener);
    }

    static class SpecialtyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvProjectName;
        private final ImageView ivProject;
        public SpecialtyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProjectName = itemView.findViewById(R.id.tvProjectName);
            ivProject = itemView.findViewById(R.id.ivProject);
        }

        void bind(SpecialtyModel specialty, OnPickListener listener) {
            try{
                tvProjectName.setText(specialty.getName());
                String imageUrl = ApiClient.IMAGE_BASE_URL + specialty.getImageUrl();
                Glide.with(ivProject)
                        .load(imageUrl)
                        .centerCrop()
                        .into(ivProject);

                itemView.setOnClickListener(v ->listener.OnClick(specialty));
            }
            catch (Exception e)
            {
                AppLogger.logError(TAG, "bind",e);
            }

        }
    }
}
