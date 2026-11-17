
package com.ranamustafa.clinicshub.ui.appointments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ranamustafa.clinicshub.R;
import com.ranamustafa.clinicshub.data.model.AppointmentListItem;
import com.ranamustafa.clinicshub.data.model.AppointmentModel;
import com.ranamustafa.clinicshub.data.model.SlotListItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import static java.security.AccessController.getContext;

public class appointmentsAdapter extends ListAdapter<AppointmentListItem, RecyclerView.ViewHolder> {
    private final OnFinishClickListener listener;
    private static final int TYPE_DAY = 0;
    public interface OnFinishClickListener {
        void onDelete(int position , String ref);
    }

    private static final DiffUtil.ItemCallback<AppointmentListItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<AppointmentListItem>() {
                @Override
                public boolean areItemsTheSame(@NonNull AppointmentListItem oldItem, @NonNull AppointmentListItem newItem) {

                    if (oldItem.getType() != newItem.getType())
                        return false;

                    if (oldItem.getType() == TYPE_DAY)
                        return Objects.equals(oldItem.getDay(), newItem.getDay());
                    return oldItem.getBooking().getBookingRef() ==  newItem.getBooking().getBookingRef();
                }

                @Override
                public boolean areContentsTheSame(@NonNull AppointmentListItem oldItem, @NonNull AppointmentListItem newItem) {
                    if (oldItem.getType() == TYPE_DAY)
                        return Objects.equals(oldItem.getDay(), newItem.getDay());
                    return Objects.equals(oldItem.getBooking().getBookingRef(),newItem.getBooking().getBookingRef()
                    );
                }
            };

    public appointmentsAdapter(OnFinishClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_DAY) {
            View view = inflater.inflate(R.layout.slot_day_header, parent, false);
            return new DayViewHolder(view);
        }
        View view = inflater.inflate(R.layout.appointment_item, parent, false);
        return new AppointmentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        AppointmentListItem item = getItem(position);
        if (holder instanceof DayViewHolder) {
            ((DayViewHolder) holder).bind(item.getDay());
        } else if (holder instanceof AppointmentsViewHolder) {
            ((AppointmentsViewHolder) holder).bind(item.getBooking(),position, listener);
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

    static class AppointmentsViewHolder extends RecyclerView.ViewHolder {
        private final TextView ref;
        private final TextView from;
       // private final TextView state;
        private final Button cancel;
        AppointmentsViewHolder(@NonNull View itemView) {
            super(itemView);
            ref = itemView.findViewById(R.id.ref);
            from = itemView.findViewById(R.id.from);
            //state = itemView.findViewById(R.id.state);
            cancel = itemView.findViewById(R.id.btnCancel);
        }
        void bind(AppointmentModel appointment, int position , OnFinishClickListener listener) {
            String start = appointment.getSlot().getStartsAt().substring(11, 16);
            SimpleDateFormat input =  new SimpleDateFormat("HH:mm", Locale.US);
            SimpleDateFormat output = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            try {
                Date date = input.parse(start);
                from.setText(output.format(date));
                ref.setText(appointment.getBookingRef());
             //   state.setText(appointment.getBookingRef());
                if (appointment.getStatus() == 1) {

                    cancel.setText(R.string.canceled);
                    cancel.setEnabled(false);
                    cancel.setBackgroundTintList(
                            ColorStateList.valueOf(Color.GRAY)
                    );
                    cancel.setOnClickListener(null);

                } else {

                    cancel.setText(R.string.cancel);
                    cancel.setEnabled(true);
                    cancel.setBackgroundTintList(ColorStateList.valueOf( Color.GREEN ) );
                    cancel.setOnClickListener(v -> {
                        listener.onDelete(position, appointment.getBookingRef());
                    });
                }
             } catch (ParseException e) {
                from.setText(start);
            }
        }
    }
}