
package com.ranamustafa.clinicshub.ui.doctors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.ranamustafa.clinicshub.R;
import com.ranamustafa.clinicshub.data.api.ApiClient;
import com.ranamustafa.clinicshub.data.model.DoctorModel;
import com.ranamustafa.clinicshub.utils.AppLogger;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class doctorAdapter extends ListAdapter<DoctorModel, doctorAdapter.DoctorViewHolder> {
    private final OnProjectClickListener listener;
    private static final String TAG = doctorAdapter.class.getSimpleName();
    public interface OnProjectClickListener {
        void onProjectClick(DoctorModel project);
    }
    private static final DiffUtil.ItemCallback<DoctorModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<DoctorModel>() {
                @Override
                public boolean areItemsTheSame(@NonNull DoctorModel oldItem,@NonNull DoctorModel newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull DoctorModel oldItem,@NonNull DoctorModel newItem) {
                    return Objects.equals(
                            oldItem.getFirstName(), newItem.getFirstName())
                            && Objects.equals(
                            oldItem.getImageUrl(), newItem.getImageUrl());
                }
            };

    public doctorAdapter(OnProjectClickListener listener ) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_item, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder,int position) {
        DoctorModel project = getItem(position);
        holder.bind(project, listener);
    }

    static class DoctorViewHolder extends RecyclerView.ViewHolder {
        private final TextView doctorName;
        private final ImageView ivdoctor;
        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorName = itemView.findViewById(R.id.doctorName);
            ivdoctor = itemView.findViewById(R.id.ivdoctor);
        }

        void bind(DoctorModel item, OnProjectClickListener listener) {
            try{
                String text = Stream.of(item.getFirstName(), item.getLastName())
                        .filter(name -> name != null && !name.trim().isEmpty())
                        .collect(Collectors.joining(" "));
                doctorName.setText(text);
                String imageUrl = ApiClient.IMAGE_BASE_URL + item.getImageUrl();
                Glide.with(ivdoctor)
                        .load(imageUrl)
                        .centerCrop()
                        .into(ivdoctor);

                itemView.setOnClickListener(v ->
                        listener.onProjectClick(item));
            }catch (Exception e)
            {
                AppLogger.logError(TAG, "bind",e);
            }

        }
    }
}
