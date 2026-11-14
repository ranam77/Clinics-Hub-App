
package com.ranamustafa.clinicshub.ui.slots;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ranamustafa.clinicshub.R;
import com.ranamustafa.clinicshub.data.model.DoctorModel;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class slotsFragment extends androidx.fragment.app.Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private TextView tvError;
    private View errorContainer;
    private Button btnRetry;
    private slotsAdapter adapter;
    private slotsViewModel viewModel;
    SessionManager session ;
    int DoctorID;
    private static final String TAG = slotsFragment.class.getSimpleName();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
           DoctorID = bundle.getInt("DoctorID", 0);
        }
        session = new SessionManager(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_slots, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.avSlots));
        initializeViews(view);
        setupRecyclerView();
        setupViewModel();
        observeViewModel();
        viewModel.loadData(getContext(),DoctorID);
    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.slots);
        progressBar = view.findViewById(R.id.progressBar);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        tvError = view.findViewById(R.id.tvError);
        errorContainer = view.findViewById(R.id.errorContainer);
        btnRetry = view.findViewById(R.id.btnRetry);
    }

    private void setupRecyclerView() {
        try{
                adapter = new slotsAdapter(item -> {
                    // Handle selected slot here
                });

                GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 3);
                layoutManager.setSpanSizeLookup(
                        new GridLayoutManager.SpanSizeLookup() {
                            @Override
                            public int getSpanSize(int position) {
                                return adapter.getItemViewType(position)
                                        == SlotListItem.TYPE_DAY ? 3 : 1;
                            }
                        });

                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

        } catch (Exception e) {
            ErrorHandler.handle(TAG, e);
        }
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(slotsViewModel.class);
    }

    private void observeViewModel() {
        try{
            viewModel.getData().observe(getViewLifecycleOwner(), this::update );
            viewModel.getLoading().observe(getViewLifecycleOwner(), this::updateLoading);
            viewModel.getError().observe(getViewLifecycleOwner(), this::updateError);

            btnRetry.setOnClickListener(v -> viewModel.loadData(getContext(), DoctorID));
        }
        catch (Exception e)
        {
            ErrorHandler.handle(TAG, e);
        }
    }

    private void update (List<SlotModel> data) {
        try{
            List<SlotListItem> groupedItems = groupSlotsByDay(data);
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
    private List<SlotListItem> groupSlotsByDay(List<SlotModel> slots) {

        Map<String, List<SlotModel>> grouped = new TreeMap<>();

        for (SlotModel slot : slots) {

            String date = slot.getStartsAt().substring(0, 10);

            grouped.computeIfAbsent(date, k -> new ArrayList<>())
                    .add(slot);
        }

        List<SlotListItem> items = new ArrayList<>();

        SimpleDateFormat inputFormat =
                new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        SimpleDateFormat outputFormat =
                new SimpleDateFormat("EEEE, dd MMMM", Locale.getDefault());

        for (Map.Entry<String, List<SlotModel>> entry : grouped.entrySet()) {

            String formattedDate;

            try {
                Date date = inputFormat.parse(entry.getKey());
                formattedDate = outputFormat.format(date);
            } catch (ParseException e) {
                formattedDate = entry.getKey();
            }

            items.add(SlotListItem.day(formattedDate));

            for (SlotModel slot : entry.getValue()) {
                items.add(SlotListItem.slot(slot));
            }
        }

        return items;
    }
}
