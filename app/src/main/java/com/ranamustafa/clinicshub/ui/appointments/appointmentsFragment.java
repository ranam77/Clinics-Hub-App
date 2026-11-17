
package com.ranamustafa.clinicshub.ui.appointments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ranamustafa.clinicshub.R;
import com.ranamustafa.clinicshub.data.model.AppointmentListItem;
import com.ranamustafa.clinicshub.data.model.AppointmentModel;
import com.ranamustafa.clinicshub.data.model.BookingResponse;
import com.ranamustafa.clinicshub.data.model.SlotListItem;
import com.ranamustafa.clinicshub.data.model.SlotModel;
import com.ranamustafa.clinicshub.utils.SessionManager;
import com.ranamustafa.clinicshub.utils.errors.ErrorHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class appointmentsFragment extends androidx.fragment.app.Fragment {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private TextView tvError;
    private View errorContainer;
    private Button btnRetry;
    private appointmentsAdapter adapter;
    private appointmentsViewModel viewModel;
    SessionManager session ;
    BookingResponse newbooking ;
    TextView bookingdone ;
    private static final String TAG = appointmentsFragment.class.getSimpleName();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(requireContext());
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appointments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.myappointments));
        initializeViews(view);
        setupRecyclerView();
        setupViewModel();
        observeViewModel();
        if(newbooking != null && newbooking.isSuccess())
        {
            bookingdone.setVisibility(View.VISIBLE);
            bookingdone.setText(newbooking.getMessage());
        }
        viewModel.loadData(getContext());
    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.appointmentslist);
        progressBar = view.findViewById(R.id.progressBar);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        tvError = view.findViewById(R.id.tvError);
        errorContainer = view.findViewById(R.id.errorContainer);
        btnRetry = view.findViewById(R.id.btnRetry);
        bookingdone = view.findViewById(R.id.bookingdone);
    }
    private void setupRecyclerView() {
        try {
            adapter = new appointmentsAdapter(this::cencelBooking);

            GridLayoutManager layoutManager =
                    new GridLayoutManager(requireContext(), 3);

            layoutManager.setSpanSizeLookup(
                    new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            if (adapter.getItemViewType(position) == SlotListItem.TYPE_DAY) {
                                return 3;
                            }
                            return 3;
                        }
                    });

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

            DividerItemDecoration divider = new DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
            );

            recyclerView.addItemDecoration(divider);

        } catch (Exception e) {
            ErrorHandler.handle(TAG, e);
        }
    }
    private void cencelBooking(int postion , String ref) {
        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.bookingcanceldialogtitle))
                .setMessage(getString(R.string.cancelbookingdialogtext) + ref)
                .setPositiveButton(getString(R.string.cancelappbtn), (dialog, which) -> {
                    viewModel.cancelBooking(getActivity(), ref, deleted -> {
                        if (deleted) {
                            viewModel.ChnageAppointmentState(ref);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(),
                                    "Failed to cancel appointment. Please try again.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });
                })
                .setNegativeButton(getString(R.string.cancelbtn), (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(appointmentsViewModel.class);
    }

    private void observeViewModel() {
        try{
            viewModel.getData().observe(getViewLifecycleOwner(), this::update );
            viewModel.getLoading().observe(getViewLifecycleOwner(), this::updateLoading);
            viewModel.getError().observe(getViewLifecycleOwner(), this::updateError);
            btnRetry.setOnClickListener(v -> viewModel.loadData(getContext()));
        }
        catch (Exception e)
        {
            ErrorHandler.handle(TAG, e);
        }
    }

    private void update (List<AppointmentModel> data) {
        try{
            List<AppointmentListItem> groupedItems = groupAppointmentsByDay(data);
            adapter.submitList(groupedItems);
            boolean isEmpty = data == null || data.isEmpty();
            tvEmpty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        }
        catch (Exception e)
        {
            ErrorHandler.handle(TAG, e);
        }

    }

    private void updateLoading(Boolean isLoading) {
        boolean loading = Boolean.TRUE.equals(isLoading);
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    private void updateError(String message) {
        boolean hasError = message != null && !message.isEmpty();
        errorContainer.setVisibility(hasError ? View.VISIBLE : View.GONE);
        if (hasError) {
            tvError.setText(message);
        }
    }
    private List<AppointmentListItem> groupAppointmentsByDay(List<AppointmentModel> apps) {

        Map<String, List<AppointmentModel>> grouped = new TreeMap<>();
        for (AppointmentModel appointment : apps) {
            String date = appointment.getSlot().getStartsAt().substring(0, 10);
            grouped.computeIfAbsent(date, k -> new ArrayList<>()).add(appointment);
        }

        List<AppointmentListItem> items = new ArrayList<>();
        SimpleDateFormat inputFormat =   new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, dd MMMM", Locale.getDefault());
        for (Map.Entry<String, List<AppointmentModel>> entry : grouped.entrySet()) {
            String formattedDate;

            try {
                Date date = inputFormat.parse(entry.getKey());
                formattedDate = outputFormat.format(date);
            } catch (ParseException e) {
                formattedDate = entry.getKey();
            }
            items.add(new AppointmentListItem(SlotListItem.TYPE_DAY , formattedDate , null));

            for (AppointmentModel appointment : entry.getValue()) {
                items.add(new AppointmentListItem(1, null, appointment)) ;
            }
        }

        return items;
    }
}
