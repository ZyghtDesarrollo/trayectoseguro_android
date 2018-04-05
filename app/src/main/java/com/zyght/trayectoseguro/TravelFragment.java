package com.zyght.trayectoseguro;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zyght.trayectoseguro.driver_services.DriverTracker;
import com.zyght.trayectoseguro.entity.TravelItem;
import com.zyght.trayectoseguro.entity.TravelItemBLL;
import com.zyght.trayectoseguro.handler.GetTravelsAPIHandler;
import com.zyght.trayectoseguro.network.ResponseActionDelegate;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class TravelFragment extends Fragment implements ResponseActionDelegate, TravelRecyclerViewAdapter.TravelClick{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TravelFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TravelFragment newInstance(int columnCount) {
        TravelFragment fragment = new TravelFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            GetTravelsAPIHandler getTravelsAPIHandler = new GetTravelsAPIHandler();
            getTravelsAPIHandler.setRequestHandle(this, context);

            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void didSuccessfully(String message) {
        recyclerView.setAdapter(new TravelRecyclerViewAdapter(TravelItemBLL.getInstance().getTravelItems(), this));
    }

    @Override
    public void didNotSuccessfully(String message) {

    }

    @Override
    public void onClick(TravelItem travelItem) {
        Toast.makeText(this.getActivity(), "ID de Viaje: "+travelItem.getId(), Toast.LENGTH_LONG).show();

        DriverTracker.getInstance().setCurrentTravelItem(travelItem);

        showDialog();


    }

    public void showDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        SummaryDialogFragment newFragment = new SummaryDialogFragment();




        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // For a little polish, specify a transition animation
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // To make it fullscreen, use the 'content' root view as the container
        // for the fragment, which is always the root view for the activity
        transaction.add(android.R.id.content, newFragment)
                .addToBackStack(null).commit();

    }


    public interface OnListFragmentInteractionListener {

        void onListFragmentInteraction(TravelItem item);
    }
}
