package com.example.pokelearn.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pokelearn.Activities.CreateCourse;
import com.example.pokelearn.Activities.CreateQuiz;
import com.example.pokelearn.Activities.Home;
import com.example.pokelearn.Activities.I_MyCourse;
import com.example.pokelearn.Activities.I_MyQuiz;
import com.example.pokelearn.Activities.I_Progress;
import com.example.pokelearn.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InstructorDashFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InstructorDashFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InstructorDashFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private CardView iMyCourseCard, iCreateCourseCard, iProgressCard, iExploreCard, iCreateQuizCard, iMyQuizCard;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public InstructorDashFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InstructorDashFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InstructorDashFragment newInstance(String param1, String param2) {
        InstructorDashFragment fragment = new InstructorDashFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_instructor_dash, container, false);


        iMyCourseCard = (CardView) v.findViewById(R.id.i_myCourses_card);
        iCreateCourseCard = (CardView) v.findViewById(R.id.i_createNewCourse_card);
        iProgressCard = (CardView) v.findViewById(R.id.i_progress_card);
        iExploreCard = (CardView) v.findViewById(R.id.i_explore_card);
        iCreateQuizCard = (CardView) v.findViewById(R.id.i_createQuiz_card);
        iMyQuizCard = (CardView) v.findViewById(R.id.i_myQuiz_card);
        //add click listener to the cards

        iCreateCourseCard.setOnClickListener(this);
        iMyCourseCard.setOnClickListener(this);
        iProgressCard.setOnClickListener(this);
        iExploreCard.setOnClickListener(this);
        iCreateQuizCard.setOnClickListener(this);
        iMyQuizCard.setOnClickListener(this);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()) {
            case R.id.i_myCourses_card:
                i = new Intent(getActivity(), I_MyCourse.class); startActivity(i); break;
            case R.id.i_createNewCourse_card:
                i = new Intent(getActivity(), CreateCourse.class );startActivity(i);  break;
            case R.id.i_progress_card:
                i = new Intent(getActivity(), I_Progress.class );startActivity(i); break;
            case R.id.i_explore_card:
                ((Home) getActivity())
                        .setActionBarTitle("Home");
                getFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();break;
            case R.id.i_createQuiz_card:
                i = new Intent(getActivity(), CreateQuiz.class );startActivity(i);  break;
            case R.id.i_myQuiz_card:
                i = new Intent(getActivity(), I_MyQuiz.class );startActivity(i);  break;

            default:
                break;


        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
