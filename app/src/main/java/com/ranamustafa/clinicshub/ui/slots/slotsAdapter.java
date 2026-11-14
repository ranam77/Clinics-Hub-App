
package com.ranamustafa.clinicshub.ui.slots;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ranamustafa.clinicshub.R;
import com.ranamustafa.clinicshub.data.api.ApiClient;
import com.ranamustafa.clinicshub.data.model.DoctorModel;
import com.ranamustafa.clinicshub.data.model.SlotListItem;
import com.ranamustafa.clinicshub.data.model.SlotModel;
import com.ranamustafa.clinicshub.utils.errors.ErrorHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class slotsAdapter extends
        ListAdapter<SlotListItem, RecyclerView.ViewHolder> {

    private final OnProjectClickListener listener;

    private static final int TYPE_DAY = 0;
    private static final int TYPE_SLOT = 1;

    public interface OnProjectClickListener {
        void onProjectClick(SlotModel item);
    }

    private static final DiffUtil.ItemCallback<SlotListItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<SlotListItem>() {

                @Override
                public boolean areItemsTheSame(
                        @NonNull SlotListItem oldItem,
                        @NonNull SlotListItem newItem) {

                    if (oldItem.getType() != newItem.getType())
                        return false;

                    if (oldItem.getType() == TYPE_DAY)
                        return Objects.equals(oldItem.getDay(), newItem.getDay());

                    return oldItem.getSlot().getId() ==
                            newItem.getSlot().getId();
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull SlotListItem oldItem,
                        @NonNull SlotListItem newItem) {

                    if (oldItem.getType() == TYPE_DAY)
                        return Objects.equals(oldItem.getDay(), newItem.getDay());

                    return Objects.equals(
                            oldItem.getSlot().getStartsAt(),
                            newItem.getSlot().getStartsAt()
                    );
                }
            };

    public slotsAdapter(OnProjectClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_DAY) {
            View view = inflater.inflate(R.layout.slot_day_header, parent, false);

            return new DayViewHolder(view);
        }

        View view = inflater.inflate(
                R.layout.slot_item, parent, false);

        return new SlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecyclerView.ViewHolder holder, int position) {

        SlotListItem item = getItem(position);

        if (holder instanceof DayViewHolder) {
            ((DayViewHolder) holder).bind(item.getDay());
        } else if (holder instanceof SlotViewHolder) {
            ((SlotViewHolder) holder).bind(item.getSlot(), listener);
        }
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvDay;

        DayViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tvDayHeader);
        }

        void bind(String day) {
            tvDay.setText(day);
        }
    }

    static class SlotViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvSlotTime;

        SlotViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSlotTime = itemView.findViewById(R.id.fromdate);
        }

        void bind(SlotModel slot, OnProjectClickListener listener) {

            String start = slot.getStartsAt().substring(11, 16);

            SimpleDateFormat input =
                    new SimpleDateFormat("HH:mm", Locale.US);

            SimpleDateFormat output =
                    new SimpleDateFormat("hh:mm a", Locale.getDefault());

            try {
                Date date = input.parse(start);
                tvSlotTime.setText(output.format(date));
            } catch (ParseException e) {
                tvSlotTime.setText(start);
            }

            itemView.setOnClickListener(v ->
                    listener.onProjectClick(slot));
        }
    }
}